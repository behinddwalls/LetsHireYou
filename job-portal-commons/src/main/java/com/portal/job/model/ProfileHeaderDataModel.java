package com.portal.job.model;

public class ProfileHeaderDataModel {

	private String firstName;
	private String lastName;
	private String ctc;
	private String profileHeadline;
	private String address;
	private String summary;	
	private String jobFunction;
	private String industryName;
	private byte hideCtc;


	@Override
	public String toString() {
		return "ProfileHeaderDataModel [firstName=" + firstName + ", lastName="
				+ lastName + ", ctc=" + ctc + ", profileHeadline="
				+ profileHeadline + ", address=" + address + ", summary="
				+ summary + ", jobFunction="+ jobFunction + ", industryName="+ industryName +"]";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCtc() {
		return ctc;
	}

	public void setCtc(String ctc) {
		this.ctc = ctc;
	}

	public String getProfileHeadline() {
		return profileHeadline;
	}

	public void setProfileHeadline(String profileHeadline) {
		this.profileHeadline = profileHeadline;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public byte getHideCtc() {
		return hideCtc;
	}

	public void setHideCtc(byte hideCtc) {
		this.hideCtc = hideCtc;
	}
}
