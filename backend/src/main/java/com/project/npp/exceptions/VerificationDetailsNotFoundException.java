package com.project.npp.exceptions;

public class VerificationDetailsNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public VerificationDetailsNotFoundException() {
		super();
	}

	public VerificationDetailsNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VerificationDetailsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public VerificationDetailsNotFoundException(String message) {
		super(message);
	}

	public VerificationDetailsNotFoundException(Throwable cause) {
		super(cause);
	}

}
