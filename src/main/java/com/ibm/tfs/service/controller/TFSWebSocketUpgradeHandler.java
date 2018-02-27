package com.ibm.tfs.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.ws.WebSocketUpgradeHandler;

public class TFSWebSocketUpgradeHandler extends WebSocketUpgradeHandler {

	private static final Logger logger = LoggerFactory.getLogger(TFSWebSocketUpgradeHandler.class.getName());

	@Override
	public com.ning.http.client.AsyncHandler.STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("##### headers #####"+headers);
		System.out.println("##### headers.getHeaders() #####"+headers.getHeaders());
		logger.info("##### headers #####"+headers);
		logger.info("##### headers.getHeaders() #####"+headers.getHeaders());
		
		return super.onHeadersReceived(headers);
	}

	
}
