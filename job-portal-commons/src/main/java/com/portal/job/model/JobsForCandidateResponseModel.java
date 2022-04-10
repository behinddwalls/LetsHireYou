package com.portal.job.model;

import java.util.List;

/**
 * 
 * This is used to hold the Jobs that is having the good matching with the
 * Customer skills. It fetch the data from the same table that is getting
 * populated while we do the matching for the customers for the posted Jobs. We
 * kind of do the look the 'perfect match' field of the table of figuring.
 * 
 */
public class JobsForCandidateResponseModel {

	private List<JobsForCandidateResult> jobsResult;
	private Pagination pagination;

	public JobsForCandidateResponseModel(
			List<JobsForCandidateResult> jobsResult, Pagination pagination) {
		this.jobsResult = jobsResult;
		this.pagination = pagination;
	}

	public JobsForCandidateResponseModel() {

	}

	public List<JobsForCandidateResult> getJobsResult() {
		return jobsResult;
	}

	public void setJobsResult(List<JobsForCandidateResult> jobsResult) {
		this.jobsResult = jobsResult;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
