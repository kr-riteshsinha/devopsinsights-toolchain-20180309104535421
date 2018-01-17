package com.ibm.tfs.service.watson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ibm.tfs.service.model.TFSDataModel;

@Service("tfsOrchWDSService")
public class TFSOrchWDSService {

	private static final Logger logger = LogManager.getLogger(TFSOrchWDSService.class.getName());

	public TFSOrchWDSService() {
	}

	public TFSDataModel getWDSResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration WDS Service - begin");
		
		// TODO: call WDS
		
		return tfsDataModel;
	}
}
