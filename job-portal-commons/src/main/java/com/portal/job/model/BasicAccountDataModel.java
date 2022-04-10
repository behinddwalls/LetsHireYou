package com.portal.job.model;

import java.util.Date;

public class BasicAccountDataModel {

	private Long accountId;
	private String emailId;
	private String workEmailId;
	private String passwordHash;
	private String forgotPasswordKey;
	private Date forgotPasswordKeyGenerateTime;
	private boolean isJobseeker;
	private boolean isRecruiter;
	private boolean isExpert;
	private String jobseekerVerificationKey;
	private String recruiterVerificationKey;
	private String expertVerificationKey;
	private String emailIdVerificationKey;
	private String workEmailIdVerificationKey;
	private boolean isEmailIdVerified;
	private boolean isWorkEmailIdVerified;
	private String newUnverifiedEmailId;
	private String newUnverfiedWorkEmailId;
	private Date createDate;
	private Date modifiedDate;

	public BasicAccountDataModel(Long accountId, String emailId,
			String workEmailId, String passwordHash, String forgotPasswordKey,
			Date forgotPasswordKeyGenerateTime, boolean isJobseeker,
			boolean isRecruiter, boolean isExpert,
			String jobseekerVerificationKey, String recruiterVerificationKey,
			String expertVerificationKey, Date createDate, Date modeifiedDate,
			String emailidVerificationKey, String workEmailidVerificationKey,
			boolean isEmailidVerified, boolean isWorkEmailidVerified,
			String newUnverifiedEmailId, String newUnverfiedWorkEmailId) {
		this.accountId = accountId;
		this.emailId = emailId;
		this.workEmailId = workEmailId;
		this.passwordHash = passwordHash;
		this.forgotPasswordKey = forgotPasswordKey;
		this.forgotPasswordKeyGenerateTime = forgotPasswordKeyGenerateTime;
		this.isJobseeker = isJobseeker;
		this.isRecruiter = isRecruiter;
		this.isExpert = isExpert;
		this.jobseekerVerificationKey = jobseekerVerificationKey;
		this.recruiterVerificationKey = recruiterVerificationKey;
		this.expertVerificationKey = expertVerificationKey;
		this.createDate = createDate;
		this.modifiedDate = modeifiedDate;
		this.emailIdVerificationKey = emailidVerificationKey;
		this.workEmailIdVerificationKey = workEmailidVerificationKey;
		this.isEmailIdVerified = isEmailidVerified;
		this.isWorkEmailIdVerified = isWorkEmailidVerified;
		this.newUnverifiedEmailId = newUnverifiedEmailId;
		this.newUnverfiedWorkEmailId = newUnverfiedWorkEmailId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String password) {
		this.passwordHash = password;
	}

	public String getForgotPasswordKey() {
		return forgotPasswordKey;
	}

	public void setForgotPasswordKey(String forgotPasswordKey) {
		this.forgotPasswordKey = forgotPasswordKey;
	}

	public Date getForgotPasswordKeyGenerateTime() {
		return forgotPasswordKeyGenerateTime;
	}

	public void setForgotPasswordKeyGenerateTime(
			Date forgotPasswordKeyGenerateTime) {
		this.forgotPasswordKeyGenerateTime = forgotPasswordKeyGenerateTime;
	}

	public String getEmailIdVerificationKey() {
		return emailIdVerificationKey;
	}

	public void setEmailIdVerificationKey(String emailIdVerificationKey) {
		this.emailIdVerificationKey = emailIdVerificationKey;
	}

	public String getWorkEmailIdVerificationKey() {
		return workEmailIdVerificationKey;
	}

	public void setWorkEmailIdVerificationKey(String workEmailIdVerificationKey) {
		this.workEmailIdVerificationKey = workEmailIdVerificationKey;
	}

	public boolean isEmailIdVerified() {
		return isEmailIdVerified;
	}

	public void setEmailIdVerified(boolean isEmailIdVerified) {
		this.isEmailIdVerified = isEmailIdVerified;
	}

	public boolean isWorkEmailIdVerified() {
		return isWorkEmailIdVerified;
	}

	public void setWorkEmailIdVerified(boolean isWorkEmailIdVerified) {
		this.isWorkEmailIdVerified = isWorkEmailIdVerified;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public boolean isJobseeker() {
		return isJobseeker;
	}

	public void setJobseeker(boolean isJobseeker) {
		this.isJobseeker = isJobseeker;
	}

	public boolean isRecruiter() {
		return isRecruiter;
	}

	public void setRecruiter(boolean isRecruiter) {
		this.isRecruiter = isRecruiter;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}

	@Override
	public String toString() {
		return "BasicAccountDataModel [accountId=" + accountId + ", emailId="
				+ emailId + ", workEmailId=" + workEmailId + ", passwordHash="
				+ passwordHash + ", forgotPasswordKey=" + forgotPasswordKey
				+ ", forgotPasswordKeyGenerateTime="
				+ forgotPasswordKeyGenerateTime + ", isJobseeker="
				+ isJobseeker + ", isRecruiter=" + isRecruiter + ", isExpert="
				+ isExpert + ", jobseekerVerificationKey="
				+ jobseekerVerificationKey + ", recruiterVerificationKey="
				+ recruiterVerificationKey + ", expertVerificationKey="
				+ expertVerificationKey + ", emailIdVerificationKey="
				+ emailIdVerificationKey + ", workEmailIdVerificationKey="
				+ workEmailIdVerificationKey + ", isEmailIdVerified="
				+ isEmailIdVerified + ", isWorkEmailIdVerified="
				+ isWorkEmailIdVerified + ", newUnverifiedEmailId="
				+ newUnverifiedEmailId + ", newUnverfiedWorkEmailId="
				+ newUnverfiedWorkEmailId + ", createDate=" + createDate
				+ ", modifiedDate=" + modifiedDate + "]";
	}

	public String getJobseekerVerificationKey() {
		return jobseekerVerificationKey;
	}

	public void setJobseekerVerificationKey(String jobseekerVerificationKey) {
		this.jobseekerVerificationKey = jobseekerVerificationKey;
	}

	public String getRecruiterVerificationKey() {
		return recruiterVerificationKey;
	}

	public void setRecruiterVerificationKey(String recruiterVerificationKey) {
		this.recruiterVerificationKey = recruiterVerificationKey;
	}

	public String getExpertVerificationKey() {
		return expertVerificationKey;
	}

	public void setExpertVerificationKey(String expertVerificationKey) {
		this.expertVerificationKey = expertVerificationKey;
	}

	public String getNewUnverifiedEmailId() {
		return newUnverifiedEmailId;
	}

	public void setNewUnverifiedEmailId(String newUnverifiedEmailId) {
		this.newUnverifiedEmailId = newUnverifiedEmailId;
	}

	public String getNewUnverfiedWorkEmailId() {
		return newUnverfiedWorkEmailId;
	}

	public void setNewUnverfiedWorkEmailId(String newUnverfiedWorkEmailId) {
		this.newUnverfiedWorkEmailId = newUnverfiedWorkEmailId;
	}

}
