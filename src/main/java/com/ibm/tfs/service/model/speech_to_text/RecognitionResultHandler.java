package com.ibm.tfs.service.model.speech_to_text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class RecognitionResultHandler implements MessageHandlerEX {
	long _timerBeg = System.currentTimeMillis();
	long _timerNow = System.currentTimeMillis();

	
	TFSDataModel _transcript;
	Gson _gson = new Gson();
	
	static final int dindstep = 10; // initial size = 10;
	ArrayList< ArrayList<Boolean> > dindex = new ArrayList< ArrayList<Boolean> >();
	ArrayList< Double > tthr = new ArrayList<Double>();
	double forceCut;

	public RecognitionResultHandler(TFSDataModel transcript, double forceCut) {
		_transcript = transcript;
		this.forceCut = forceCut;
		System.err.println("forceCut: " + forceCut);
		
		for (int i = 0; i < dindstep; i ++) {
			dindex.add(new ArrayList<Boolean>());
			tthr.add(new Double(0.0)); // Store thresholds to determine whether to do forceCut
		}
		disableSslVerification();
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

		if (isConnectionReset) {
			reset();
		}
		
		if (_messageHandler != null) {
			_messageHandler.handleMessage(jbuf, 0);
		}
		
	}
	
	String _url = null;
	
	public int setupHttpConnection(String url, String user, String pass) {
		_url = url;
		return 0;
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
	
	public synchronized int postSpeechRecognitionResult(String callid, int speechid, String speakertype, int speechstartingtime, int duration, String speechstring) {
		String request = null;
		try {
			request = String.format("callID=%s&speechID=%s&speakerCode=%s&speechStartingTime=%d&durationTime=%d&speechString=%s", 
						callid, speechid, speakertype, speechstartingtime, duration, URLEncoder.encode(speechstring, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		int rc = 0;
		HttpURLConnection conn = null;
		PrintStream ps = null;
		try {
			conn = (HttpURLConnection) new URL(_url).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			ps = new PrintStream(conn.getOutputStream());
			ps.print(request);
			ps.close();
			System.err.println("sending=" + request);
			
			InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = br.readLine()) != null) {
            	System.out.printf("(RX %d byte)", s.length());
//                System.out.println(s);
            }
        	System.out.printf("\n");
            br.close();
		} catch (IOException e) {
			e.printStackTrace();
			rc = -1;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (ps != null) {
				ps.close();
			}
		}
		return rc;
	}
	
	static int _shared_speechid = 0; // ==========> STATIC (SHARED BY ALL INSTANCE) CHECK THREAD SAFETY !!!!!!!!!!!!!!!!!!
	
	synchronized public int getAndIncrementSpeechId() {
		_shared_speechid += 1;
		return _shared_speechid;
	}
	
	private void reset() {
		dindex.clear();
		tthr.clear();
		for (int i = 0; i < dindstep; i ++) {
			dindex.add(new ArrayList<Boolean>());
			tthr.add(new Double(0.0));
		}
	}
    
	public static interface MessageHandler {
        public void handleMessage(String message, int flag);
    }
    public void addMessageHandler(MessageHandler lHandler) {
    	_messageHandler = lHandler;
    }

    protected MessageHandler _messageHandler;
}
