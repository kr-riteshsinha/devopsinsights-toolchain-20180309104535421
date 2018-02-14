package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Context {
	private String intent_new;

	private System system;

	private String conversation_id;

	private String[] intents_identified;

	public String getIntent_new() {
		return intent_new;
	}

	public void setIntent_new(String intent_new) {
		this.intent_new = intent_new;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	public String getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(String conversation_id) {
		this.conversation_id = conversation_id;
	}

	public String[] getIntents_identified() {
		return intents_identified;
	}

	public void setIntents_identified(String[] intents_identified) {
		this.intents_identified = intents_identified;
	}

	@Override
	public String toString() {
		return "Context [intent_new = " + intent_new + ", system = " + system + ", conversation_id = "
				+ conversation_id + ", intents_identified = " + intents_identified + "]";
	}
}