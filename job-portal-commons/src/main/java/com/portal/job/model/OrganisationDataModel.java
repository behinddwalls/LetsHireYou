package com.portal.job.model;

public class OrganisationDataModel {

	//Member fields.
	private String organisationId;
	private String organisationName;
	private int organisationIndustryCode;
	private int organisationTier;
	
	//Getters and setters.
	public String getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public int getOrganisationIndustryCode() {
		return organisationIndustryCode;
	}
	public void setOrganisationIndustryCode(int organisationIndustryCode) {
		this.organisationIndustryCode = organisationIndustryCode;
	}
	
	@Override
	public String toString() {
		return "OrganisationDataModel [organisationId=" + organisationId
				+ ", organisationName=" + organisationName
				+ ", organisationIndustryCode=" + organisationIndustryCode
				+ ", organisationTier=" + organisationTier + "]";
	}
	public int getOrganisationTier() {
		return organisationTier;
	}
	public void setOrganisationTier(int organisationTier) {
		this.organisationTier = organisationTier;
	}

	
}
