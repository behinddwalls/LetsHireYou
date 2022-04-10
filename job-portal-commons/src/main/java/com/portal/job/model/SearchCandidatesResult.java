package com.portal.job.model;

import org.apache.commons.lang3.StringUtils;

public class SearchCandidatesResult {

	private Long jobseekerId;
	private String firstName;
	private String lastName;
	private String profileImageUrl;
	private String profileHeadline;
	private String company;
	private String location;
	private String experience;
	private double match;
	private Long jobId;
	private String userResumeLink;

	private boolean fallback = false;

	public boolean isFallback() {
		return fallback;
	}

	public void setFallback(boolean fallback) {
		this.fallback = fallback;
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

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileHeadline() {
		return profileHeadline;
	}

	public void setProfileHeadline(String profileHeadline) {
		this.profileHeadline = profileHeadline;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public double getMatch() {
		return match;
	}

	public void setMatch(double match) {
		this.match = match;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getJobseekerId() {
		return jobseekerId;
	}

	public void setJobseekerId(Long jobseekerId) {
		this.jobseekerId = jobseekerId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public boolean isFirstNameEmpty() {
		return StringUtils.isEmpty(firstName);
	}

	public boolean isLastNameEmpty() {
		return StringUtils.isEmpty(lastName);
	}

	public boolean isProfileImageUrlEmpty() {
		return StringUtils.isEmpty(profileImageUrl);
	}

	public boolean isProfileHeadlineEmpty() {
		return StringUtils.isEmpty(profileHeadline);
	}

	public boolean isLocationEmpty() {
		return StringUtils.isEmpty(location);
	}

	public boolean isCompanyEmpty() {
		return StringUtils.isEmpty(company);
	}

	public boolean isExperienceEmpty() {
		return StringUtils.isEmpty(experience);
	}
	
	public String getUserResumeLink() {
		return userResumeLink;
	}

	public void setUserResumeLink(String userResumeLink) {
		this.userResumeLink = userResumeLink;
	}
	
	@Override
	public String toString() {
		return "SearchCandidatesResult [jobseekerId=" + jobseekerId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", profileImageUrl=" + profileImageUrl + ", profileHeadline="
				+ profileHeadline + ", company=" + company + ", location="
				+ location + ", experience=" + experience + ", match=" + match
				+ ", jobId=" + jobId + ", userResumeLink=" + userResumeLink + ", fallback=" + fallback + "]";
	}
}
