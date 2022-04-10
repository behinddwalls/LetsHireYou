package com.portal.job.model;

public class UserPatentDataModel {

	private String patentId;
	private String patentApplicationNumber;
	private String patentOffice;
	private String patentDescription;
	private String patentFillingDate;
	private String patentTitle;
	private String patentUrl;
	private String patentStatus;

	public UserPatentDataModel() {

	}

	public UserPatentDataModel(String patentId,
			String patentApplicationNumber, String patentOffice,
			String patentDescription, String patentFillingDate,
			String patentTitle, String patentUrl, String patentStatus) {
		super();
		this.patentId = patentId;
		this.patentApplicationNumber = patentApplicationNumber;
		this.patentOffice = patentOffice;
		this.patentDescription = patentDescription;
		this.patentFillingDate = patentFillingDate;
		this.patentTitle = patentTitle;
		this.patentUrl = patentUrl;
		this.patentStatus = patentStatus;
	}

	public String getPatentStatus() {
		return patentStatus;
	}

	public void setPatentStatus(String patentStatus) {
		this.patentStatus = patentStatus;
	}

	public String getPatentId() {
		return patentId;
	}

	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}

	public String getPatentApplicationNumber() {
		return patentApplicationNumber;
	}

	public void setPatentApplicationNumber(String patentApplicationNumber) {
		this.patentApplicationNumber = patentApplicationNumber;
	}

	public String getPatentOffice() {
		return patentOffice;
	}

	public void setPatentOffice(String patentOffice) {
		this.patentOffice = patentOffice;
	}

	public String getPatentDescription() {
		return patentDescription;
	}

	public void setPatentDescription(String patentDescription) {
		this.patentDescription = patentDescription;
	}

	public String getPatentFillingDate() {
		return patentFillingDate;
	}

	public void setPatentFillingDate(String patentFillingDate) {
		this.patentFillingDate = patentFillingDate;
	}

	public String getPatentTitle() {
		return patentTitle;
	}

	public void setPatentTitle(String patentTitle) {
		this.patentTitle = patentTitle;
	}

	public String getPatentUrl() {
		return patentUrl;
	}

	public void setPatentUrl(String patentUrl) {
		this.patentUrl = patentUrl;
	}

	@Override
	public String toString() {
		return "JobSeekerPatentDataModel [patentId=" + patentId
				+ ", patentApplicationNumber=" + patentApplicationNumber
				+ ", patentOffice=" + patentOffice + ", patentDescription="
				+ patentDescription + ", patentFillingDate="
				+ patentFillingDate + ", patentTitle=" + patentTitle
				+ ", patentUrl=" + patentUrl + ", patentStatus=" + patentStatus
				+ "]";
	}

}
