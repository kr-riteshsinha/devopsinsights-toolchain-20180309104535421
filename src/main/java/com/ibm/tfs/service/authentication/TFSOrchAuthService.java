package com.ibm.tfs.service.authentication;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

public class TFSOrchAuthService {

	public boolean authenticate(String credential, String configUsername, String configPassword) {
		if (null == credential) {
			return false;
		}
		// header value format will be "Basic encodedstring" for Basic authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = credential.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
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