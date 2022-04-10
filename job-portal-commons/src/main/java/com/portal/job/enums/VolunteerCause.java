package com.portal.job.enums;

public enum VolunteerCause {
	animalRight("Animal Welfare"),
	artsAndCulture("Arts and Culture"),
	children("Children"),
	civilRights("Civil Rights and Social Action"),
	humanitarianRelief("Disaster and Humanitarian Relief"),
	economicEmpowerment("Economic Empowerment"),
	education("Education"),
	environment("Environment"),
	health("Health Care"),
	humanRights("Human Rights"),
	politics("Politics"),
	povertyAlleviation("Poverty Alleviation"),
	scienceAndTechnology("Science and Technology"),
	socialServices("Social Services");
	
	private String value;
	
	private VolunteerCause(String value){
		this.value = value;
	}
	
	public String getVolunteerCause(){
		return this.value;
	}
	
	public String getName(){
		return this.name();
	}

}
