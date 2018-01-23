package com.ibm.tfs.service.model.wds;

public class Result_metadata {
	private String score;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Result_metadata [score = " + score + "]";
	}
}