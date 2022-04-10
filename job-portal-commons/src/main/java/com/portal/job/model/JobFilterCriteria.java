package com.portal.job.model;

/**
 * 
 * @author pandeysp
 *
 */
public class JobFilterCriteria {

	private String jobTitle;
	private String jobStatus;
	private String jobSortCriteria;
	private Pagination pagination;
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobSortCriteria() {
		return jobSortCriteria;
	}
	public void setJobSortCriteria(String jobSortCriteria) {
		this.jobSortCriteria = jobSortCriteria;
	}
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	@Override
	public String toString() {
		return "JobFilterCriteria [jobTitle=" + jobTitle + ", jobStatus="
				+ jobStatus + ", jobSortCriteria=" + jobSortCriteria
				+ ", pagination=" + pagination + "]";
	}

}
