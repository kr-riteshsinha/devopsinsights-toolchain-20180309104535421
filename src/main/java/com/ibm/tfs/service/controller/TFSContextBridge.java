package com.ibm.tfs.service.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ibm.tfs.service.config.TFSConfig;

@Component
public class TFSContextBridge implements TFSConfigService, ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Autowired
	private TFSConfig tfsConfig;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}
	
	public static TFSConfigService getTFSConfigService() {
		return applicationContext.getBean(TFSConfigService.class);

	}

	@Override
	public TFSConfig getTFSConfig() {
		return tfsConfig;
	}

}

interface  TFSConfigService {
	public TFSConfig getTFSConfig();
}