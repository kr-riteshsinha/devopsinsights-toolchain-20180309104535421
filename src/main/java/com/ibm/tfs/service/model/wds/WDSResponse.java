package com.ibm.tfs.service.model.wds;

public class WDSResponse {

	private Results[] results;

	private String matching_results;

	public Results[] getResults() {
		return results;
	}

	public void setResults(Results[] results) {
		this.results = results;
	}

	public String getMatching_results() {
		return matching_results;
	}

	public void setMatching_results(String matching_results) {
		this.matching_results = matching_results;
	}

	@Override
	public String toString() {
		return "WDSResponse [results = " + results + ", matching_results = " + matching_results + "]";
	}
}
