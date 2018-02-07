package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class _node_output_map {
	
	@JsonProperty("Welcome")
	private String[] welcome;

	public String[] getWelcome() {
		return welcome;
	}

	public void setWelcome(String[] Welcome) {
		this.welcome = Welcome;
	}

	@Override
	public String toString() {
		return "_node_output_map [welcome = " + welcome + "]";
	}
}