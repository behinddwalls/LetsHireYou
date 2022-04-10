package com.portal.job.model;

public class BasicAccountSettingsDataModel {

	private String emailId;
	private String newUnverfiedEmailId;
	private String newUnverfiedWorkEmailId;
	private String workEmailId;
	private String password;
	private String verifyPassword;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getWorkEmailId() {
		return workEmailId;
	}

	public void setWorkEmailId(String workEmailId) {
		this.workEmailId = workEmailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	@Override
	public String toString() {
		return "BasicAccountSettingsDataModel [emailId=" + emailId
				+ ", newUnverfiedEmailId=" + newUnverfiedEmailId
				+ ", newUnverfiedWorkEmailId=" + newUnverfiedWorkEmailId
				+ ", workEmailId=" + workEmailId + ", password=" + password
				+ ", verifyPassword=" + verifyPassword + "]";
	}

	public String getNewUnverfiedEmailId() {
		return newUnverfiedEmailId;
	}

	public void setNewUnverfiedEmailId(String newUnverfiedEmailId) {
		this.newUnverfiedEmailId = newUnverfiedEmailId;
	}

	public String getNewUnverfiedWorkEmailId() {
		return newUnverfiedWorkEmailId;
	}

	public void setNewUnverfiedWorkEmailId(String newUnverfiedWorkEmailId) {
		this.newUnverfiedWorkEmailId = newUnverfiedWorkEmailId;
	}

}
