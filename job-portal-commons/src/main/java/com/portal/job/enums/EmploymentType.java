package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum EmploymentType {

	Temporary("Temporary"), 
	FullTIEM("Full-time"), 
	PartTime("Part-time"),
	Contract("Contract"), 
	Volunteer("Volunteer"), 
	Other("Other"),
	NONE("NONE");
	
	private final static Map<String,EmploymentType> employmentTypeMap = new HashMap<String,EmploymentType>();
	// initialize it statically.
	static {

		for (EmploymentType statueEnum : EmploymentType.values()) {
			employmentTypeMap.put(statueEnum.getEmploymentType(), statueEnum);
		}
	}
	//
	private String employmentType;

	private EmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getEmploymentType() {
		return this.employmentType;
	}
	
	public static EmploymentType getEmploymentType(
			final String applicationStatus) {
		if (employmentTypeMap.containsKey(applicationStatus)) {
			return employmentTypeMap.get(applicationStatus);
		}
		return EmploymentType.NONE;
	}
}
