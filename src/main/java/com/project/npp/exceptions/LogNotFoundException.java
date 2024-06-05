package com.project.npp.exceptions;

public class LogNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public LogNotFoundException() {
		super();
		
	}

	public LogNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public LogNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public LogNotFoundException(String message) {
		super(message);
		
	}

	public LogNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
