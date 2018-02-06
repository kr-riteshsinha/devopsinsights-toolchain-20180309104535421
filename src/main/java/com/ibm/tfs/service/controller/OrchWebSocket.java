package com.ibm.tfs.service.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.ibm.smc.SpeectToTextWs;
import com.ibm.tfs.service.model.SessionMapper;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.tfs.service.model.speech_to_text.RecognitionResultHandler;
import com.ibm.utility.ObjectConverter;
import com.ibm.utility.SpeechAudioFileWriter;

@Controller
@ServerEndpoint("/websocketbinary")
public class OrchWebSocket {

	private static Map<String,SessionMapper> clientsMap = Collections.synchronizedMap(new HashMap<String,SessionMapper>());
	private static SpeectToTextWs speechToTextWs = null;
	Gson _gson = new Gson();

	 @OnOpen
	    public void onOpen(Session session) throws IOException {
	        // Get session and WebSocket connection
		 System.out.println("ddd");
	    }
	 
	    @OnMessage
	    public void onMessage(Session session, String jsonObject) throws IOException {
	    	TFSDataModel model = _gson.fromJson(jsonObject, TFSDataModel.class) ;
	    	SessionMapper sessionMapper = clientsMap.get(model.getHostName());
	    	
	    	byte[] b = model.getSttRequest();
	    	if(b != null) {
	    		onMessage(session, b);
	    	}
//	    	
//	    	if(sessionMapper!= null) {
//	    		 sessionMapper.getSpeechToTextWs().sendBinary(model.getSttRequest());
//	    	} else {
//	    		sessionMapper = new SessionMapper();
//	    		sessionMapper.setDataModel(model);
//	    		sessionMapper.setWsSession(session);
//	    		SpeectToTextWs.DefaultParams sttparam = new SpeectToTextWs.DefaultParams();
//		    	SpeectToTextWs speechToTextWs = new SpeectToTextWs("wss://stream.watsonplatform.net/speech-to-text/api/v1/recognize?model=en-US_NarrowbandModel&x-watson-learning-opt-out=1", "62dc089d-131a-4d8b-aa01-d339938557db",
//		    			"dlNL4oB8TK0z", sttparam); 
//		    	sessionMapper.setSpeechToTextWs(speechToTextWs);
//		    	speechToTextWs.sendBinary(model.getSttRequest());
//		    	RecognitionResultHandler resultHandler = new RecognitionResultHandler(model, 0.5);
//		    	speechToTextWs.addResultHandler(resultHandler);
//		    	clientsMap.put(model.getHostName(), sessionMapper);
//	    		
//	    	}
	    }
	    
	    private byte[] convertBytetoVoiceByte(byte[] b) {
			byte[] _b1 = null;
			TFSDataModel model = (TFSDataModel)ObjectConverter.deserialize(b);
			_b1 = model.getSttRequest();
			return _b1;
	    }
	    
	    @OnMessage
	    public void onMessage(Session session,byte[] b ) {
	    	isValidSessionExist(session,b);
	    	
//	    	if(speechToTextWs == null ) {
//			SpeectToTextWs.DefaultParams sttparam = new SpeectToTextWs.DefaultParams();
//			speechToTextWs = new SpeectToTextWs(
//					"wss://stream.watsonplatform.net/speech-to-text/api/v1/recognize?model=en-US_NarrowbandModel&x-watson-learning-opt-out=1&customization_id=14ed00ee-17ba-4320-85c9-d689d0614515",
//					"9ba2eda8-43fc-4f82-8480-b2e31de1e414", "exWewnIa0kjh", sttparam);
//			// sessionMapper.setSpeechToTextWs(speechToTextWs);
//			TFSDataModel model = new TFSDataModel();
//			RecognitionResultHandler msgHandler = new RecognitionResultHandler(model, 0.5);
//			msgHandler.addMessageHandler(new RecognitionResultHandler.MessageHandler() {
//				@Override
//				public void handleMessage(String message, int flag) {
//					synchronized (msgHandler) {
//						try {
//							if (message != null && session.isOpen()) {
//								session.getBasicRemote().sendText(message);
//							}
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//
//	    	speechToTextWs.addResultHandler(msgHandler);
//	    	}
//	    	byte[] sttVoicedata = convertBytetoVoiceByte(b);
//	    	System.out.println("c");
//	    	speechToTextWs.sendBinary(sttVoicedata);
//	    	try {
//				SpeechAudioFileWriter.writeTofile(b);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	    }
	    
	    private void isValidSessionExist(Session session,byte[] b) {
	    	//TODO :Decryot and deseralize ;
	    	
	    	TFSDataModel model = (TFSDataModel)ObjectConverter.deserialize(b);
	    	SessionMapper sessionMapper = new SessionMapper();
	    	
	    	if(clientsMap.get(model.getAgentId()) == null) {
	
				sessionMapper.setDataModel(model);
				sessionMapper.setWsSession(session);
				SpeectToTextWs.DefaultParams sttparam = new SpeectToTextWs.DefaultParams();
				speechToTextWs = new SpeectToTextWs(
						"wss://stream.watsonplatform.net/speech-to-text/api/v1/recognize?model=en-US_NarrowbandModel&x-watson-learning-opt-out=1&customization_id=14ed00ee-17ba-4320-85c9-d689d0614515",
						"9ba2eda8-43fc-4f82-8480-b2e31de1e414", "exWewnIa0kjh", sttparam);
				// sessionMapper.setSpeechToTextWs(speechToTextWs);
				RecognitionResultHandler msgHandler = new RecognitionResultHandler(model, 0.5);
				msgHandler.addMessageHandler(new RecognitionResultHandler.MessageHandler() {
					@Override
					public void handleMessage(String message, int flag) {
						synchronized (msgHandler) {
							try {
								if (message != null && session.isOpen()) {
									sessionMapper.getWsSession().getBasicRemote().sendText(message);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
	
				speechToTextWs.addResultHandler(msgHandler);
				sessionMapper.setSpeechToTextWs(speechToTextWs);
				sessionMapper.getSpeechToTextWs().sendBinary(model.getSttRequest());
				clientsMap.put(model.getAgentId(), sessionMapper);
		   } else {
			  SessionMapper existingMapper = clientsMap.get(model.getAgentId());
			   existingMapper.getSpeechToTextWs().sendBinary(model.getSttRequest());
			  existingMapper.setWsSession(session);
		   }
	    
	    }
	    
	    public void sendResponse(TFSDataModel model) {
	    	SessionMapper mapper = clientsMap.get(model.getHostName());
	    	try {
	    		//WCS WDS
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
	    }
	 
	    @OnError
	    public void onError(Session session, Throwable throwable) {
	        // Do error handling here
	    	System.out.println("error");
	    }
	    
	    
}
