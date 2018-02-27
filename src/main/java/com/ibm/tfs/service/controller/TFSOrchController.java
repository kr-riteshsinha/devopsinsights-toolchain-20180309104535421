package com.ibm.tfs.service.controller;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.CallContext;
import com.ibm.tfs.service.model.SessionMapper;
import com.ibm.tfs.service.model.TFSDataModel;
//import com.ibm.tfs.service.model.stt.Alternatives;
//import com.ibm.tfs.service.model.stt.Results;
//import com.ibm.tfs.service.model.stt.STTResponse;
import com.ibm.tfs.service.model.wcs.Output;
import com.ibm.tfs.service.model.wcs.WCSResponse;
import com.ibm.tfs.service.watson.TFSOrchSTTService;
import com.ibm.tfs.service.watson.TFSOrchWCSService;
import com.ibm.tfs.service.watson.TFSOrchWDSService;
import com.ibm.utility.PIIScrubbingService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;

@RestController
public class TFSOrchController {

	private static final Logger logger = LoggerFactory.getLogger(TFSOrchController.class.getName());

	// Map to maintain the session for a single call, map of AgentId-CallContext
	private static Map<String, CallContext> sessionMap = new ConcurrentHashMap<>();

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
	@RequestMapping(value = "/tfsOrchService/disconnect/{agentId}", method = RequestMethod.PUT)
	public String disconnectSession(@PathVariable("agentId") String agentId) {
		logger.info("TFS Orchestration Service - disconnect session!");
		String response = null;

		// disconnect the STT session for the given agent
		OrchWebSocket.disconnectSTT(agentId);

		TFSDataModel dataModel = new TFSDataModel();
		dataModel.setAgentId(agentId);

		// remove the call session for given agent
		CallContext callContext = sessionMap.remove(agentId);

		if (callContext == null) {
			logger.info("Call session does not exist for agent " + agentId);
			dataModel.setResponseMessage("Call session does not exist for agent " + agentId);
		} else {
			logger.info("Disconnecting call session for agent " + agentId);
			long endTime = new Date().getTime();
			long callDuration = endTime - callContext.getCallStartTime();

			logger.info(
			        "Disconnected call session for agent " + agentId + ". The call duration (in milliseconds) was - " + callDuration);
			dataModel.setResponseMessage(
			        "Disconnected call session for agent " + agentId + ". The call duration (in milliseconds) was - " + callDuration);
			dataModel.setCallStartTime(String.valueOf(callContext.getCallStartTime()));
			dataModel.setCallEndTime(String.valueOf(endTime));
			dataModel.setCallDurationTime(String.valueOf(callDuration));
		}

		response = dataModel.toString();

		return response;
	}

	/**
	 * POST method to post response from Mediation component to STT, WCS and WDS And return the populated TFSDataModel with responses from above services
	 * 
	 * @param tfsDataModel
	 * @return tfsDataModel
	 */
/*	@RequestMapping(value = "/tfsOrchService", method = RequestMethod.POST)
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
									context = sessionMap.get(tfsDataModel.getAgentId());
									tfsOrchWCSService.getWCSResponse(tfsDataModel, context);

									if (tfsDataModel.getWcsResponse() != null) {
										consolidatedWcsResponse += tfsDataModel.getWcsResponse();
										ObjectMapper wcsResponseMapper = new ObjectMapper();
										WCSResponse wcsResponse = wcsResponseMapper.readValue(tfsDataModel.getWcsResponse(), WCSResponse.class);

										if (wcsResponse != null) {
											context = wcsResponse.getContext();
											sessionMap.put(tfsDataModel.getAgentId(), context);

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

			// the STT request json looks ugly when printed, removing it
			tfsDataModel.setSttRequest(null);

			response = mapper.writeValueAsString(tfsDataModel);

			logger.info("TFS Orchestration Service - processing done");
		} catch (Exception e) {
			logger.error("Error while communicating with Watson services. " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
*/

	/**
	 * Method to receive Async call after STT response is received to further query WCS/WDS
	 */
	@Async
	@Transactional
	public synchronized void processSTTResponse(SessionMapper sessionMapper, TFSDataModel mediationTfsDataModel) {

		logger.info("TFSOrchController.processSTTResponse - begin");
		Context context = null;
		CallContext callContext = null;
		TFSDataModel tfsDataModel = new TFSDataModel();
		tfsDataModel.setAgentId(mediationTfsDataModel.getAgentId());
		tfsDataModel.setSttResponse(mediationTfsDataModel.getSttResponse());
		
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
				callContext = sessionMap.get(tfsDataModel.getAgentId());
				if (callContext != null) {
					context = callContext.getContext();
				}
				logger.debug("WCS Context - " + context);
				// call WCS service
				tfsOrchWCSService.getWCSResponse(tfsDataModel, context);

				if (tfsDataModel.getWcsResponse() != null) {
					ObjectMapper wcsResponseMapper = new ObjectMapper();
					WCSResponse wcsResponse = wcsResponseMapper.readValue(tfsDataModel.getWcsResponse(), WCSResponse.class);

					if (wcsResponse != null) {
						context = wcsResponse.getContext();
						if (callContext != null) {
							callContext.setContext(context);							
						} else {
							callContext = new CallContext();
							callContext.setCallStartTime(new Date().getTime());
							callContext.setContext(context);
						}
						sessionMap.put(tfsDataModel.getAgentId(), callContext);

						Output output = wcsResponse.getOutput();
						if (output.getAction() != null && output.getAction().getDiscovery() != null
						        && output.getAction().getDiscovery().getQuery_text() != null) {
							// Query WDS
							tfsDataModel.setWdsRequest(output.getAction().getDiscovery().getQuery_text());
							tfsOrchWDSService.getWDSResponse(tfsDataModel);
						}
					}
				}
			}

			// removing the STT request data from TFS Data model, to reduce the object size
			tfsDataModel.setSttRequest(null);
			// removing the WCS request data since it is duplicate of STT response
			tfsDataModel.setWcsRequest(null);
			
			logger.debug("response sent to "+ tfsDataModel.getAgentId());
			logger.debug("TFSDataModel : " + tfsDataModel.toString());
			sessionMapper.getWsSession().getBasicRemote().sendText(tfsDataModel.toString());

			logger.info("TFSOrchController.processSTTResponse - end");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			tfsDataModel.setResponseMessage(e.getMessage());
		}
	}
}
