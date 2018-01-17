package com.ibm.tfs.service.watson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ibm.tfs.service.model.TFSDataModel;

@Service("tfsOrchWCSService")
public class TFSOrchWCSService {

	private static final Logger logger = LogManager.getLogger(TFSOrchWCSService.class.getName());

	public TFSOrchWCSService() {
	}

	public TFSDataModel getWCSResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration WCS Service - begin");
		
		// TODO: call WCS
		
		return tfsDataModel;
	}
}
