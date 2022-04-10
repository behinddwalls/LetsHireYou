package com.portal.job.model;


/**
 * @author preetam
 *
 */
public class BaseRegisterDataModel {
	private String emailId;
	private String workEmailId;
	private String firstName;
	private String lastName;
	private String password;
	private String verifyPassword;
	private String userType;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getWorkEmailId() {
		return workEmailId;
	}

	public void setWorkEmailId(String workEmailId) {
		this.workEmailId = workEmailId;
	}

	protected void clear() {
		this.setEmailId(null);
		this.setFirstName(null);
		this.setLastName(null);
		this.setPassword(null);
		this.setUserType(null);
		this.setVerifyPassword(null);
		this.setWorkEmailId(null);
	}
}
