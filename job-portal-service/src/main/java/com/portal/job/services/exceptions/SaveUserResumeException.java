package com.portal.job.services.exceptions;

public class SaveUserResumeException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public SaveUserResumeException(String msg){
		super(msg);
	}
	
	public SaveUserResumeException(String msg, Throwable e){
		super(msg,e);
	}

}
