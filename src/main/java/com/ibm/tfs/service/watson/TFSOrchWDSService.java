package com.ibm.tfs.service.watson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.query.QueryRequest;
import com.ibm.watson.developer_cloud.discovery.v1.model.query.QueryResponse;

@Service("tfsOrchWDSService")
public class TFSOrchWDSService {

	private static final Logger logger = LogManager.getLogger(TFSOrchWDSService.class.getName());
	private String username;
	private String password;
	private String environmentId;
	private String collectionId;
	private String endpoint;
	private String versionDate;
	
	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchWDSService() {
	}

	public TFSDataModel getWDSResponse(TFSDataModel tfsDataModel) {
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
			QueryRequest.Builder queryBuilder = new QueryRequest.Builder(environmentId, collectionId);
			queryBuilder.query(tfsDataModel.getWdsRequest());

			logger.info("Sending the quey to discovery");
			QueryResponse results = discovery.query(queryBuilder.build()).execute();

			if (results != null) {
				logger.debug("Response from WDS : " + results);
				String json = new Gson().toJson(results);
				logger.debug("Response in JSON : " + json);
				tfsDataModel.setWdsResponse(json);
			}
			
		} catch (Exception e) {
			logger.error("Error in getting the WDS response. " + e.getMessage());
			e.printStackTrace();
		}
		
		return tfsDataModel;
	}
}
