package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class _node_output_map {
	
	@JsonProperty("Welcome")
	private String[] Welcome;

	public String[] getWelcome() {
		return Welcome;
	}

	public void setWelcome(String[] Welcome) {
		this.Welcome = Welcome;
	}

	@Override
	public String toString() {
		return "_node_output_map [Welcome = " + Welcome + "]";
	}
}