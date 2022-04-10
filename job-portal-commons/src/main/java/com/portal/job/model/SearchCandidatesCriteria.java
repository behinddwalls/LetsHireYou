package com.portal.job.model;

public class SearchCandidatesCriteria {

	private Long jobId;
	private Pagination pagination;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "SearchCandidatesDataModel [jobId=" + jobId + ", pagination="
				+ pagination + "]";
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
}
