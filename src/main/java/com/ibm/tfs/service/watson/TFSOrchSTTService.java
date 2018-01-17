package com.ibm.tfs.service.watson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ibm.tfs.service.model.TFSDataModel;

@Service("tfsOrchSTTService")
public class TFSOrchSTTService {

	private static final Logger logger = LogManager.getLogger(TFSOrchSTTService.class.getName());

	public TFSOrchSTTService() {
	}

	public TFSDataModel getSTTResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration STT Service - begin");
		
		// TODO: call STT
		
		return tfsDataModel;
	}
}
