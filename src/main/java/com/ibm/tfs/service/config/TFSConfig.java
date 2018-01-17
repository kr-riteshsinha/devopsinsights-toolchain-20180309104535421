package com.ibm.tfs.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.ibm.tfs.service.*" })
@PropertySource("classpath:locale/config.properties")
public class TFSConfig {

	@Value("${sttusername}")
	private String sttUsername;

	@Value("${sttpassword}")
	private String sttPassword;

	@Value("${sttendpoint}")
	private String sttEndPoint;

	@Value("${wcsusername}")
	private String wcsUsername;

	@Value("${wcspassword}")
	private String wcsPassword;

	@Value("${wcsworkspaceid}")
	private String wcsWorkspaceId;

	@Value("${wcsendpoint}")
	private String wcsEndPoint;

	@Value("${wdsusername}")
	private String wdsUsername;

	@Value("${wdspassword}")
	private String wdsPassword;

	@Value("${wdscollectionid}")
	private String wdsCollectionId;

	@Value("${wdsenvironmentid}")
	private String wdsEnvironmentId;

	@Value("${wdsendpoint}")
	private String wdsEndPoint;

	public String getSttUsername() {
		return sttUsername;
	}

	public void setSttUsername(String sttUsername) {
		this.sttUsername = sttUsername;
	}

	public String getSttPassword() {
		return sttPassword;
	}

	public void setSttPassword(String sttPassword) {
		this.sttPassword = sttPassword;
	}

	public String getSttEndPoint() {
		return sttEndPoint;
	}

	public void setSttEndPoint(String sttEndPoint) {
		this.sttEndPoint = sttEndPoint;
	}

	public String getWcsUsername() {
		return wcsUsername;
	}

	public void setWcsUsername(String wcsUsername) {
		this.wcsUsername = wcsUsername;
	}

	public String getWcsPassword() {
		return wcsPassword;
	}

	public void setWcsPassword(String wcsPassword) {
		this.wcsPassword = wcsPassword;
	}

	public String getWcsWorkspaceId() {
		return wcsWorkspaceId;
	}

	public void setWcsWorkspaceId(String wcsWorkspaceId) {
		this.wcsWorkspaceId = wcsWorkspaceId;
	}

	public String getWcsEndPoint() {
		return wcsEndPoint;
	}

	public void setWcsEndPoint(String wcsEndPoint) {
		this.wcsEndPoint = wcsEndPoint;
	}

	public String getWdsUsername() {
		return wdsUsername;
	}

	public void setWdsUsername(String wdsUsername) {
		this.wdsUsername = wdsUsername;
	}

	public String getWdsPassword() {
		return wdsPassword;
	}

	public void setWdsPassword(String wdsPassword) {
		this.wdsPassword = wdsPassword;
	}

	public String getWdsCollectionId() {
		return wdsCollectionId;
	}

	public void setWdsCollectionId(String wdsCollectionId) {
		this.wdsCollectionId = wdsCollectionId;
	}

	public String getWdsEnvironmentId() {
		return wdsEnvironmentId;
	}

	public void setWdsEnvironmentId(String wdsEnvironmentId) {
		this.wdsEnvironmentId = wdsEnvironmentId;
	}

	public String getWdsEndPoint() {
		return wdsEndPoint;
	}

	public void setWdsEndPoint(String wdsEndPoint) {
		this.wdsEndPoint = wdsEndPoint;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
