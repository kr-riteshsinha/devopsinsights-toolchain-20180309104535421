package com.ibm.tfs.service.watson;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechSession;

@Service("tfsOrchSTTService")
public class TFSOrchSTTService {

	private static final Logger logger = LogManager.getLogger(TFSOrchSTTService.class.getName());
	private String username;
	private String password;
	private String endpoint;
	
	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchSTTService() {
	}

	public TFSDataModel getSTTResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration STT Service - begin");
		
		// TODO: call STT
		username = tfsConfig.getSttUsername();
		password = tfsConfig.getSttPassword();
		endpoint = tfsConfig.getSttEndPoint();
		try {
			SpeechToText service = new SpeechToText();
			
			if (username != null && password != null) {
				service.setUsernameAndPassword(username, password);
			}
			if (endpoint != null) {
				service.setEndPoint(endpoint);
			}
			
			// convert byte[] to audio file
			File inputAudioFile = new File("C:\\work\\Cognitive\\TFS\\temp\\audio.wav");
			byte[] sttRequestBytes = tfsDataModel.getSttRequest();			
			AudioFormat frmt = new AudioFormat(44100, 8, 1, true, true);
			AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(sttRequestBytes), frmt,sttRequestBytes.length / frmt.getFrameSize());
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, inputAudioFile);
			ais.close();
			
//			ServiceCall<SpeechSession> session = service.createSession("en-US_NarrowbandModel");
//			ServiceCall<SpeechSessionStatus> status = service.getRecognizeStatus(session.execute());

			// TODO : Testing with actual file
			inputAudioFile = new File("C:\\work\\Cognitive\\TFS\\sample.wav");
			
			SpeechSession session = service.createSession().execute();
			System.out.println("Session is : " + session.getSessionId());

			// The below sessionOptions is used to recognize the audio using session
			RecognizeOptions sessionOptions = new RecognizeOptions.Builder().sessionId(session.getSessionId()).contentType("audio/wav; rate=16000").build();

			// The below option is to get the STT response using asynchronous
			SpeechResults results = service.recognize(inputAudioFile, sessionOptions).execute();
			if (results != null) {
				logger.debug("Response from STT : " + results);
				String json = new Gson().toJson(results);
				logger.debug("Response in JSON : " + json);
				tfsDataModel.setSttResponse(json);
			}
		} catch (Exception e) {
			logger.error("Error in getting the STT response. " + e.getMessage());
			e.printStackTrace();
		}
		
		return tfsDataModel;
	}
}
