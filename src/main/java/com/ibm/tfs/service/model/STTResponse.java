package com.ibm.tfs.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class STTResponse {

	private Results[] results;

	private String result_index;

	public Results[] getResults() {
		return results;
	}

	public void setResults(Results[] results) {
		this.results = results;
	}

	public String getResult_index() {
		return result_index;
	}

	public void setResult_index(String result_index) {
		this.result_index = result_index;
	}

	@Override
	public String toString() {
		return "ClassPojo [results = " + results + ", result_index = " + result_index + "]";
	}
}

class Results {

	@JsonProperty("final")
	private String isFinal;

	private Alternatives[] alternatives;

	private Keywords_result keywords_result;

	public String getFinal() {
		return isFinal;
	}

	public void setFinal(String isFinal) {
		this.isFinal = isFinal;
	}

	public Alternatives[] getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(Alternatives[] alternatives) {
		this.alternatives = alternatives;
	}

	public Keywords_result getKeywords_result() {
		return keywords_result;
	}

	public void setKeywords_result(Keywords_result keywords_result) {
		this.keywords_result = keywords_result;
	}

	@Override
	public String toString() {
		return "ClassPojo [final = " + isFinal + ", alternatives = " + alternatives + ", keywords_result = "
				+ keywords_result + "]";
	}
}

class Keywords_result {
	private Financial[] financial;

	public Financial[] getFinancial() {
		return financial;
	}

	public void setFinancial(Financial[] financial) {
		this.financial = financial;
	}

	@Override
	public String toString() {
		return "ClassPojo [financial = " + financial + "]";
	}
}

class Financial {
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
		return "ClassPojo [end_time = " + end_time + ", start_time = " + start_time + ", confidence = " + confidence
				+ ", normalized_text = " + normalized_text + "]";
	}
}

class Alternatives {
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
		return "ClassPojo [transcript = " + transcript + ", timestamps = " + timestamps + ", confidence = " + confidence
				+ "]";
	}
}