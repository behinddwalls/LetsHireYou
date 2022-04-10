package com.portal.job.model;

import java.util.Date;

import com.portal.job.enums.JobApplicationStatus;

public class JobRelationshipDataModel {

    // Member field
    private Long jobRelationshipId;
    private Long jobId;
    private Long expertId;
    private Long jobseekerId;
    private Long recruiterId;
    private JobApplicationStatus jobApplicationStatus;
    private JobApplicationStatus recruiterApplicationStatus;
    private JobApplicationStatus jobSeekerApplicationStatus;
    private JobApplicationStatus expertApplicationStatus;
    private Date rejectedDate;
    private String expertAssessment;
    private boolean isInJobseekerWishlist;
    private boolean isInRecruiterWishlist;
    private Date createDate;
    private Date modifiedDate;

    public JobRelationshipDataModel() {
        super();
    }

    public JobRelationshipDataModel(Long jobRelationshipId, Long jobId, Long expertId, Long jobseekerId,
            Long recruiterId, JobApplicationStatus jobApplicationStatus,
            JobApplicationStatus recruiterApplicationStatus, JobApplicationStatus jobSeekerApplicationStatus,
            JobApplicationStatus expertApplicationStatus, Date rejectedDate, String expertAssessment,
            boolean isInJobseekerWishlist, boolean isInRecruiterWishlist, Date createDate, Date modifiedDate) {
        super();
        this.jobRelationshipId = jobRelationshipId;
        this.jobId = jobId;
        this.expertId = expertId;
        this.jobseekerId = jobseekerId;
        this.recruiterId = recruiterId;
        this.jobApplicationStatus = jobApplicationStatus;
        this.recruiterApplicationStatus = recruiterApplicationStatus;
        this.jobSeekerApplicationStatus = jobSeekerApplicationStatus;
        this.expertApplicationStatus = expertApplicationStatus;
        this.rejectedDate = rejectedDate;
        this.expertAssessment = expertAssessment;
        this.isInJobseekerWishlist = isInJobseekerWishlist;
        this.isInRecruiterWishlist = isInRecruiterWishlist;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getJobRelationshipId() {
        return jobRelationshipId;
    }

    public void setJobRelationshipId(Long jobRelationshipId) {
        this.jobRelationshipId = jobRelationshipId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getExpertId() {
        return expertId;
    }

    public void setExpertId(Long expertId) {
        this.expertId = expertId;
    }

    public Long getJobseekerId() {
        return jobseekerId;
    }

    public void setJobseekerId(Long jobseekerId) {
        this.jobseekerId = jobseekerId;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public JobApplicationStatus getJobApplicationStatus() {
        return jobApplicationStatus;
    }

    public void setJobApplicationStatus(JobApplicationStatus jobApplicationStatus) {
        this.jobApplicationStatus = jobApplicationStatus;
    }

    public JobApplicationStatus getRecruiterApplicationStatus() {
        return recruiterApplicationStatus;
    }

    public void setRecruiterApplicationStatus(JobApplicationStatus recruiterApplicationStatus) {
        this.recruiterApplicationStatus = recruiterApplicationStatus;
    }

    public JobApplicationStatus getJobSeekerApplicationStatus() {
        return jobSeekerApplicationStatus;
    }

    public void setJobSeekerApplicationStatus(JobApplicationStatus jobSeekerApplicationStatus) {
        this.jobSeekerApplicationStatus = jobSeekerApplicationStatus;
    }

    public JobApplicationStatus getExpertApplicationStatus() {
        return expertApplicationStatus;
    }

    public void setExpertApplicationStatus(JobApplicationStatus expertApplicationStatus) {
        this.expertApplicationStatus = expertApplicationStatus;
    }

    public String getExpertAssessment() {
        return expertAssessment;
    }

    public void setExpertAssessment(String expertAssessment) {
        this.expertAssessment = expertAssessment;
    }

    public boolean isInJobseekerWishlist() {
        return isInJobseekerWishlist;
    }

    public void setInJobseekerWishlist(boolean isInJobseekerWishlist) {
        this.isInJobseekerWishlist = isInJobseekerWishlist;
    }

    public boolean isInRecruiterWishlist() {
        return isInRecruiterWishlist;
    }

    public void setInRecruiterWishlist(boolean isInRecruiterWishlist) {
        this.isInRecruiterWishlist = isInRecruiterWishlist;
    }

    @Override
    public String toString() {
        return "JobRelationshipDataModel [jobRelationshipId=" + jobRelationshipId + ", jobId=" + jobId + ", expertId="
                + expertId + ", jobseekerId=" + jobseekerId + ", recruiterId=" + recruiterId
                + ", jobApplicationStatus=" + jobApplicationStatus + ", recruiterApplicationStatus="
                + recruiterApplicationStatus + ", jobSeekerApplicationStatus=" + jobSeekerApplicationStatus
                + ", expertApplicationStatus=" + expertApplicationStatus + ", rejectedDate=" + rejectedDate
                + ", expertAssessment=" + expertAssessment + ", isInJobseekerWishlist=" + isInJobseekerWishlist
                + ", isInRecruiterWishlist=" + isInRecruiterWishlist + "]";
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
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

}
