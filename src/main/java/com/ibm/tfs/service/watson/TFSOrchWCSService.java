package com.ibm.tfs.service.watson;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.exception.WatsonCommunicationException;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@Service("tfsOrchWCSService")
public class TFSOrchWCSService {

	private static final Logger logger = LoggerFactory.getLogger(TFSOrchWCSService.class.getName());
	private String username;
	private String password;
//	private String endpoint;
	private String workspaceId;

	private long startTime;

	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchWCSService() {
	}

	public TFSDataModel getWCSResponse(TFSDataModel tfsDataModel, Context context) throws WatsonCommunicationException {
		logger.info("TFS Orchestration WCS Service - begin");

		username = tfsConfig.getWcsUsername();
		password = tfsConfig.getWcsPassword();
//		endpoint = tfsConfig.getWcsEndPoint();
		workspaceId = tfsConfig.getWcsWorkspaceId();

		try {
			Gson gson = new Gson();

			Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
			if (username != null && password != null) {
				service.setUsernameAndPassword(username, password);
			}
			InputData input = new InputData.Builder(tfsDataModel.getWcsRequest()).build();
			MessageOptions options = new MessageOptions.Builder(workspaceId).input(input).context(context).build();

			startTime = new Date().getTime();
			MessageResponse results = service.message(options).execute();
			logger.debug("Time(ms) for WCS API - " + (new Date().getTime()-startTime));
			if (results != null) {
				logger.debug("Response from WCS : " + results);
				String json = gson.toJson(results);
				logger.debug("Response in JSON : " + json);
				tfsDataModel.setWcsResponse(json);
			}
		} catch (Exception e) {
			logger.error("Error in getting the WCS response. " + e.getMessage());
			e.printStackTrace();
			throw new WatsonCommunicationException("Error in getting the WCS response. " + e.getMessage());
		}

		return tfsDataModel;
	}
}
