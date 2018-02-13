package com.ibm.tfs.service.watson;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;

@Service("tfsOrchWCSService")
public class TFSOrchWCSService {

	private static final Logger logger = LogManager.getLogger(TFSOrchWCSService.class.getName());
	private String username;
	private String password;
//	private String endpoint;
	private String workspaceId;

	private long startTime;

	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchWCSService() {
	}

	public TFSDataModel getWCSResponse(TFSDataModel tfsDataModel, com.ibm.tfs.service.model.wcs.Context context) {
		logger.info("TFS Orchestration WCS Service - begin");

		username = tfsConfig.getWcsUsername();
		password = tfsConfig.getWcsPassword();
//		endpoint = tfsConfig.getWcsEndPoint();
		workspaceId = tfsConfig.getWcsWorkspaceId();

		try {
			Gson gson = new Gson();
			Context wcsContext = null;

			Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
			if (username != null && password != null) {
				service.setUsernameAndPassword(username, password);
			}
			InputData input = new InputData.Builder(tfsDataModel.getWcsRequest()).build();
			if (context != null) {
				String contextString = gson.toJson(context);
				wcsContext = new ObjectMapper().readValue(contextString, Context.class);
			}
			MessageOptions options = new MessageOptions.Builder(workspaceId).input(input).context(wcsContext).build();

			startTime = new Date().getTime();
			MessageResponse results = service.message(options).execute();
			System.out.println("Time(ms) for WCS API - " + (new Date().getTime()-startTime));
			if (results != null) {
				logger.debug("Response from WCS : " + results);
				String json = gson.toJson(results);
				logger.debug("Response in JSON : " + json);
				tfsDataModel.setWcsResponse(json);
			}
		} catch (IllegalArgumentException | UnauthorizedException e) {
			logger.error("Error in getting the WCS response. " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Error in getting the WCS response. " + e.getMessage());
			e.printStackTrace();
		}

		return tfsDataModel;
	}
}
