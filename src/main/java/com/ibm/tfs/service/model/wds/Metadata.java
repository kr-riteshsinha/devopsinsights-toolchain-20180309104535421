package com.ibm.tfs.service.model.wds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
	private String title;

	@JsonProperty("filename")
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
		return "Metadata [title = " + title + ", link = " + link + "]";
	}
}