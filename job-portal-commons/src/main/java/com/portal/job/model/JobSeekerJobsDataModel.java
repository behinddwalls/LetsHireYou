package com.portal.job.model;

/**
 * 
 * @author pandeysp This holds the required Job related info that we want to
 *         show on 'JobSeeker' pages.
 */
public class JobSeekerJobsDataModel {

    // wrap this info also in this model itself. Evenhough
    // this is not the part of actual 'JOB' info model but
    // still we are using this to identify this info uniquely
    // in the
    private String jobRelationshipId;
    private String jobId;
    private String jobTitle;
    private String jobLocation;
    private String jobOrganisationName;
    private String percentageMatch;
    private String link_to_site;
    private boolean fallback;
    private boolean isJobseekerJobApplicationStatusRejected;
    private boolean isJobseekerJobApplicationStatusApplied;
    private boolean isJobWishlistedByUser;
    
    public String getJobRelationshipId() {
        return jobRelationshipId;
    }

    public void setJobRelationshipId(String jobRelationshipId) {
        this.jobRelationshipId = jobRelationshipId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    
    public String getPercentageMatch() {
        return percentageMatch;
    }
    
    public void setPercentageMatch(String percentageMatch) {
        this.percentageMatch=percentageMatch;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobOrganisationName() {
        return jobOrganisationName;
    }

    public void setJobOrganisationName(String jobOrganisationName) {
        this.jobOrganisationName = jobOrganisationName;
    }

    public boolean isJobseekerJobApplicationStatusRejected() {
        return isJobseekerJobApplicationStatusRejected;
    }

    public void setJobseekerJobApplicationStatusRejected(
            boolean isJobseekerJobApplicationStatusRejected) {
        this.isJobseekerJobApplicationStatusRejected = isJobseekerJobApplicationStatusRejected;
    }

    public boolean isJobseekerJobApplicationStatusApplied() {
        return isJobseekerJobApplicationStatusApplied;
    }

    public void setJobseekerJobApplicationStatusApplied(
            boolean isJobseekerJobApplicationStatusApplied) {
        this.isJobseekerJobApplicationStatusApplied = isJobseekerJobApplicationStatusApplied;
    }

    public boolean isJobWishlistedByUser() {
        return isJobWishlistedByUser;
    }

    public void setJobWishlistedByUser(boolean isJobWishlistedByUser) {
        this.isJobWishlistedByUser = isJobWishlistedByUser;
    }
    
    public String getLink_to_site() {
        return link_to_site;
    }

    public void setLink_to_site(String link_to_site) {
        this.link_to_site = link_to_site;
    }
    
    public boolean getFallback() {
        return this.fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }
    
    @Override
    public String toString() {
        return "JobSeekerJobsDataModel [jobRelationshipId=" + jobRelationshipId + ", jobId="
                + jobId + ", jobTitle=" + jobTitle + ", jobLocation=" + jobLocation
                + ", jobOrganisationName=" + jobOrganisationName
                + ", isJobseekerJobApplicationStatusRejected="
                + isJobseekerJobApplicationStatusRejected
                + ", isJobseekerJobApplicationStatusApplied="
                + isJobseekerJobApplicationStatusApplied + ", isJobWishlistedByUser="
                + isJobWishlistedByUser + "]";
    }

}
