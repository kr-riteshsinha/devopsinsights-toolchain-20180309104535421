package com.ibm.tfs.service.model.wcs;

public class Action {
	private Discovery discovery;

	public Discovery getDiscovery() {
		return discovery;
	}

	public void setDiscovery(Discovery discovery) {
		this.discovery = discovery;
	}

	@Override
	public String toString() {
		return "Action [discovery = " + discovery + "]";
	}
}