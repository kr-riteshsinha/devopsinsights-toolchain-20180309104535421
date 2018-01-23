package com.ibm.tfs.service.model.stt;

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
		return "STTResponse [results = " + results + ", result_index = " + result_index + "]";
	}
}