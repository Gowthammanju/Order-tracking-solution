package com.etrusted.interview.demo.exception;

public class DemoBaseException extends Exception {

	private static final long serialVersionUID = -2757754726462068025L;

	public DemoBaseException(String message) {
		super(message);
	}

	public DemoBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DemoBaseException(Throwable cause) {
		super(cause);
	}
}