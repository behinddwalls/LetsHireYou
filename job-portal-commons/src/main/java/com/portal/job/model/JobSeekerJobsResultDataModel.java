package com.portal.job.model;

import java.util.List;

/**
 * 
 * @author pandeysp
 * This holds the data related to Jobs that Jobseeker has performed some action
 * on. This model is basically used to show the values on 'JobSeeker' pages 
 * only.
 */
public class JobSeekerJobsResultDataModel {

    private List<JobSeekerJobsDataModel> jobSeekerJobsDataModelList;
    private Pagination pagination;
    
    public List<JobSeekerJobsDataModel> getJobSeekerJobsDataModelList() {
        return jobSeekerJobsDataModelList;
    }
    public void setJobSeekerJobsDataModelList(List<JobSeekerJobsDataModel> jobSeekerJobsDataModelList) {
        this.jobSeekerJobsDataModelList = jobSeekerJobsDataModelList;
    }
    public Pagination getPagination() {
        return pagination;
    }
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    
   //Other methods.
    @Override
    public String toString() {
        return "JobSeekerJobsResultDataModel [jobSeekerJobsDataModelList="
                + jobSeekerJobsDataModelList + ", pagination=" + pagination + "]";
    }
   
}
