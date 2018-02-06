package com.ibm.tfs.service.model.wcs;

public class Discovery {
	private String query_text;

	public String getQuery_text() {
		return query_text;
	}

	public void setQuery_text(String query_text) {
		this.query_text = query_text;
	}

	@Override
	public String toString() {
		return "Discovery [query_text = " + query_text + "]";
	}
}