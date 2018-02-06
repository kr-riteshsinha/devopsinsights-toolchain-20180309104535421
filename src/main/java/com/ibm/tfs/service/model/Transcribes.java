package com.ibm.tfs.service.model;

import java.util.ArrayList;

public class Transcribes extends ArrayList<TranscribeResult> {
	private static final long serialVersionUID = 1L;
	String _name = "";
	String _cid = ""; 
	public void setConversationName(String id) {
		_name = id;
	}
	public String getConversationName() {
		return _name;
	}
	public void setSpeaker(String name) {
		_cid = name;
	}
	public String getSpeaker() {
		return _cid;
	}
}
