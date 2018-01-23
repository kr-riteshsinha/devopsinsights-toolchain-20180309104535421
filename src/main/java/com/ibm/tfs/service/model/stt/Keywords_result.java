package com.ibm.tfs.service.model.stt;

public class Keywords_result {
	private Financial[] financial;

	public Financial[] getFinancial() {
		return financial;
	}

	public void setFinancial(Financial[] financial) {
		this.financial = financial;
	}

	@Override
	public String toString() {
		return "Keywords_result [financial = " + financial + "]";
	}
}