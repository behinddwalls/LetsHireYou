package com.portal.job.dao.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pulledJobData")
public class PulledJobDataModel {

    @Id
    private String _id;
    private String jobSourcePortal;
    private String jobSourcePortalId;

    private String jobDescription;
    private String jobSalary;
    private String jobExperience;
    private String jobTitle;
    private String location;
    private String jobComapnyName;
    private String jobIndustryName;
    private String jobFunction;

    private String jobModifiedDate;
    private String jobCreatedDate;
    
    private String jobSkills;
    private String jobOtherSkills;
    private String jobRoleCategory;
    private String jobLinkToJobSourcePortal;
    
    private boolean parsedToSQL;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getJobSourcePortal() {
        return jobSourcePortal;
    }

    public void setJobSourcePortal(String jobSourcePortal) {
        this.jobSourcePortal = jobSourcePortal;
    }

    public String getJobSourcePortalId() {
        return jobSourcePortalId;
    }

    public void setJobSourcePortalId(String jobSourcePortalId) {
        this.jobSourcePortalId = jobSourcePortalId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobComapnyName() {
        return jobComapnyName;
    }

    public void setJobComapnyName(String jobComapnyName) {
        this.jobComapnyName = jobComapnyName;
    }

    public String getJobIndustryName() {
        return jobIndustryName;
    }

    public void setJobIndustryName(String jobIndustryName) {
        this.jobIndustryName = jobIndustryName;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public String getJobModifiedDate() {
        return jobModifiedDate;
    }

    public void setJobModifiedDate(String jobModifiedDate) {
        this.jobModifiedDate = jobModifiedDate;
    }

    public String getJobCreatedDate() {
        return jobCreatedDate;
    }

    public void setJobCreatedDate(String jobCreatedDate) {
        this.jobCreatedDate = jobCreatedDate;
    }

    public String getJobLinkToJobSourcePortal() {
        return jobLinkToJobSourcePortal;
    }

    public void setJobLinkToJobSourcePortal(String jobLinkToJobSourcePortal) {
        this.jobLinkToJobSourcePortal = jobLinkToJobSourcePortal;
    }
    
    public String getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(String jobSkills) {
        this.jobSkills = jobSkills;
    }
    
    public String getJobOtherSkills() {
        return jobOtherSkills;
    }

    public void setJobOtherSkills(String jobOtherSkills) {
        this.jobOtherSkills = jobOtherSkills;
    }

	public boolean getParsedToSQL() {
		return parsedToSQL;
	}

	public void setParsedToSQL(boolean parsedToSQL) {
		this.parsedToSQL = parsedToSQL;
	}
	
	public String getJobRoleCategory() {
		return this.jobRoleCategory;
	}

	public void setJobRoleCategory(String jobRoleCategory) {
		this.jobRoleCategory = jobRoleCategory;
	}
}
