package com.portal.job.model;

public class UserAwardDataModel {
	
	private String awardId;
	private String organisationName;
	private String title;
	private String date;

	@Override
	public String toString() {
		return "UserAwardDataModel [awardId=" + awardId + ", organisationName="
				+ organisationName + ", title=" + title + ", date=" + date
				+ "]";
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
