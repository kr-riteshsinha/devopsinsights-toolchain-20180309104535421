package com.ibm.tfs.service.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.SessionMapper;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.tfs.service.model.stt.Alternatives;
import com.ibm.tfs.service.model.stt.Results;
import com.ibm.tfs.service.model.stt.STTResponse;
import com.ibm.tfs.service.model.wcs.Output;
import com.ibm.tfs.service.model.wcs.WCSResponse;
import com.ibm.tfs.service.watson.TFSOrchSTTService;
import com.ibm.tfs.service.watson.TFSOrchWCSService;
import com.ibm.tfs.service.watson.TFSOrchWDSService;
import com.ibm.utility.ObjectConverter;
import com.ibm.utility.PIIScrubbingService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;

@RestController
public class TFSOrchController {

	private static final Logger logger = LogManager.getLogger(TFSOrchController.class.getName());

	// Map to maintain the session for a single call, map of hostname-WCSContext
	private static Map<String, Context> sessionMap = new ConcurrentHashMap<>();

	@Autowired
	TFSOrchSTTService tfsOrchSTTService;
	@Autowired
	TFSOrchWCSService tfsOrchWCSService;
	@Autowired
	TFSOrchWDSService tfsOrchWDSService;
	@Autowired
	private TFSConfig tfsConfig;

