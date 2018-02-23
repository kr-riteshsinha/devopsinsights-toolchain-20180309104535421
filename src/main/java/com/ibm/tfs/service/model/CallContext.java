package com.ibm.tfs.service.model;

import com.ibm.watson.developer_cloud.conversation.v1.model.Context;

public class CallContext {
	
//	private String agentId;
	private Context context;
	private long callStartTime;
//	private long callEndTime;
//	private long callDuration;
	
//	public String getAgentId() {
//		return agentId;
//	}
//	public void setAgentId(String agentId) {
//		this.agentId = agentId;
//	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public long getCallStartTime() {
		return callStartTime;
	}
	public void setCallStartTime(long callStartTime) {
		this.callStartTime = callStartTime;
	}
//	public long getCallEndTime() {
//		return callEndTime;
//	}
//	public void setCallEndTime(long callEndTime) {
//		this.callEndTime = callEndTime;
//	}
//	public long getCallDuration() {
//		return callDuration;
//	}
//	public void setCallDuration(long callDuration) {
//		this.callDuration = callDuration;
//	}
	
}
