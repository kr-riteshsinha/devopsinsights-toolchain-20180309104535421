package com.ibm.tfs.service.watson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.tfs.service.config.TFSConfig;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechSession;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechSessionStatus;

@Service("tfsOrchSTTService")
public class TFSOrchSTTService {

	private static final Logger logger = LoggerFactory.getLogger(TFSOrchSTTService.class.getName());

	// Map to maintain the STT session for a single call, map of hostname-STTSpeechSession
	private static Map<String, SpeechSession> sessionMap = new ConcurrentHashMap<>();

	private String username;
	private String password;
	private String endpoint;

	private long startTime;

	@Autowired
	private TFSConfig tfsConfig;

	public TFSOrchSTTService() {
	}

	public TFSDataModel getSTTResponse(TFSDataModel tfsDataModel) {
		logger.info("TFS Orchestration STT Service - begin");

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
			AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(sttRequestBytes), frmt, sttRequestBytes.length / frmt.getFrameSize());
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, inputAudioFile);
			ais.close();

			// TODO : Testing with actual file
			inputAudioFile = new File("C:\\work\\Cognitive\\TFS\\sample.wav");
			// TODO : need to break this file and call STT that many times...

			SpeechSession speechSession = sessionMap.get(tfsDataModel.getAgentId());
			SpeechSessionStatus status = null;
			if (speechSession != null) {
				try {
					startTime = new Date().getTime();
					status = service.getRecognizeStatus(speechSession).execute();
					System.out.println("Time(ms) for STT SessionStatus API - " + (new Date().getTime() - startTime));
					System.out.println(status);
				} catch (BadRequestException e) {
					logger.info("The session timed out due to inactivity, or the request failed to pass the session cookie.");
					status = null;
					e.printStackTrace();
				} catch (NotFoundException e) {
					logger.info("The specified session was not found, possibly because of an invalid session cookie.");
					status = null;
					e.printStackTrace();
				}
			}

			// create a new session if existing session not found
			if (status == null) {
				startTime = new Date().getTime();
				speechSession = service.createSession(SpeechModel.EN_US_NARROWBANDMODEL).execute();
				System.out.println("Time(ms) for STT CreateSession API - " + (new Date().getTime() - startTime));
				sessionMap.put(tfsDataModel.getAgentId(), speechSession);
			}
			if (speechSession != null) {
				try {
					startTime = new Date().getTime();
					status = service.getRecognizeStatus(speechSession).execute();
					System.out.println("Time(ms) for STT SessionStatus API - " + (new Date().getTime() - startTime));
					System.out.println(status);
				} catch (BadRequestException e) {
					logger.info("The session timed out due to inactivity, or the request failed to pass the session cookie.");
					status = null;
					e.printStackTrace();
				} catch (NotFoundException e) {
					logger.info("The specified session was not found, possibly because of an invalid session cookie.");
					status = null;
					e.printStackTrace();
				}
			}

			System.out.println("Session is : " + speechSession.getSessionId());

			// The below sessionOptions is used to recognize the audio using session
			RecognizeOptions sessionOptions = new RecognizeOptions.Builder().sessionId(speechSession.getSessionId()).contentType("audio/wav; rate=16000")
			        .inactivityTimeout(-1).build();

			// The below option is to get the STT response using asynchronous
			startTime = new Date().getTime();
			SpeechResults results = service.recognize(inputAudioFile, sessionOptions).execute();
			System.out.println("Time(ms) for STT Recognize API - " + (new Date().getTime() - startTime));
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
