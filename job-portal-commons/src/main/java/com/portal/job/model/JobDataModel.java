package com.portal.job.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portal.job.constants.JobPortalConstants;
import com.portal.job.enums.JobStatus;
import com.portal.job.enums.ProcessingState;

public class JobDataModel {

	// class for holding the applicant-setting-types.
	public enum ApplicantSettingTypes {
		EMAIL("EMAIL"), EXTERNALURLLINK("URL"), NONE("NONE");

		private final static Map<String, ApplicantSettingTypes> applicantSettingTypesMap = new HashMap<String, ApplicantSettingTypes>();

		static {

			for (ApplicantSettingTypes statueEnum : ApplicantSettingTypes
					.values()) {
				applicantSettingTypesMap.put(statueEnum.getType(), statueEnum);
			}
		}

		public static ApplicantSettingTypes getType(
				final String applicationStatus) {
			if (applicantSettingTypesMap.containsKey(applicationStatus)) {
				return applicantSettingTypesMap.get(applicationStatus);
			}
			return ApplicantSettingTypes.NONE;
		}

		private ApplicantSettingTypes(String type) {
			this.type = type;
		}

		// member field.
		private String type;

		public String getType() {
			return this.type;
		}
	}

	private String jobId;
	private String jdName; // name of the JD file being uploaded.
	private String jobDescription;
	private boolean keepSalaryHidden;
	private boolean topTierOnly;
	private String maxSalary; // will be populated from drop-down
	private String minSalary; // will be populated from drop-down
	private String salaryCurrencyCode; // will be enum, selected from drop-down
	private String title;
	private String location; // will be populated from the drop-down
	private String organisationName;
	// set the organisation tier.
	private Integer organisationTier;
	private String industryName; // populated from drop-down, will be integer
									// eventually
	private String recruiterId; // the id of the recruiter who posted the job

	private String jobDescriptionFileLocation;
	private String jobStatus;
	private String jobExpiaryDate;
	private String jobModifiedDate;
	private String jobCreatedDate;
	// newly added fields.
	private String employmentType;
	private int jobExperience;
	private String applicantSettingTypes;
	private String linkToExternalSite;
	private String jobFunction;
	private String jobProcessingState;
	private Date lastProcessedTime;
	private String jobseekerProcessingState;

	public String getJobseekerProcessingState() {
		return jobseekerProcessingState;
	}

	public void setJobseekerProcessingState(String jobseekerProcessingState) {
		this.jobseekerProcessingState = jobseekerProcessingState;
	}

	//
	private boolean jobClosed;

	@JsonIgnore
	public boolean isJobClosed() {
		return JobStatus.CLOSED.getStatus().equals(this.jobStatus);
	}

	public Date getLastProcessedTime() {
		return lastProcessedTime;
	}

	public void setLastProcessedTime(Date lastProcessedTime) {
		this.lastProcessedTime = lastProcessedTime;
	}

	public String getJobProcessingState() {
		return jobProcessingState;
	}

