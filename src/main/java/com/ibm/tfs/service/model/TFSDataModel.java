package com.ibm.tfs.service.model;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class TFSDataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String agentId;
	private String channelId;
	private byte[] sttRequest;
	private String sttResponse;
	private String wcsResponse;
	private String wdsResponse;
	private String wcsRequest;
	private String wdsRequest;	
	private String responseMessage;
	private String callStartTimestamp;
	private String callEndTimeStamp;
	private String callDurationTimestamp;
	
	private Map<Object, Object> propertiesMap;
	
	public TFSDataModel() {
	}

	public TFSDataModel(String agentId, String channelId, byte[] sttRequest, String sttResponse,
			String wcsResponse, String wdsResponse, String wcsRequest, String wdsRequest, String responseMessage,
			Map<Object, Object> propertiesMap) {
		this.agentId = agentId;
		this.channelId = channelId;		
		this.sttRequest = sttRequest;
		this.sttResponse = sttResponse;
		this.wcsResponse = wcsResponse;
		this.wdsResponse = wdsResponse;
		this.wcsRequest = wcsRequest;
		this.wdsRequest = wdsRequest;
		this.responseMessage = responseMessage;
		this.propertiesMap = propertiesMap;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public byte[] getSttRequest() {
		return sttRequest;
	}

	public void setSttRequest(byte[] sttRequest) {
		this.sttRequest = sttRequest;
	}

	public String getSttResponse() {
		return sttResponse;
	}

	public void setSttResponse(String sttResponse) {
		this.sttResponse = sttResponse;
	}

	public String getWcsResponse() {
		return wcsResponse;
	}

	public void setWcsResponse(String wcsResponse) {
		this.wcsResponse = wcsResponse;
	}

	public String getWdsResponse() {
		return wdsResponse;
	}

	public void setWdsResponse(String wdsResponse) {
		this.wdsResponse = wdsResponse;
	}

	public String getWcsRequest() {
		return wcsRequest;
	}

	public void setWcsRequest(String wcsRequest) {
		this.wcsRequest = wcsRequest;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getWdsRequest() {
		return wdsRequest;
	}

	public void setWdsRequest(String wdsRequest) {
		this.wdsRequest = wdsRequest;
	}

	public Map<Object, Object> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<Object, Object> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}
	
	public String getCallStartTime() {
		return callStartTimestamp;
	}

	public void setCallStartTime(String callStartTime) {
		this.callStartTimestamp = callStartTime;
	}

	public String getCallEndTime() {
		return callEndTimeStamp;
	}

	public void setCallEndTime(String callEndTime) {
		this.callEndTimeStamp = callEndTime;
	}

	public String getCallDurationTime() {
		return callDurationTimestamp;
	}

	public void setCallDurationTime(String callDurationTime) {
		this.callDurationTimestamp = callDurationTime;
	}

	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
