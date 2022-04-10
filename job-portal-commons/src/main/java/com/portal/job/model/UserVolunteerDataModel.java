package com.portal.job.model;

public class UserVolunteerDataModel {

	private String volunteerId;
	private String volunteerCause;
	private String volunteerDescription;
	private String organisationName;
	private String roleName;
	private String volunteerStartDate;
	private String volunteerEndDate;

	public UserVolunteerDataModel() {

	}

	public String getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(String volunteerId) {
		this.volunteerId = volunteerId;
	}

	public String getVolunteerCause() {
		return volunteerCause;
	}

	public void setVolunteerCause(String volunteerCause) {
		this.volunteerCause = volunteerCause;
	}

	public String getVolunteerDescription() {
		return volunteerDescription;
	}

	public void setVolunteerDescription(String volunteerDescription) {
		this.volunteerDescription = volunteerDescription;
	}

	public String getVolunteerStartDate() {
		return volunteerStartDate;
	}

	public void setVolunteerStartDate(String volunteerStartDate) {
		this.volunteerStartDate = volunteerStartDate;
	}

	public String getVolunteerEndDate() {
		return volunteerEndDate;
	}

	public void setVolunteerEndDate(String volunteerEndDate) {
		this.volunteerEndDate = volunteerEndDate;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public UserVolunteerDataModel(String volunteerId, String volunteerCause,
			String volunteerDescription, String organisationName,
			String roleName, String volunteerStartDate, String volunteerEndDate) {
		super();
		this.volunteerId = volunteerId;
		this.volunteerCause = volunteerCause;
		this.volunteerDescription = volunteerDescription;
		this.organisationName = organisationName;
		this.roleName = roleName;
		this.volunteerStartDate = volunteerStartDate;
		this.volunteerEndDate = volunteerEndDate;
	}
	
	@Override
	public String toString() {
		return "UserVolunteerDataModel [volunteerId=" + volunteerId
				+ ", volunteerCause=" + volunteerCause
				+ ", volunteerDescription=" + volunteerDescription
				+ ", organisationName=" + organisationName + ", roleName="
				+ roleName + ", volunteerStartDate=" + volunteerStartDate
				+ ", volunteerEndDate=" + volunteerEndDate + "]";
	}

}
