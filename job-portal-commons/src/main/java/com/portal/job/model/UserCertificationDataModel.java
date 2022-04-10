package com.portal.job.model;

public class UserCertificationDataModel {

	private String certificationId;
	private String organisationName;
	private String certificationStartDate;
	private String certificationEndDate;
	private String certificationName;
	private String certificationUrl;

	public UserCertificationDataModel() {

	}

	public String getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	
	public String getCertificationStartDate() {
		return certificationStartDate;
	}

	public void setCertificationStartDate(String certificationStartDate) {
		this.certificationStartDate = certificationStartDate;
	}

	public String getCertificationEndDate() {
		return certificationEndDate;
	}

	public void setCertificationEndDate(String certificationEndDate) {
		this.certificationEndDate = certificationEndDate;
	}

	public String getCertificationName() {
		return certificationName;
	}

	public String getCertificationUrl() {
		return certificationUrl;
	}

	public void setCertificationUrl(String certificationUrl) {
		this.certificationUrl = certificationUrl;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public UserCertificationDataModel(String certificationId,
			String organisationName, String certificationStartDate,
			String certificationEndDate, String certificationName,
			String certificationUrl) {
		super();
		this.certificationId = certificationId;
		this.organisationName = organisationName;
		this.certificationStartDate = certificationStartDate;
		this.certificationEndDate = certificationEndDate;
		this.certificationName = certificationName;
		this.certificationUrl = certificationUrl;
	}

	@Override
	public String toString() {
		return "UserCertificationDataModel [certificationId=" + certificationId
				+ ", organisationName=" + organisationName
				+ ", certificationStartDate=" + certificationStartDate
				+ ", certificationEndDate=" + certificationEndDate
				+ ", certificationName=" + certificationName
				+ ", certificationUrl=" + certificationUrl + "]";
	}
}
