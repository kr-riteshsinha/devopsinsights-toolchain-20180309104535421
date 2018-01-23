package com.ibm.tfs.service.model.wcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class System {
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
		return "System [branch_exited = " + branch_exited + ", dialog_request_counter = " + dialog_request_counter
				+ ", _node_output_map = " + _node_output_map + ", dialog_stack = " + dialog_stack
				+ ", branch_exited_reason = " + branch_exited_reason + ", dialog_turn_counter = " + dialog_turn_counter
				+ "]";
	}
}