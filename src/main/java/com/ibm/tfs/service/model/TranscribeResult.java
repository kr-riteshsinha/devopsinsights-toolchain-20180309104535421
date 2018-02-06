package com.ibm.tfs.service.model;

//import java.util.HashMap;

public class TranscribeResult {
	String _transcript;
	double _tbeg;
	double _tend;

	public TranscribeResult() {
	}

	public TranscribeResult(String transcript, double tbeg, double tend) {
		_transcript = transcript;
		_tbeg = tbeg;
		_tend = tend;
	}
	public String getTranscript() {
		return _transcript;
	}
	public double getBegin() {
		return _tbeg;
	}
	public double getEnd() {
		return _tend;
	}
}
