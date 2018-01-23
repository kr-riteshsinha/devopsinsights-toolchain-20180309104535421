package com.ibm.tfs.service.model.wcs;

public class WCSResponse {
	
	private Input input;

	private Intents[] intents;

	private Context context;

	private Output output;

	private String[] entities;

	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public Intents[] getIntents() {
		return intents;
	}

	public void setIntents(Intents[] intents) {
		this.intents = intents;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public String[] getEntities() {
		return entities;
	}

	public void setEntities(String[] entities) {
		this.entities = entities;
	}

	@Override
	public String toString() {
		return "WCSResponse [input = " + input + ", intents = " + intents + ", context = " + context + ", output = "
				+ output + ", entities = " + entities + "]";
	}
}