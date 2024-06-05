package com.project.npp.exceptions;

public class PortRequestNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PortRequestNotFoundException() {
		super();
		
	}

	public PortRequestNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public PortRequestNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PortRequestNotFoundException(String message) {
		super(message);
		
	}

	public PortRequestNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
