package com.portal.job.exceptions;

public class InvalidRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187488266722250374L;

	public InvalidRequestException() {
		super();
	}

	public InvalidRequestException(final String msg) {
		super(msg);
	}
}
