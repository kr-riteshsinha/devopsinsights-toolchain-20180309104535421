package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Output {
//	private String[] nodes_visited;

	private String[] text;

	private Action action;

//	private String[] log_messages;

//	public String[] getNodes_visited() {
//		return nodes_visited;
//	}
//
//	public void setNodes_visited(String[] nodes_visited) {
//		this.nodes_visited = nodes_visited;
//	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

//	public String[] getLog_messages() {
//		return log_messages;
//	}
//
//	public void setLog_messages(String[] log_messages) {
//		this.log_messages = log_messages;
//	}
//
//	@Override
//	public String toString() {
//		return "Output [nodes_visited = " + nodes_visited + ", text = " + text + ", action = " + action
//				+ ", log_messages = " + log_messages + "]";
//	}
}