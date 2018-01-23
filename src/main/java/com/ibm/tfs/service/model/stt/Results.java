package com.ibm.tfs.service.model.stt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Results {
	@JsonProperty("final")
	private String isFinal;
	private Alternatives[] alternatives;
	private Keywords_result keywords_result;

	public Results() {
	}

	public String getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(String isFinal) {
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
		return "Results [final = " + isFinal + ", alternatives = " + alternatives + ", keywords_result = "
				+ keywords_result + "]";
	}
}