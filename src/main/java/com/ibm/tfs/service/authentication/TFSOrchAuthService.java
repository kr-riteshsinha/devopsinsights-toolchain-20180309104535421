package com.ibm.tfs.service.authentication;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TFSOrchAuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(TFSOrchAuthFilter.class.getName());

	public boolean authenticate(String credential, String configUsername, String configPassword) {
		if (null == credential) {
			System.err.println("Authorization Header is missing");
			logger.error("Authorization Header is missing");
			return false;
		}
		// header value format will be "Basic encodedstring" for Basic authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = credential.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			logger.error("Error while decoding the Authorization Header info");
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		if (!tokenizer.hasMoreTokens()) {
			return false;
		}
		final String password = tokenizer.nextToken();

		return configUsername.equals(username) && configPassword.equals(password);
	}
}