package com.portal.job.enums;

public enum Tier {
	First(1),
	Second(2),
	Last(3);
	
	private Tier(int value){
		this.tierValue = value;
	}
	private int tierValue;
	
	public int getTierValue(){
		return this.tierValue;
	}
	

}
