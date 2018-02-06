package com.ibm.smc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class STTService {

	public String recognizeSingleAndReturnTranscript (){
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("62dc089d-131a-4d8b-aa01-d339938557db", "dlNL4oB8TK0z");
		RecognizeOptions options = new RecognizeOptions.Builder()
				.model("en-US_NarrowbandModel")
				//.customizationId(utilityClass.getSTTCustomizationId())
				.contentType("audio/l16; rate=16000")
				.interimResults(true).maxAlternatives(3)
				.keywords(new String[]{"toyota", "financial", "account"})
				.keywordsThreshold(0.5).build();

		StringBuffer transcript = new StringBuffer();
		
		String flag ="0" ;
		
		BaseRecognizeCallback callback = new BaseRecognizeCallback() {
			@Override
			public void onTranscription(SpeechResults speechResults) {
				System.out.println(speechResults);
				if (speechResults != null){
					if (speechResults.getResults() != null && speechResults.getResults().size()>0){
						for (Transcript t : speechResults.getResults()){
							if (t!=null && t.getAlternatives()!= null && t.getAlternatives().size()>0){
								for (SpeechAlternative sa : t.getAlternatives()){
									if (sa!= null && sa.getTranscript()!=null){
										transcript.append(System.getProperty("line.separator") + sa.getTranscript());
									}
								}
							}
						}
					}
				}
			}

			@Override
			public void onDisconnected() {
				//flag = "1";
				//flag = true;
			}
		};

		try {
			service.recognizeUsingWebSocket
			(new FileInputStream("C:/audioStore/Angela_O_No PII_1_6475744010258424712_1_30.wav"), options, callback);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (flag.equalsIgnoreCase("1")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return transcript.toString();
	}
	
	public static void main(String arg[] ) {
		STTService service = new STTService();
		String str = service.recognizeSingleAndReturnTranscript();
		System.out.println(str);
	}
}
