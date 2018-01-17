package com.ibm.tfs.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WDSResponse {

	@JsonProperty("results")
	private WDSResults[] results;

	private String matching_results;

	public WDSResults[] getResults() {
		return results;
	}

	public void setResults(WDSResults[] results) {
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
		return "ClassPojo [results = " + results + ", matching_results = " + matching_results + "]";
	}
}

class WDSResults {
	private String id;

	private Result_metadata result_metadata;

	private Metadata metadata;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Result_metadata getResult_metadata() {
		return result_metadata;
	}

	public void setResult_metadata(Result_metadata result_metadata) {
		this.result_metadata = result_metadata;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", result_metadata = " + result_metadata + ", metadata = " + metadata + "]";
	}
}

class Metadata {
	private String title;

	private String link;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "ClassPojo [title = " + title + ", link = " + link + "]";
	}
}

class Result_metadata {
	private String score;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ClassPojo [score = " + score + "]";
	}
}
