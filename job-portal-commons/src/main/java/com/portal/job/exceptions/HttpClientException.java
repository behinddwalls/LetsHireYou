package com.portal.job.exceptions;

public class HttpClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187488266722250374L;

	public HttpClientException() {
		super();
	}

	public HttpClientException(final String msg) {
		super(msg);
	}

	public HttpClientException(final Throwable t) {
		super(t);
	}

	public HttpClientException(final String msg, final Throwable t) {
		super(msg, t);
	}
}
