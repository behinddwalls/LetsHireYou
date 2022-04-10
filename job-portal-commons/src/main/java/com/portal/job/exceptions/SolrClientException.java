package com.portal.job.exceptions;

public class SolrClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187488266722250374L;

	public SolrClientException() {
		super();
	}

	public SolrClientException(final String msg) {
		super(msg);
	}

	public SolrClientException(final Throwable t) {
		super(t);
	}

	public SolrClientException(final String msg, final Throwable t) {
		super(msg, t);
	}
}
