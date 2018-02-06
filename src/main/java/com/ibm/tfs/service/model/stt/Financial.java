package com.ibm.tfs.service.model.stt;

public class Financial {
	private String end_time;

	private String start_time;

	private String confidence;

	private String normalized_text;

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getNormalized_text() {
		return normalized_text;
	}

	public void setNormalized_text(String normalized_text) {
		this.normalized_text = normalized_text;
	}

	@Override
	public String toString() {
		return "Financial [end_time = " + end_time + ", start_time = " + start_time + ", confidence = " + confidence
				+ ", normalized_text = " + normalized_text + "]";
	}
}