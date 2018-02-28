package com.ibm.smc;



import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.ibm.tfs.service.model.TFSDataModel;
import com.ibm.tfs.service.model.speech_to_text.RecognitionResultHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketCloseCodeReasonListener;
import com.ning.http.client.ws.WebSocketTextListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;

public class SpeectToTextWs implements WebSocketTextListener, WebSocketCloseCodeReasonListener {

	public static String CONTENT_TYPE_PCM16K = "audio/l16; rate=16000";
	public static String CONTENT_TYPE_OPUS = "audio/ogg; codecs=opus";
	//public SpeectToTextMessageService messageService = null;
	private DefaultParams _params;
	public static class DefaultParams {
		public String action = "start";
		public int inactivity_timeout = 360;
		@SerializedName("content-type") public String content_type = CONTENT_TYPE_PCM16K;
		
		public boolean interim_results = true; 
		 // Deprecated on 22 May, 2017
		 // public boolean continuous = true; 
		public int max_alternatives = 1; 
		public boolean timestamps = true; 
		public boolean word_confidence = true;
		public boolean smart_formatting = true;
		public boolean speaker_labels = true;
		public String customization_id = "14ed00ee-17ba-4320-85c9-d689d0614515";
		//public String model="en-US_NarrowbandModel&x-watson-learning-opt-out=1&customization_id=14ed00ee-17ba-4320-85c9-d689d0614515";
	};

	public final int REQUEST_TIMEOUT = 20 * 1000; // ms
	public final int WEBSOCKET_OPEN_STATUS = 200;
	public final int WEBSOCKET_ERROR_STATUS = 401;
	public final int WEBSOCKET_CLOSED_STATUS = 1006;
	private String _endpointURI;
	private String _username;
	private String _password;

	private Gson _gson = new Gson();
	
	public static String DefaultQuery = "x-watson-learning-opt-out=1";


	private WebSocket _socket = null;
	private AsyncHttpClient _client = null;
	private int _status = 0;
	
	private boolean _isConnectionReset = false;
	public final int MAX_CONNECT_RETRY = 1;
	public final int CONNECTION_RETRY_WAIT_MSEC = 3000;

	public SpeectToTextWs(String endpointURI, final String username, final String password,DefaultParams params) {
		_params = params;
		_endpointURI = endpointURI;
		_username = username;
		_password = password;
    }

	public SpeectToTextWs() {
		
    }

	
	@Override
	public void onClose(WebSocket soc, int code, String reason) {
		// This operation is synchronized to avoid a race condition with the connect().
		synchronized(this) {
			//System.out.printf("MESSAGE: ONCLOSE CALLED CNT=[%d] in %s\n", _cntOfOnClosedCalled++, Thread.currentThread().getName());
			_status = WEBSOCKET_CLOSED_STATUS;
			//TODO handle and log some thing on close
//			if (closeHandler != null) {
//				closeHandler.handleClose(soc.toString(), code, reason);
//			}
			
			// When onClose is called in a thread and connect is called in another thread, soc may not equal to _socket.
			// But this operation is synchronized, so soc equals to _socket.
			System.out.printf("DEBUG: soc: " + soc + ", _socket: " + _socket);
			if (soc.isOpen()) {
				System.out.printf("DEBUG: socket status : soc.isOpen[%b]\n", soc.isOpen());
				soc.removeWebSocketListener(this);
				for (int i = 0; i < 20 && soc.isOpen(); i ++) {
					soc.close();
					System.out.printf("DEBUG: socket status : tryclose[%d] soc.isOpen[%b]\n", i, soc.isOpen());
				}
				System.out.printf("DEBUG: socket status : _socket.isOpen[%b]\n", _socket.isOpen());
				if (_socket.isOpen()) {
					_socket.close();
					System.out.printf("DEBUG: socket status : tryclose[%d] _socket.isOpen[%b]\n", 0, _socket.isOpen());
				}
			}
		}
		
	}

	@Override
	public void onClose(WebSocket arg0) {
		// TODO Auto-generated method stub
		System.out.println("On close "+ arg0);
		
	}

