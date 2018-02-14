package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Intents {
	private String confidence;

	private String intent;

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	@Override
	public String toString() {
		return "Intents [confidence = " + confidence + ", intent = " + intent + "]";
	}
}