package com.portal.job.enums;

public enum FieldOfStudy {
	CS("Computer Science");
	
	private FieldOfStudy(final String fieldOfStudy){
		this.fieldOfStudyValue = fieldOfStudy;
	}
	
	private String fieldOfStudyValue;
	
	public String getFieldOfStudyValue(){
		return this.fieldOfStudyValue;
	}

}
