package com.ibm.tfs.service.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
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
	
	@Value("${wdsversiondate}")
	private String wdsVersionDate;

	@Value("${customization_id}")
	private String customizationId;
	
	@Value("${speaker_label}")
	private String speakerLabel ;
	
	@Value("${smart_formatting}")
	private String smartFormatting;
	
	@Value("${model}")
	private String model;
	
	@Value("${content_type}")
	private String content_type; //=audio/l16;rate=16000
	
	@Value("${inactivity_timeout}")
	private String inactivityTimeout;
	
	@Value("${interim_results}")
	private String intermiResult;
	
	@Value("${max_alternatives}")
	private String	max_alternatives;
	
	@Value("${word_confidence}")
	private String wordConfidence;
	
	@Value("${acoustic_customization_id}")
	private String acousticCustomizationID;

	@Value("${watson_learning_opt_out}")
	private String watsonLearningOptout;
	
	@Value("${timestamp}")
	private String timestamp;
	
	
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getWatsonLearningOptout() {
		return watsonLearningOptout;
	}

	public void setWatsonLearningOptout(String watsonLearningOptout) {
		this.watsonLearningOptout = watsonLearningOptout;
	}

	public String getAcousticCustomizationID() {
		return acousticCustomizationID;
	}

	public void setAcousticCustomizationID(String acousticCustomizationID) {
		this.acousticCustomizationID = acousticCustomizationID;
	}

	
	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getInactivityTimeout() {
		return inactivityTimeout;
	}

	public void setInactivityTimeout(String inactivityTimeout) {
		this.inactivityTimeout = inactivityTimeout;
	}

	public String getIntermiResult() {
		return intermiResult;
	}

	public void setIntermiResult(String intermiResult) {
		this.intermiResult = intermiResult;
	}

	public String getMax_alternatives() {
		return max_alternatives;
	}

	public void setMax_alternatives(String max_alternatives) {
		this.max_alternatives = max_alternatives;
	}

	public String getWordConfidence() {
		return wordConfidence;
	}

	public void setWordConfidence(String wordConfidence) {
		this.wordConfidence = wordConfidence;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	
	
	public String getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(String customizationId) {
		this.customizationId = customizationId;
	}

	public String getSpeakerLabel() {
		return speakerLabel;
	}

	public void setSpeakerLabel(String speakerLabel) {
		this.speakerLabel = speakerLabel;
	}

	public String getSmartFormatting() {
		return smartFormatting;
	}

	public void setSmartFormatting(String smartFormatting) {
		this.smartFormatting = smartFormatting;
	}
	
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

	public String getWdsVersionDate() {
		return wdsVersionDate;
	}

	public void setWdsVersionDate(String versionDate) {
		this.wdsVersionDate = versionDate;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Value("${tfs.orch.service.username}")
	private String tfsOrchServiceUsername;

	@Value("${tfs.orch.service.password}")
	private String tfsOrchServicePassword;

	public String getTfsOrchServiceUsername() {
		return tfsOrchServiceUsername;
	}

	public void setTfsOrchServiceUsername(String tfsOrchServiceUsername) {
		this.tfsOrchServiceUsername = tfsOrchServiceUsername;
	}

	public String getTfsOrchServicePassword() {
		return tfsOrchServicePassword;
	}

	public void setTfsOrchServicePassword(String tfsOrchServicePassword) {
		this.tfsOrchServicePassword = tfsOrchServicePassword;
	}
	
	@Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("QueryWcsWds");
        executor.initialize();
        return executor;
    }

	@Value("${tfs.orch.service.stt.scrubOrStrip}")
	private String scrubOrStrip;

	@Value("${tfs.orch.service.stt.scrubkey}")
	private String scrubKey;

	public String getScrubOrStrip() {
		return scrubOrStrip;
	}

	public void setScrubOrStrip(String scrubOrStrip) {
		this.scrubOrStrip = scrubOrStrip;
	}

	public String getScrubKey() {
		return scrubKey;
	}

	public void setScrubKey(String scrubKey) {
		this.scrubKey = scrubKey;
	} 	
}
