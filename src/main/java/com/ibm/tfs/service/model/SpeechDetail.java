package com.ibm.tfs.service.model;

public class SpeechDetail {
	
	private double from;
	private double end;
	private int SpeakerId =-1;
	private String word;

	
	public double getFrom() {
		return from;
	}
	public void setFrom(double from) {
		this.from = from;
	}
	public double getEnd() {
		return end;
	}
	public void setEnd(double end) {
		this.end = end;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getSpeakerId() {
		return SpeakerId;
	}
	public void setSpeakerId(int speakerId) {
		SpeakerId = speakerId;
	}


}
