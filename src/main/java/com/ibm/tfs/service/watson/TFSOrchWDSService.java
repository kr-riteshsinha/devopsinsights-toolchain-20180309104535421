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
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;

@Service("tfsOrchWDSService")
public class TFSOrchWDSService {

	private static final Logger logger = LoggerFactory.getLogger(TFSOrchWDSService.class.getName());
	private String username;
	private String password;
	private String environmentId;
	private String collectionId;
	private String endpoint;
	private String versionDate;

	private long startTime;

	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchWDSService() {
	}

	public TFSDataModel getWDSResponse(TFSDataModel tfsDataModel) throws WatsonCommunicationException {
		logger.info("TFS Orchestration WDS Service - begin");

		collectionId = tfsConfig.getWdsCollectionId();
		environmentId = tfsConfig.getWdsEnvironmentId();
		username = tfsConfig.getWdsUsername();
		password = tfsConfig.getWdsPassword();
		endpoint = tfsConfig.getWdsEndPoint();
		versionDate = tfsConfig.getWdsVersionDate();

		try {
			Discovery discovery = new Discovery(versionDate);
			discovery.setEndPoint(endpoint);
			discovery.setUsernameAndPassword(username, password);
			QueryOptions.Builder queryOptions = new QueryOptions.Builder(environmentId, collectionId);
			queryOptions.addReturnField("id").addReturnField("result_metadata").addReturnField("metadata");
			queryOptions.count(1);
			queryOptions.naturalLanguageQuery(tfsDataModel.getWdsRequest());

			logger.info("Sending the quey to discovery");
			startTime = new Date().getTime();
			QueryResponse results = discovery.query(queryOptions.build()).execute();
			logger.debug("Time(ms) for WDS API - " + (new Date().getTime() - startTime));
			if (results != null) {
				logger.debug("Response from WDS : " + results);
				String json = new Gson().toJson(results);
				logger.debug("Response in JSON : " + json);
				tfsDataModel.setWdsResponse(json);
			}
		} catch (Exception e) {
			logger.error("Error in getting the WDS response. " + e.getMessage());
			e.printStackTrace();
			throw new WatsonCommunicationException("Error in getting the WDS response. " + e.getMessage());
		}

		return tfsDataModel;
	}
}
