package com.ibm.tfs.service.model;

import javax.websocket.Session;

import com.ibm.smc.SpeectToTextWs;
import com.ibm.tfs.service.model.speech_to_text.RecognitionResultHandler;

public class SessionMapper  {

	private SpeectToTextWs speechToTextWs=null;
	private TFSDataModel dataModel = null;
	private Session wsSession = null;
	private RecognitionResultHandler resultHandler;

	public SpeectToTextWs getSpeechToTextWs() {
		return speechToTextWs;
	}

	public void setSpeechToTextWs(SpeectToTextWs speechToTextWs) {
		this.speechToTextWs = speechToTextWs;
	}

	public TFSDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(TFSDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public Session getWsSession() {
		return wsSession;
	}

	public void setWsSession(Session session) {
		wsSession = session;
	}

	public RecognitionResultHandler getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(RecognitionResultHandler resultHandler) {
		this.resultHandler = resultHandler;
	}
	
}
