package com.portal.job.exceptions;

public class TaskRejectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187488266722250374L;

	public TaskRejectionException() {
		super();
	}

	public TaskRejectionException(final String msg) {
		super(msg);
	}

	public TaskRejectionException(final Throwable t) {
		super(t);
	}

	public TaskRejectionException(final String msg, final Throwable t) {
		super(msg, t);
	}
}
