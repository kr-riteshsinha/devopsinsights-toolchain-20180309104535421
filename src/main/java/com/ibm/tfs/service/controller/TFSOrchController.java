package com.ibm.tfs.service.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

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
import com.ibm.tfs.service.model.stt.Alternatives;
import com.ibm.tfs.service.model.stt.Results;
import com.ibm.tfs.service.model.stt.STTResponse;
import com.ibm.tfs.service.model.wcs.Context;
import com.ibm.tfs.service.model.wcs.Output;
import com.ibm.tfs.service.model.wcs.WCSResponse;
import com.ibm.tfs.service.watson.TFSOrchSTTService;
import com.ibm.tfs.service.watson.TFSOrchWCSService;
import com.ibm.tfs.service.watson.TFSOrchWDSService;

@RestController
public class TFSOrchController {

	private static final Logger logger = LogManager.getLogger(TFSOrchController.class.getName());
	
	// Map to maintain the session for a single call
	private static Map<String, Context> sessionMap = new HashMap<>();

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

			// TODO: get the responses from STT, WCS and WDS
			
			// TEST code: ideally tfsDataModel would have the byte[] populated
			File sampleAudioFile = new File("C:\\work\\Cognitive\\TFS\\sample.wav");
//			InputStream fis = new FileInputStream(sampleAudioFile);
//			byte[] sampleBytes = new byte[(int) sampleAudioFile.length()];
//			fis.read(sampleBytes, 0, sampleBytes.length);
//			fis.close();
			
			FileInputStream in = new FileInputStream(sampleAudioFile);
			BufferedInputStream bis = new BufferedInputStream(in);
			byte[] sampleBytes = new byte[bis.available()];
			bis.close();

			tfsDataModel.setSttRequest(sampleBytes);
			// TEST code - end

			if (tfsDataModel.getSttRequest() != null) {
				tfsOrchSTTService.getSTTResponse(tfsDataModel);
			}
			
			if (tfsDataModel.getSttResponse() != null) {
				ObjectMapper sttResponseMapper = new ObjectMapper();
				STTResponse sttResponse = sttResponseMapper.readValue(tfsDataModel.getSttResponse(), STTResponse.class);
				if (sttResponse != null) {
					Results[] results = sttResponse.getResults();
					for (Results result : results) {
						if ("true".equalsIgnoreCase(result.getIsFinal())) {
							Alternatives[] alternatives = result.getAlternatives();
							for (Alternatives alternative : alternatives) {
								tfsDataModel.setWcsRequest(alternative.getTranscript());
							}
						}
					}
				}
				
				if (tfsDataModel.getWcsRequest() != null) {
					// get caller context if available in session map
					Context context = sessionMap.get(tfsDataModel.getAgentId());
					tfsOrchWCSService.getWCSResponse(tfsDataModel, context);
					
					if (tfsDataModel.getWcsResponse() != null) {
						ObjectMapper wcsResponseMapper = new ObjectMapper();
						WCSResponse wcsResponse = wcsResponseMapper.readValue(tfsDataModel.getWcsResponse(), WCSResponse.class);
						
						if (wcsResponse != null) {
							// TODO : maintain WCS context in a map for a given caller
							context = wcsResponse.getContext();
							sessionMap.put(tfsDataModel.getAgentId(), context);
							
							Output output = wcsResponse.getOutput();
							if (output.getAction() != null && output.getAction().getDiscovery() != null && output.getAction().getDiscovery().getQuery_text() != null) {
								// Query WDS
								tfsDataModel.setWdsRequest(output.getAction().getDiscovery().getQuery_text());

								tfsOrchWDSService.getWDSResponse(tfsDataModel);
							}

							/*
							if (tfsDataModel.getWdsResponse() != null) {
								ObjectMapper wdsResponseMapper = new ObjectMapper();
								WDSResponse wdsResponse = wdsResponseMapper.readValue(tfsDataModel.getWdsResponse(), WDSResponse.class);
								response = wdsResponseMapper.writeValueAsString(wdsResponse);
							} else {
								response = wcsResponseMapper.writeValueAsString(wcsResponse);
							}
							*/
						}
					}
				}
			}
			
			response = mapper.writeValueAsString(tfsDataModel);

			logger.info("TFS Orchestration Service - processing done");
		} catch (Exception e) {
			logger.error("Error while communicating with Watson services. " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

}
