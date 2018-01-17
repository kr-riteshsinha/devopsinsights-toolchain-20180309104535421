package com.ibm.tfs.service.watson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ibm.tfs.service.model.TFSDataModel;

@Service("tfsOrchService")
public class TFSOrchService {

	private static final Logger logger = LogManager.getLogger(TFSOrchService.class.getName());

	public TFSOrchService() {
	}

	public TFSDataModel getOrchestrationResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration Service - begin");
		return tfsDataModel;
	}
}
