package com.evola.edt.service.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExecutionStatus {

	public enum Status {
		OK, NOTOK
	};

	public ExecutionStatus() {
		super();
	}

	public ExecutionStatus(Status status) {
		super();
		this.status = status;
	}

	public ExecutionStatus(String message, Status status) {
		super();
		this.message = message;
		this.status = status;
	}

	private String message;
	private Status status;

	@XmlElement
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
