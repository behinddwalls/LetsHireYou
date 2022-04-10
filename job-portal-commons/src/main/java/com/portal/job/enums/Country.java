package com.portal.job.enums;

public enum Country {

	IN("India");

	final private String countryName;

	private Country(final String countryName) {
		this.countryName = countryName;
	}

	public String getCountryName() {
		return countryName;
	}
}