	public void setJobProcessingState(String jobProcessingState) {
		this.jobProcessingState = jobProcessingState;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public int getJobExperience() {
		return jobExperience;
	}

	public void setJobExperience(int jobExperience) {
		this.jobExperience = jobExperience;
	}

	public String getLinkToExternalSite() {
		return linkToExternalSite;
	}

	public void setLinkToExternalSite(String linkToExternalSite) {
		this.linkToExternalSite = linkToExternalSite;
	}

	public JobDataModel() {

	}

	// This is the map for containing the SkillData and their corresponding
	// 'weight'
	// KEY -> 'Skill Name' , VALUE -> skillWeight
	// TODO- Need to change , if we are planning to include the
	// Skill Weitage on the future.
	// private Map<String,String> skillMap;
	// Currently we are going to take the Skills in the 'text String'
	// And would take split Skills according to the delimeter form the Skills.
	private String skills;

	public String getJobCreatedDate() {
		return jobCreatedDate;
	}

	public void setJobCreatedDate(String jobCreatedDate) {
		this.jobCreatedDate = jobCreatedDate;
	}

	public String getSkills() {
		return skills;
	}

	@JsonIgnore
	public List<String> getSkillsAsList() {
		return (skills == null) ? new ArrayList<String>() : Arrays
				.asList(skills.split(JobPortalConstants.SKILLS_DELEMITER));
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getJobDescriptionFileLocation() {
		return jobDescriptionFileLocation;
	}

	public void setJobDescriptionFileLocation(String jobDescriptionFileLocation) {
		this.jobDescriptionFileLocation = jobDescriptionFileLocation;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobExpiaryDate() {
		return jobExpiaryDate;
	}

	public void setJobExpiaryDate(String jobExpiaryDate) {
		this.jobExpiaryDate = jobExpiaryDate;
	}

	public String getJobModifiedDate() {
		return jobModifiedDate;
	}

	public void setJobModifiedDate(String jobModifiedDate) {
		this.jobModifiedDate = jobModifiedDate;
	}

	public JobDataModel(String jobId, String jdName, String jobDescription,
			boolean keepSalaryHidden, boolean topTierOnly, String maxSalary,
			String minSalary, String salaryCurrencyCode, String title,
			String location, String organisationName, String industryName,
			String recruiterId, String jobDescriptionFileLocation,
			String jobStatus, String jobExpiaryDate, String jobModifiedDate,
			String skills) {
		super();
		this.jobId = jobId;
		this.jdName = jdName;
		this.jobDescription = jobDescription;
		this.keepSalaryHidden = keepSalaryHidden;
		this.topTierOnly = topTierOnly;
		this.maxSalary = maxSalary;
		this.minSalary = minSalary;
		this.salaryCurrencyCode = salaryCurrencyCode;
		this.title = title;
		this.location = location;
		this.organisationName = organisationName;
		this.industryName = industryName;
		this.recruiterId = recruiterId;
		this.jobDescriptionFileLocation = jobDescriptionFileLocation;
		this.jobStatus = jobStatus;
		this.jobExpiaryDate = jobExpiaryDate;
		this.jobModifiedDate = jobModifiedDate;
		this.skills = skills;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJdName() {
		return jdName;
	}

	public void setJdName(String jdName) {
		this.jdName = jdName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public boolean isKeepSalaryHidden() {
		return keepSalaryHidden;
	}

	public void setKeepSalaryHidden(boolean keepSalaryHidden) {
		this.keepSalaryHidden = keepSalaryHidden;
	}

	public boolean isTopTierOnly() {
		return topTierOnly;
	}

	public void setTopTierOnly(boolean topTierOnly) {
		this.topTierOnly = topTierOnly;
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

	public void setLocation(String locationId) {
		this.location = locationId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(String recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getApplicantSettingTypes() {
		return applicantSettingTypes;
	}

	public void setApplicantSettingTypes(String applicantSettingTypes) {
		this.applicantSettingTypes = applicantSettingTypes;
	}

	public Integer getOrganisationTier() {
		return organisationTier;
	}

	public void setOrganisationTier(Integer organisationTier) {
		this.organisationTier = organisationTier;
	}

	@JsonIgnore
	public boolean isJobProcessed() {
		if (StringUtils.isEmpty(jobProcessingState))
			return false;
		if (ProcessingState.valueOf(jobProcessingState) == ProcessingState.PROCESSED)
			return true;
		return false;
	}

	@JsonIgnore
	public boolean isJobPartiallyProcessed() {
		if (StringUtils.isEmpty(jobProcessingState))
			return false;
		if (ProcessingState.valueOf(jobProcessingState) == ProcessingState.PARTIALLYPROCESSED)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "JobDataModel [jobId=" + jobId + ", jdName=" + jdName
				+ ", jobDescription=" + jobDescription + ", keepSalaryHidden="
				+ keepSalaryHidden + ", topTierOnly=" + topTierOnly
				+ ", maxSalary=" + maxSalary + ", minSalary=" + minSalary
				+ ", salaryCurrencyCode=" + salaryCurrencyCode + ", title="
				+ title + ", location=" + location + ", organisationName="
				+ organisationName + ", industryName=" + industryName
				+ ", recruiterId=" + recruiterId
				+ ", jobDescriptionFileLocation=" + jobDescriptionFileLocation
				+ ", jobStatus=" + jobStatus + ", jobExpiaryDate="
				+ jobExpiaryDate + ", jobModifiedDate=" + jobModifiedDate
				+ ", jobCreatedDate=" + jobCreatedDate + ", employmentType="
				+ employmentType + ", jobExperience=" + jobExperience
				+ ", applicantSettingTypes=" + applicantSettingTypes
				+ ", linkToExternalSite=" + linkToExternalSite
				+ ", jobFunction=" + jobFunction + ", jobProcessingState="
				+ jobProcessingState + ", lastProcessedTime="
				+ lastProcessedTime + ", skills=" + skills + "]";
	}

}
