package com.wis.basis.common.web.model;

import java.io.Serializable;

public class JsonActionResult implements Serializable {

	private boolean success;
	private String message;
	private Object data;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
