package com.portal.job.enums;

public enum DegreeType {
	PHD(1),
	MASTERS(2),
	BACHELORS(3);
	
	private DegreeType(int value){
		this.value = value;
	}
	private int value;
	
	public int getValue(){
		return this.value;
	}
	
	

}
