package com.ibm.tfs.service.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.smc.SpeectToTextWs;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.SessionMapper;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.tfs.service.model.speech_to_text.RecognitionResultHandler;
import com.ibm.utility.ObjectConverter;

@Service("orchWebSocket")
@ServerEndpoint("/websocketbinary")
public class OrchWebSocket {

	private static Map<String, SessionMapper> clientsMap = Collections
			.synchronizedMap(new HashMap<String, SessionMapper>());
	private static SpeectToTextWs speechToTextWs = null;
	private static final String MODEL = "model";
	private static final String CUSTOMIZATIONID = "&customization_id";
	private static final String ACOUSTINCCUSTOMIZATIONID = "&acoustic_customization_id";
	private static final String WATSON_LEARNING_OPT_OUT = "&x-watson-learning-opt-out";
	private SpeectToTextWs.DefaultParams sttparam = new SpeectToTextWs.DefaultParams();
	private TFSConfig tfsConfig;
	Gson _gson = new Gson();
	private static final Logger logger = LoggerFactory.getLogger(OrchWebSocket.class.getName());
	private static   int sessionCount = 0;

	private TFSConfig getTFSConfig() {
		return TFSContextBridge.getTFSConfigService().getTFSConfig();
	}



	@OnOpen
	public void onOpen(Session session) throws IOException {
		// Get session and WebSocket connection
		sessionCount++;
		logger.info("no of session open : " + sessionCount);
		System.out.println("no of session open : " + sessionCount);
	}

	@OnMessage
	public void onMessage(Session session, String jsonObject) throws IOException {
		logger.info("onMessage called with String parameter :");
		logger.debug(jsonObject);
		System.out.println("onMessage called with String parameter :" + jsonObject);
		session.getBasicRemote().sendText(jsonObject);
//		TFSDataModel model = _gson.fromJson(jsonObject, TFSDataModel.class);
//		SessionMapper sessionMapper = clientsMap.get(model.getHostName());
//
//		byte[] b = model.getSttRequest();
//		if (b != null) {
//			onMessage(session, b);
//		}
	}

	private byte[] convertBytetoVoiceByte(byte[] b) {
		byte[] _b1 = null;
		TFSDataModel model = (TFSDataModel) ObjectConverter.deserialize(b);
		_b1 = model.getSttRequest();
		return _b1;
	}

	@OnMessage
	public void onMessage(Session session, byte[] b) {
		isValidSessionExist(session, b);
	}

	private String createSTTURL() {
		if(tfsConfig == null) {
			tfsConfig = getTFSConfig();
		}
		String endPointURL = tfsConfig.getSttEndPoint();

		StringBuilder urlBuilder = new StringBuilder(endPointURL + "/v1/recognize?");

		if (!StringUtils.isBlank(tfsConfig.getModel())) {
			urlBuilder.append(MODEL + "=" + tfsConfig.getModel());
		}
		if (!StringUtils.isBlank(tfsConfig.getAcousticCustomizationID())) {
			urlBuilder.append(ACOUSTINCCUSTOMIZATIONID + "=" + tfsConfig.getAcousticCustomizationID());
		}

//		if (!StringUtils.isBlank(tfsConfig.getCustomizationId())) {
//			urlBuilder.append(CUSTOMIZATIONID + "=" + tfsConfig.getCustomizationId());
//		}

		if (!StringUtils.isBlank(tfsConfig.getWatsonLearningOptout())) {
			urlBuilder.append(WATSON_LEARNING_OPT_OUT + "=" + tfsConfig.getWatsonLearningOptout());
		}
		
		sttparam.smart_formatting = BooleanUtils.toBoolean(tfsConfig.getSmartFormatting());
		sttparam.interim_results = BooleanUtils.toBoolean(tfsConfig.getIntermiResult());
		sttparam.inactivity_timeout = NumberUtils.toInt(tfsConfig.getInactivityTimeout());
		sttparam.word_confidence = BooleanUtils.toBoolean(tfsConfig.getWordConfidence());
		sttparam.max_alternatives = NumberUtils.toInt(tfsConfig.getMax_alternatives());
		sttparam.timestamps = BooleanUtils.toBoolean(tfsConfig.getTimestamp());

		return urlBuilder.toString();
	}

	private void isValidSessionExist(Session session, byte[] b) {
		// TODO :Decryot and deseralize ;
//		SealedObject sealedObj = (SealedObject) ObjectConverter.deserialize(b);
		
		try {
//		TFSDataModel decryptModel = EncryptionUtility.getInstance().decrypt(sealedObj);
		TFSDataModel model = (TFSDataModel) ObjectConverter.deserialize(b);
		SessionMapper sessionMapper = new SessionMapper();

		if (clientsMap.get(model.getAgentId()) == null) {
			logger.info("New Agent Id registerd " + model.getAgentId());

			sessionMapper.setDataModel(model);
			sessionMapper.setWsSession(session);
			speechToTextWs = new SpeectToTextWs(createSTTURL(), tfsConfig.getSttUsername(), tfsConfig.getSttPassword(),
					sttparam);
			RecognitionResultHandler msgHandler = new RecognitionResultHandler(model, tfsConfig.getMaxWrdBuffer());
			msgHandler.addMessageHandler(new RecognitionResultHandler.MessageHandler() {
				@Override
				public void handleMessage(TFSDataModel model) {
					synchronized (msgHandler) {
						logger.info("buffered message received for agent id " + model.getAgentId());
						logger.debug(model.getSttResponse());
						postToOrcController(sessionMapper, model);
					}
				}
			});

			speechToTextWs.addResultHandler(msgHandler);
			sessionMapper.setSpeechToTextWs(speechToTextWs);
			sessionMapper.getSpeechToTextWs().sendBinary(model.getSttRequest());
			clientsMap.put(model.getAgentId(), sessionMapper);
		} else {
			logger.info("Agent Id already registerd " + model.getAgentId());
			SessionMapper existingMapper = clientsMap.get(model.getAgentId());
			existingMapper.getSpeechToTextWs().sendBinary(model.getSttRequest());
			existingMapper.setWsSession(session);
		}
		
		} catch (Exception e) {
			logger.error(e.getMessage());
			TFSDataModel exceptionModel = new TFSDataModel();
			exceptionModel.setResponseMessage(e.getMessage());
			try {
				session.getBasicRemote().sendText(exceptionModel.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void sendResponse(TFSDataModel model) {
		SessionMapper mapper = clientsMap.get(model.getAgentId());
		try {
			// WCS WDS
			mapper.getWsSession().getBasicRemote().sendText(_gson.toJson(model));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		// WebSocket connection closes
		System.out.println("session closed");
		logger.info("session closed");
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		System.out.println("error" + throwable.getMessage());
            try {
				session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
	}

	public void postToOrcController(SessionMapper sessionMapper, TFSDataModel tfsDataMode) { 
		TFSContextBridge.getTFSOrchController().processSTTResponse(sessionMapper, tfsDataMode);
	}
}
