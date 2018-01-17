package com.ibm.tfs.service.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.tfs.service.watson.TFSOrchSTTService;
import com.ibm.tfs.service.watson.TFSOrchService;
import com.ibm.tfs.service.watson.TFSOrchWCSService;
import com.ibm.tfs.service.watson.TFSOrchWDSService;

@RestController
public class TFSOrchController {

	private static final Logger logger = LogManager.getLogger(TFSOrchController.class.getName());

	@Autowired
	TFSOrchService tfsOrchService;
	@Autowired
	TFSOrchSTTService tfsOrchSTTService;
	@Autowired
	TFSOrchWCSService tfsOrchWCSService;
	@Autowired
	TFSOrchWDSService tfsOrchWDSService;

	/**
	 * GET method to test the service availability
	 * @return
	 */
	@RequestMapping(value = "/tfsOrchService/status", method = RequestMethod.GET)
	public String getMessage() {
		logger.info("TFS Orchestration Service - status check!");
		return "Welcome to TFS Orchestration Service";
	}

	/**
	 * POST method to post response from Mediation component to STT, WCS and WDS
	 * And return the populated TFSDataModel with responses from above services
	 * 
	 * @param tfsDataModel
	 * @return tfsDataModel
	 */
	@RequestMapping(value = "/tfsOrchService", method = RequestMethod.POST)
	@ResponseBody
	public String postMessage(@RequestBody String json) {

		logger.info("TFS Orchestration Service - POST - begin");
		String response = null;
		// Build the response from STT, WCS and WDS
		try {
			TFSDataModel tfsDataModel = new TFSDataModel();
		    ObjectMapper mapper = new ObjectMapper();
		    tfsDataModel = mapper.readValue(json, TFSDataModel.class);
		    
			// returns the TFSDataModel as-is
			tfsOrchService.getOrchestrationResponse(tfsDataModel);

			// TODO: get the responses from STT, WCS and WDS
			tfsOrchSTTService.getSTTResponse(tfsDataModel);
			if (tfsDataModel.getSttResponse() != null) {
				tfsOrchWCSService.getWCSResponse(tfsDataModel);
			}
			if (tfsDataModel.getWcsResponse() != null) {
				tfsOrchWDSService.getWDSResponse(tfsDataModel);
			}
			
		    // write TFSDataModel back to response
			response =  mapper.writeValueAsString(tfsDataModel);

			logger.info("TFS Orchestration Service - processing done");
		} catch (Exception e) {
			logger.error("Error while communicating with Watson services. " + e.getMessage());
		}
		return response;
	}

}