	@Override
	public void onError(Throwable arg0) {
		//TODO add some handler to send message 
		_status = WEBSOCKET_ERROR_STATUS;
		System.out.println("on error "+ arg0);
	}

	@Override
	public void onOpen(WebSocket arg0) {
		System.out.println("on open" + arg0);
		
	}
	
	@Override
	public void onMessage(String msg) {
		System.out.println("on message "+msg);
		if(resultHandler == null) {
			resultHandler = new RecognitionResultHandler(new TFSDataModel(),5);
		}
		resultHandler.handleMessage(msg, false);
	}
	
	
	/**
	 * private void connect() is a synchronous method, so this method is also a synchronous method.
	 * To avoid a race condition with the onClose(), make this method synchronized.
	 * @throws Exception
	 */
	public synchronized void connect() throws Exception {		
		if (_status == WEBSOCKET_OPEN_STATUS) {
			System.err.printf("MESSAGE: WS/WSS - CONNECTION IS ALREADY EASTABLISHED, SO NOTHING TO DO.\n");
			return;
		}
		
		for (int cntConnecting = 1; cntConnecting <= MAX_CONNECT_RETRY; cntConnecting++) {
			System.err.printf("MESSAGE: WS/WSS - CONNECTING. TRY=[%d/%d].\n", cntConnecting, MAX_CONNECT_RETRY);
			try {
				connect(_endpointURI, _username, _password);
				System.err.printf("MESSAGE: WS/WSS - CONNECTING SUCCEDED.\n");
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(CONNECTION_RETRY_WAIT_MSEC);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				if (cntConnecting == MAX_CONNECT_RETRY) {
					throw(e);
				}
			}
		}
	}

    private void connect(String endpointURI, final String username, final String password) throws Exception {
    	String apikey = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
    	
    	AsyncHttpClient client = null;
		try {
			client = getAsyncHttpClient(endpointURI);

		    AsyncHttpClient.BoundRequestBuilder requestBuilder = client.prepareGet(endpointURI)
	                .addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + apikey)
	                .addHeader(HttpHeaders.Names.CONNECTION, "Upgrade")
	                .addHeader(HttpHeaders.Names.UPGRADE, "WebSocket")
	                .setRequestTimeout(REQUEST_TIMEOUT);
		    System.err.println("DEBUG: SENDING TIMEOUT: " + this.REQUEST_TIMEOUT);
		    
		    // get() waits for the computation to complete, and then retrieves its result.
		    _socket = requestBuilder.execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(this).build()).get();
		    _status = WEBSOCKET_OPEN_STATUS;
		    _isConnectionReset = true;
		    
		    // SEND HEADER
		    System.err.println("MESSAGE: SENDING HEADER: " + _gson.toJson(_params));
		    _socket.sendMessage(_gson.toJson(_params));
		} catch (Exception e) {
			e.printStackTrace();
			throw(e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
    }
    
    private AsyncHttpClient getAsyncHttpClient(String endpointURI) throws URISyntaxException {
    	AsyncHttpClient client = null;
    	client = new AsyncHttpClient();
		return client;
    }
    
	public void sendBinary(byte[] b) {
		// In case _status is WEBSOCKET_ERROR_STATUS
		 //and _socket is null or closed, try to reconnect
		if (_status == WEBSOCKET_CLOSED_STATUS || 
				(_status == WEBSOCKET_ERROR_STATUS && (_socket == null || !_socket.isOpen())) || _socket == null ) {
			System.out.printf("MESSAGE: RECONNECTING\n");
			try {
				connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (_socket != null) {
			_socket.sendMessage(b);
			//_socket.sendMessage("wss://stream.watsonplatform.net/speech-to-text/api/v1/customizations?language=en-US");
		}
		
	}

	RecognitionResultHandler resultHandler = null;
	public void addResultHandler(RecognitionResultHandler handler) {
		
		resultHandler = handler;
	}
	
	public void stopAction() {
		_socket.sendMessage("{\"action\":\"stop\"}");
	}
	
	
}
