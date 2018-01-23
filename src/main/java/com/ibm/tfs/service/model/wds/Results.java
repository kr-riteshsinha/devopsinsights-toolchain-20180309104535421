package com.ibm.tfs.service.model.wds;

public class Results {
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
		return "Results [id = " + id + ", result_metadata = " + result_metadata + ", metadata = " + metadata + "]";
	}
}