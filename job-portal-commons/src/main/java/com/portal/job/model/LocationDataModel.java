package com.portal.job.model;

import com.portal.job.enums.Country;

/**
 * @author preetam
 *
 */
public class LocationDataModel {

	private Long locationId;
	private String cityName;
	private String stateName;
	private Country countryEnum;

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Country getCountryEnum() {
		return countryEnum;
	}

	public void setCountryEnum(Country countryEnum) {
		this.countryEnum = countryEnum;
	}

	@Override
	public String toString() {
		return "LocationDataModel [locationId=" + locationId + ", cityName="
				+ cityName + ", stateName=" + stateName + ", countryEnum="
				+ countryEnum + "]";
	}

}
