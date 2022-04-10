package com.portal.job.model.engine;

import java.util.List;

/**
 * 
 * @author pandeysp
 *
 *         This Data model is specific for Engine related Data only. It carries
 *         the Job info required by 'Engine' for processing the 'posted Job'
 *         against the 'jobseekers' present in the data store.
 */
public class EngineJobData {

    private String jobId;
    private String jobTitle;
    private String jobDescription;
    private String organisationName;
    private List<String> jobSkills;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<String> getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(List<String> jobSkills) {
        this.jobSkills = jobSkills;
    }


	@Override
    public String toString() {
        return "EngineJobData [jobId=" + jobId + ", jobTitle=" + jobTitle + ", jobDescription=" + jobDescription
                + ", organisationName=" + organisationName + ", jobSkills=" + jobSkills + "]";
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

}
