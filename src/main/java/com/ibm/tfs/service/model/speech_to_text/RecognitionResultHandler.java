package com.ibm.tfs.service.model.speech_to_text;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.ibm.tfs.service.model.SpeechDetail;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeakerLabel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechTimestamp;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;

public class RecognitionResultHandler implements MessageHandlerEX {
	private long _timerBeg = System.currentTimeMillis();
	private long _timerNow = System.currentTimeMillis();
	private List<String> operatorVoicelst = new ArrayList();
	private TFSDataModel _transcript;
	private Gson _gson = new Gson();
	private int maxword=0;
	private double forceCut;
	protected MessageHandler _messageHandler;
	private ConcurrentSkipListMap<Double, SpeechDetail> speaker1 = new ConcurrentSkipListMap<Double, SpeechDetail>();
	private SortedMap<Double, SpeechDetail> speaker2;
	private int currentSpeaker = -1;
	private SpeechResults results =  null;
	private String agentVoice = null;
	private String operatorVoice = null;
	
	
	public List<String> conutineConverstation = new ArrayList();
	
	public RecognitionResultHandler(TFSDataModel tfsDataModel, int maxword) {
		_transcript = tfsDataModel;
		this.maxword = maxword;
	}
	
	@Override
	public void handleMessage(String jbuf, boolean isConnectionReset) {
		SpeechResults results;
		try {
			// Returns one or more instances of a SpeechRecognitionEvent object depending on the input. 
			// Ref: https://www.ibm.com/watson/developercloud/speech-to-text/api/v1/#recognize_sessionless_nonmp12
			 results = _gson.fromJson(jbuf, SpeechResults.class);
		} catch (com.google.gson.JsonSyntaxException e) {
//			System.err.println("DEBUG: received message is NOT json:" + jbuf);
			return;
		}
//		System.err.println("DEBUG: received message is OK:" + jbuf);
		processJson(jbuf, maxword);
		
		if (isConnectionReset) {
			reset();
		}
		if(agentVoice != null && StringUtils.split(agentVoice,StringUtils.SPACE).length > 10) {
		//	agentVoice = agentVoice+builder.toString();
			this._transcript.setSttResponse(agentVoice);
			_messageHandler.handleMessage(this._transcript);
			agentVoice = null;
		}
		if(operatorVoice != null && StringUtils.split(operatorVoice,StringUtils.SPACE).length > 10) {
			//	agentVoice = agentVoice+builder.toString();
			this._transcript.setSttResponse(operatorVoice);
				_messageHandler.handleMessage(this._transcript);
				operatorVoice = null;
			}
//		
//		if (_messageHandler != null) {
//			_messageHandler.handleMessage(jbuf, 0);
//		}
		
	}
	
	private void processJson(String json,int maxword) {
		 results = _gson.fromJson(json, SpeechResults.class);
		getSpeechWords(results,this.speaker1);
	}
	
	
	private static void disableSslVerification() {
	    try {
	    	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
						throws CertificateException {
				}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
						throws CertificateException {
				}
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
	    	}
	    	};

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
	}
	
	
	private void reset() {
		//TODO clear the buffer if connection is reset.
	}
    
	public static interface MessageHandler {
       // public void handleMessage(String message, int flag);
        public void handleMessage(TFSDataModel tfsDataModel);
    }
    public void addMessageHandler(MessageHandler lHandler) {
    	_messageHandler = lHandler;
    }
	
	public void makeSentence(SortedMap<Double,SpeechDetail> speechDetailmap, int oldSpeaker) {
		
		StringBuilder builder = new StringBuilder();
		for (Double fromTime : speechDetailmap.keySet()) {
				SpeechDetail speechDetail = speechDetailmap.get(fromTime);
				if(speechDetail.getSpeakerId() != -1 && speechDetail.getSpeakerId() == oldSpeaker) {
					builder.append(speechDetail.getWord()+ StringUtils.SPACE);
					speechDetailmap.remove(fromTime);
				}
		}
		if(oldSpeaker == 0 ) {
			if(agentVoice == null) {
				agentVoice = builder.toString();
			}
			if(agentVoice != null && StringUtils.split(agentVoice,StringUtils.SPACE).length < 10) {
				agentVoice = agentVoice+builder.toString();
			} else{
				if(agentVoice != null || builder != null) {
					conutineConverstation.add("Speaker"+ oldSpeaker+": "+agentVoice);
					agentVoice = builder.toString();
				}
			}
				agentVoicelst.add(builder.toString());
		} else {
			if(operatorVoice == null) {
				operatorVoice = builder.toString();
			}
			if(operatorVoice != null && StringUtils.split(operatorVoice,StringUtils.SPACE).length < 10) {
				operatorVoice = operatorVoice+builder.toString();
			} else{
				if(operatorVoice != null) {
					conutineConverstation.add("Speaker"+ oldSpeaker+": "+operatorVoice);
					operatorVoice = builder.toString();
				}
			}
			operatorVoicelst.add(builder.toString());
			
		}
	}
	
	
	List<String> agentVoicelst = new ArrayList();
	public List<String> getAgentVoicelst() {
		return agentVoicelst;
	}

	public void setAgentVoicelst(List<String> agentVoicelst) {
		this.agentVoicelst = agentVoicelst;
	}

	public List<String> getOperatorVoicelst() {
		return operatorVoicelst;
	}

	public void setOperatorVoicelst(List<String> operatorVoicelst) {
		this.operatorVoicelst = operatorVoicelst;
	}

	public void  getSpeechWords(SpeechResults speechResults,SortedMap<Double,SpeechDetail> speechDetailmap) {
		if(speechDetailmap == null ) {
			speechDetailmap = new TreeMap<Double, SpeechDetail>();
		}
		if (speechResults != null){
			if (speechResults.getResults() != null && speechResults.getResults().size()>0){
				for (Transcript t : speechResults.getResults()){
					if (t!=null && t.getAlternatives()!= null && t.getAlternatives().size()>0){
					//	if (t.isFinal()) {
							for (SpeechAlternative sa : t.getAlternatives()){
								if (sa!= null && sa.getTimestamps()!=null){
									for (SpeechTimestamp st: sa.getTimestamps()){
										if (st!= null) {
											SpeechDetail detail = new SpeechDetail();
											double ctmPart3 = st.getStartTime();
											double ctmPart4 = st.getEndTime();
											String ctmPart6 = st.getWord();

											detail.setFrom(st.getStartTime());
											detail.setEnd(st.getEndTime());
											detail.setWord(st.getWord());
											speechDetailmap.put(st.getStartTime(),detail);	
										
										}
									}
								}
							}
					}
				}
			}
		}
		
		if (speechResults != null && speechResults.getSpeakerLabels() != null) {
			//int currentSpeaker = -1;
			for (SpeakerLabel speaker : speechResults.getSpeakerLabels()) {
				double from = speaker.getFrom();
				SpeechDetail speechDetail = speechDetailmap.get(from);
				if (speechDetail != null) {
					int speakar = speaker.getSpeaker();
					if(currentSpeaker == -1) {
						currentSpeaker =speakar ; // first time assigned the speaker 
					}
					if(currentSpeaker== speakar ) {
						speechDetail.setSpeakerId(speaker.getSpeaker());
					}else {
						speechDetail.setSpeakerId(speaker.getSpeaker());
						makeSentence(speechDetailmap,currentSpeaker);
						currentSpeaker =speaker.getSpeaker();
						// make sentence and remove the entry from hash map so that 
					}
				}

			}

		}
	}




}
