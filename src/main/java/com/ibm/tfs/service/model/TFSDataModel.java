package com.ibm.tfs.service.model;

import java.util.Map;

public class TFSDataModel {

	private String agentId;
	private String channelId;
	private String hostName;
	private byte[] sttRequest;
	private String sttResponse;
	private String wcsResponse;
	private String wdsResponse;
	private String wcsRequest;
	private String wdsRequest;
	private Map<Object, Object> propertiesMap;
	
	public TFSDataModel() {
	}

	public TFSDataModel(String agentId, String channelId, String hostName, byte[] sttRequest, String sttResponse,
			String wcsResponse, String wdsResponse, String wcsRequest, String wdsRequest,
			Map<Object, Object> propertiesMap) {
		this.agentId = agentId;
		this.channelId = channelId;
		this.hostName = hostName;
		this.sttRequest = sttRequest;
		this.sttResponse = sttResponse;
		this.wcsResponse = wcsResponse;
		this.wdsResponse = wdsResponse;
		this.wcsRequest = wcsRequest;
		this.wdsRequest = wdsRequest;
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

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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
}
