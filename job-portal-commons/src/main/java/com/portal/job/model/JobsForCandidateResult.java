package com.portal.job.model;

public class JobsForCandidateResult {

	private String userId;
	private double percentageMatch;
	// JOb info
	private String jobId;
	private String jobDescription;
	private String maxSalary;
	private String minSalary;
	private String salaryCurrencyCode;
	private String title;
	private String location;
	private String organisationName;
	// newly added fields.
	private String employmentType;
	private String jobExperiance;
	private String jobFunction;
	private String link_to_site;
	private boolean fallback = false;

	@Override
	public String toString() {
		return "JobsForCandidateResult [userId=" + userId
				+ ", percentageMatch=" + percentageMatch + ", jobId=" + jobId
				+ ", jobDescription=" + jobDescription + ", maxSalary="
				+ maxSalary + ", minSalary=" + minSalary
				+ ", salaryCurrencyCode=" + salaryCurrencyCode + ", title="
				+ title + ", location=" + location + ", organisationName="
				+ organisationName + ", employmentType=" + employmentType
				+ ", jobExperiance=" + jobExperiance + ", jobFunction="
				+ jobFunction + ", fallback=" + fallback + "]";
	}

	public boolean isFallback() {
		return fallback;
	}

	public void setFallback(boolean fallback) {
		this.fallback = fallback;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(String maxSalary) {
		this.maxSalary = maxSalary;
	}

	public String getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(String minSalary) {
		this.minSalary = minSalary;
	}

	public String getSalaryCurrencyCode() {
		return salaryCurrencyCode;
	}

	public void setSalaryCurrencyCode(String salaryCurrencyCode) {
		this.salaryCurrencyCode = salaryCurrencyCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getJobExperiance() {
		return jobExperiance;
	}

	public void setJobExperiance(String jobExperiance) {
		this.jobExperiance = jobExperiance;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	public double getPercentageMatch() {
		return percentageMatch;
	}

	public void setPercentageMatch(double percentageMatch) {
		this.percentageMatch = percentageMatch;
	}
	
	public String getLink_to_site() {
		return link_to_site;
	}

	public void setLink_to_site(String link_to_site) {
		this.link_to_site = link_to_site;
	}

}
