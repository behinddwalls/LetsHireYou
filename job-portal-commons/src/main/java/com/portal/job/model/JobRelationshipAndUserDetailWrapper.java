package com.portal.job.model;

import org.springframework.stereotype.Component;

import com.portal.job.enums.JobApplicationStatus;

@Component
public class JobRelationshipAndUserDetailWrapper {

    // Data related to JobRelationshoip.
    private String jobId;
    private String jobTitle;
    private String jobseekerId;
    private String jobRelationshipId;
    private String jobApplicationStatus;
    // some of the fields required for User Experiance.
    private boolean isShortlisted = false;
    private boolean isRejected = false;
    private boolean isWishlisted = false;
    // Data related to User
    private String firstName;
    private String lastName;
    private Integer pastExperienceMonths;
    private String mobileNumber;
    private String profileImageUrl;
    private String profileHeadline;
    private String company;
    private String address;
    private String jobFunction;
    private String jobCreateDate;
    private String percentageMatch;
    private String userResumeLink;


	public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobseekerId() {
        return jobseekerId;
    }

    public void setJobseekerId(String jobseekerId) {
        this.jobseekerId = jobseekerId;
    }

    public String getJobApplicationStatus() {
        return jobApplicationStatus;
    }

    public void setJobApplicationStatus(String jobApplicationStatus) {
        this.jobApplicationStatus = jobApplicationStatus;
        // Set the value of application status flag here.
        if (JobApplicationStatus.SHORTLISTED.getStatus().equals(jobApplicationStatus)) {
            this.isShortlisted = true;
        } else if (JobApplicationStatus.REJECTED.getStatus().equals(jobApplicationStatus)) {
            this.isRejected = true;
        }
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

    public Integer getPastExperienceMonths() {
        return pastExperienceMonths;
    }

    public void setPastExperienceMonths(Integer pastExperienceMonths) {
        this.pastExperienceMonths = pastExperienceMonths;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getJobRelationshipId() {
        return jobRelationshipId;
    }

    public void setJobRelationshipId(String jobRelationshipId) {
        this.jobRelationshipId = jobRelationshipId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileHeadline() {
        return profileHeadline;
    }

    public void setProfileHeadline(String profileHeadline) {
        this.profileHeadline = profileHeadline;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isShortlisted() {
        return isShortlisted;
    }

    public boolean isRejected() {
        return isRejected;
    }
    
    public boolean isWishlisted() {
        return isWishlisted;
    }

    @Override
    public String toString() {
        return "JobRelationshipAndUserDetailWrapper [jobId=" + jobId + ", jobseekerId=" + jobseekerId
                + ", jobApplicationStatus=" + jobApplicationStatus + ", firstName=" + firstName + ", lastName="
                + lastName + ", pastExperienceMonths=" + pastExperienceMonths + ", mobileNumber=" + mobileNumber
                + ", profileImageUrl=" + profileImageUrl + ", userResumeLink=" + userResumeLink + "]";
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobCreateDate() {
        return jobCreateDate;
    }

    public void setJobCreateDate(String jobCreatedate) {
        this.jobCreateDate = jobCreatedate;
    }

    public String getPercentageMatch() {
        return percentageMatch;
    }

    public void setPercentageMatch(String percentageMatch) {
        this.percentageMatch = percentageMatch;
    }

    public void setShortlisted(boolean isShortlisted) {
        this.isShortlisted = isShortlisted;
    }

    public void setRejected(boolean isRejected) {
        this.isRejected = isRejected;
    }
    
    public void setWishlisted(byte isWishlisted) {
    	if(isWishlisted==0)
        this.isWishlisted = false;
    	else
    	this.isWishlisted = true;
    }
    public String getUserResumeLink() {
		return userResumeLink;
	}

	public void setUserResumeLink(String userResumeLink) {
		this.userResumeLink = userResumeLink;
	}

}
