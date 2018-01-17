package com.ibm.tfs.service.model;

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
		return "ClassPojo [input = " + input + ", intents = " + intents + ", context = " + context + ", output = "
				+ output + ", entities = " + entities + "]";
	}
}

class Context {
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
		return "ClassPojo [intent_new = " + intent_new + ", system = " + system + ", conversation_id = "
				+ conversation_id + ", intents_identified = " + intents_identified + "]";
	}
}

class System {
	private String branch_exited;

	private String dialog_request_counter;

	private _node_output_map _node_output_map;

	private Dialog_stack[] dialog_stack;

	private String branch_exited_reason;

	private String dialog_turn_counter;

	public String getBranch_exited() {
		return branch_exited;
	}

	public void setBranch_exited(String branch_exited) {
		this.branch_exited = branch_exited;
	}

	public String getDialog_request_counter() {
		return dialog_request_counter;
	}

	public void setDialog_request_counter(String dialog_request_counter) {
		this.dialog_request_counter = dialog_request_counter;
	}

	public _node_output_map get_node_output_map() {
		return _node_output_map;
	}

	public void set_node_output_map(_node_output_map _node_output_map) {
		this._node_output_map = _node_output_map;
	}

	public Dialog_stack[] getDialog_stack() {
		return dialog_stack;
	}

	public void setDialog_stack(Dialog_stack[] dialog_stack) {
		this.dialog_stack = dialog_stack;
	}

	public String getBranch_exited_reason() {
		return branch_exited_reason;
	}

	public void setBranch_exited_reason(String branch_exited_reason) {
		this.branch_exited_reason = branch_exited_reason;
	}

	public String getDialog_turn_counter() {
		return dialog_turn_counter;
	}

	public void setDialog_turn_counter(String dialog_turn_counter) {
		this.dialog_turn_counter = dialog_turn_counter;
	}

	@Override
	public String toString() {
		return "ClassPojo [branch_exited = " + branch_exited + ", dialog_request_counter = " + dialog_request_counter
				+ ", _node_output_map = " + _node_output_map + ", dialog_stack = " + dialog_stack
				+ ", branch_exited_reason = " + branch_exited_reason + ", dialog_turn_counter = " + dialog_turn_counter
				+ "]";
	}
}

class _node_output_map {
	private String[] Welcome;

	public String[] getWelcome() {
		return Welcome;
	}

	public void setWelcome(String[] Welcome) {
		this.Welcome = Welcome;
	}

	@Override
	public String toString() {
		return "ClassPojo [Welcome = " + Welcome + "]";
	}
}

class Dialog_stack {
	private String dialog_node;

	public String getDialog_node() {
		return dialog_node;
	}

	public void setDialog_node(String dialog_node) {
		this.dialog_node = dialog_node;
	}

	@Override
	public String toString() {
		return "ClassPojo [dialog_node = " + dialog_node + "]";
	}
}

class Output {
	private String[] nodes_visited;

	private String[] text;

	private Action action;

	private String[] log_messages;

	public String[] getNodes_visited() {
		return nodes_visited;
	}

	public void setNodes_visited(String[] nodes_visited) {
		this.nodes_visited = nodes_visited;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String[] getLog_messages() {
		return log_messages;
	}

	public void setLog_messages(String[] log_messages) {
		this.log_messages = log_messages;
	}

	@Override
	public String toString() {
		return "ClassPojo [nodes_visited = " + nodes_visited + ", text = " + text + ", action = " + action
				+ ", log_messages = " + log_messages + "]";
	}
}

class Action {
	private Discovery discovery;

	public Discovery getDiscovery() {
		return discovery;
	}

	public void setDiscovery(Discovery discovery) {
		this.discovery = discovery;
	}

	@Override
	public String toString() {
		return "ClassPojo [discovery = " + discovery + "]";
	}
}

class Discovery {
	private String query_text;

	public String getQuery_text() {
		return query_text;
	}

	public void setQuery_text(String query_text) {
		this.query_text = query_text;
	}

	@Override
	public String toString() {
		return "ClassPojo [query_text = " + query_text + "]";
	}
}

class Input {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ClassPojo [text = " + text + "]";
	}
}

class Intents {
	private String confidence;

	private String intent;

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	@Override
	public String toString() {
		return "ClassPojo [confidence = " + confidence + ", intent = " + intent + "]";
	}
}