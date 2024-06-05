package com.project.npp.exceptions;

public class OperatorNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public OperatorNotFoundException() {
		super();
		
	}

	public OperatorNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public OperatorNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public OperatorNotFoundException(String message) {
		super(message);
		
	}

	public OperatorNotFoundException(Throwable cause) {
		super(cause);
		
	}
	

}
