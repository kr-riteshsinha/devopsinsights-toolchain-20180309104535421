package com.ibm.tfs.service.model.stt;

public class Alternatives {
	private String transcript;

	private String[][] timestamps;

	private String confidence;

	public String getTranscript() {
		return transcript;
	}

	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}

	public String[][] getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(String[][] timestamps) {
		this.timestamps = timestamps;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	@Override
	public String toString() {
		return "Alternatives [transcript = " + transcript + ", timestamps = " + timestamps + ", confidence = " + confidence
				+ "]";
	}
}