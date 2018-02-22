package com.ibm.tfs.service.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = "AuthFilter")
public class TFSOrchAuthFilter implements javax.servlet.Filter {
	
	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	public static final String configFile = "/WEB-INF/classes/locale/config.properties";
	public static final String configUsernameProp = "tfs.orch.service.username";
	public static final String configPasswordProp = "tfs.orch.service.password";
	
	public static String configUsername = "";
	public static String configPassword = "";
	
	private static final Logger logger = LoggerFactory.getLogger(TFSOrchAuthFilter.class.getName());
	
	static {
		Properties prop = new Properties();
		InputStream in = TFSOrchAuthFilter.class.getResourceAsStream(configFile);
		try {
			prop.load(in);
			configUsername = prop.getProperty(configUsernameProp);
			configPassword = prop.getProperty(configPasswordProp);
			in.close();
		} catch (IOException e) {
			System.out.println("Error while loading auth properties");
			logger.error("Error while loading auth properties");
			e.printStackTrace();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
		System.out.println("AuthFilter - Begin");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

			TFSOrchAuthService tfsOrchAuthService = new TFSOrchAuthService();

			boolean authenticationStatus = tfsOrchAuthService.authenticate(authCredentials, configUsername, configPassword);

			if (authenticationStatus) {
				System.out.println("Auth successful");
				logger.debug("Auth successful");
				filter.doFilter(request, response);
			} else {
				System.out.println("Auth failure");
				logger.error("Auth failure for Request : " + httpServletRequest);
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}