	/**
	 * GET method to test the service availability
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tfsOrchService/status", method = RequestMethod.GET)
	public String getMessage() {
		logger.info("TFS Orchestration Service - status check!");
		return "Welcome to TFS Orchestration Service";
	}

	/**
	 * PUT method to remove the current context from the session map
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tfsOrchService/disconnect/{hostname}", method = RequestMethod.PUT)
	public String disconnectSession(@PathVariable("hostname") String hostname) {
		logger.info("TFS Orchestration Service - disconnect session!");
		String response;
		Context remove = sessionMap.remove(hostname);
		if (remove == null) {
			response = "There was no session associated with the hostname " + hostname;
		} else {
			response = "The session associated with the hostname " + hostname + " has been removed";
		}

		return response;
	}

	/**
	 * POST method to post response from Mediation component to STT, WCS and WDS And return the populated TFSDataModel with responses from above services
	 * 
	 * @param tfsDataModel
	 * @return tfsDataModel
	 */
	@RequestMapping(value = "/tfsOrchService", method = RequestMethod.POST)
	@ResponseBody
	public String postMessage(@RequestBody String json) {

		logger.info("TFS Orchestration Service - POST - begin");
		String response = null;
		Context context = null;
		String consolidatedWcsResponse = "";
		String consolidatedWdsResponse = "";
		// Build the response from STT, WCS and WDS
		try {
			TFSDataModel tfsDataModel = new TFSDataModel();
			ObjectMapper mapper = new ObjectMapper();
			tfsDataModel = mapper.readValue(json, TFSDataModel.class);

			// TODO: get the responses from STT, WCS and WDS

			// TEST code: ideally tfsDataModel would have the byte[] populated
			File sampleAudioFile = new File("C:\\work\\Cognitive\\TFS\\sample.wav");
			// InputStream fis = new FileInputStream(sampleAudioFile);
			// byte[] sampleBytes = new byte[(int) sampleAudioFile.length()];
			// fis.read(sampleBytes, 0, sampleBytes.length);
			// fis.close();

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
								if (alternative.getTranscript() != null) {
									tfsDataModel.setWcsRequest(alternative.getTranscript());

									// get caller context if available in session map
									context = sessionMap.get(tfsDataModel.getHostName());
									tfsOrchWCSService.getWCSResponse(tfsDataModel, context);

									if (tfsDataModel.getWcsResponse() != null) {
										consolidatedWcsResponse += tfsDataModel.getWcsResponse();
										ObjectMapper wcsResponseMapper = new ObjectMapper();
										WCSResponse wcsResponse = wcsResponseMapper.readValue(tfsDataModel.getWcsResponse(), WCSResponse.class);

										if (wcsResponse != null) {
											context = wcsResponse.getContext();
											sessionMap.put(tfsDataModel.getHostName(), context);

											Output output = wcsResponse.getOutput();
											if (output.getAction() != null && output.getAction().getDiscovery() != null
											        && output.getAction().getDiscovery().getQuery_text() != null) {
												// Query WDS
												tfsDataModel.setWdsRequest(output.getAction().getDiscovery().getQuery_text());

												tfsOrchWDSService.getWDSResponse(tfsDataModel);

												consolidatedWdsResponse += tfsDataModel.getWdsResponse();
											}
										}
									}
								}
							}
						}
					}
				}
			}

			tfsDataModel.setWcsResponse(consolidatedWcsResponse);
			tfsDataModel.setWdsResponse(consolidatedWdsResponse);

			// TODO: this will be removed later. currently the STT request json looks ugly when printed
			tfsDataModel.setSttRequest(null);

			response = mapper.writeValueAsString(tfsDataModel);

			logger.info("TFS Orchestration Service - processing done");
		} catch (Exception e) {
			logger.error("Error while communicating with Watson services. " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Method to receive Async call after STT response is received to further query WCS/WDS
	 */
	@Async
	@Transactional
	public void processSTTResponse(SessionMapper sessionMapper, TFSDataModel tfsDataModel) {

		logger.info("TFSOrchController.processSTTResponse - begin");
		Context context = null;
		String sttResponse = tfsDataModel.getSttResponse();
		try {
			if (sttResponse != null) {
				logger.debug("sttResponse - before scrubbing - " + sttResponse);
				String scrubbedSTTResponse = PIIScrubbingService.scrubPIIData(sttResponse, tfsConfig.getScrubOrStrip(), tfsConfig.getScrubKey());
				logger.debug("sttResponse - after scrubbing - " + scrubbedSTTResponse);
				// set scrubbed STT response back to TFSDataModel
				tfsDataModel.setSttResponse(scrubbedSTTResponse);
				// set scrubbed STT response to TFSDataModel as WCS Request
				tfsDataModel.setWcsRequest(scrubbedSTTResponse);

				// get caller context if available in session map
				context = sessionMap.get(tfsDataModel.getHostName());
				logger.debug("WCS Context - " + context);
				// call WCS service
				tfsOrchWCSService.getWCSResponse(tfsDataModel, context);

				if (tfsDataModel.getWcsResponse() != null) {
//									consolidatedWcsResponse += tfsDataModel.getWcsResponse();
					ObjectMapper wcsResponseMapper = new ObjectMapper();
					WCSResponse wcsResponse = wcsResponseMapper.readValue(tfsDataModel.getWcsResponse(), WCSResponse.class);

					if (wcsResponse != null) {
						context = wcsResponse.getContext();
						sessionMap.put(tfsDataModel.getHostName(), context);

						Output output = wcsResponse.getOutput();
						if (output.getAction() != null && output.getAction().getDiscovery() != null
						        && output.getAction().getDiscovery().getQuery_text() != null) {
							// Query WDS
							tfsDataModel.setWdsRequest(output.getAction().getDiscovery().getQuery_text());

							tfsOrchWDSService.getWDSResponse(tfsDataModel);

//											consolidatedWdsResponse += tfsDataModel.getWdsResponse();
						}
					}
				}
			}
			
			// convert TFSDataModel back to json string and return
//			ObjectMapper mapper = new ObjectMapper();
//			String tfsDataModelResponse = mapper.writeValueAsString(tfsDataModel);
			tfsDataModel.setSttRequest(null);
			tfsDataModel.setWcsRequest(null);
			byte[] tfsDataModelResponse = ObjectConverter.serialize(tfsDataModel);			
			//sessionMapper.getWsSession().getBasicRemote().sendBinary(ByteBuffer.wrap(tfsDataModelResponse));
			System.out.println("respond send to :"+ tfsDataModel.getAgentId());
			sessionMapper.getWsSession().getBasicRemote().sendText(tfsDataModel.toString());

			logger.info("TFSOrchController.processSTTResponse - begin");
		} catch (Exception e) {
			logger.error("Error while communicating with Watson services. " + e.getMessage());
			e.printStackTrace();
		}

//		tfsDataModel.setWcsResponse(consolidatedWcsResponse);
//		tfsDataModel.setWdsResponse(consolidatedWdsResponse);
	}

}
