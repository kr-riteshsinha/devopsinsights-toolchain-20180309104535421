package com.ibm.tfs.service.model.wcs;

public class Input {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Input [text = " + text + "]";
	}
}