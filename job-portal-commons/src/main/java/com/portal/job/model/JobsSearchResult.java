package com.portal.job.model;

import java.util.List;

public class JobsSearchResult {
	private int count;
	private int actualCount;
	private List<JobsForCandidateResult> jobsForUser;
	private int actualCurrentPageNumber;
	private int finalPageNumber;

	public JobsSearchResult(int count, int actualCount,
			List<JobsForCandidateResult> jobsForUser,
			int actualCurrentPageNumber, int finalPageNumber) {
		super();
		this.count = count;
		this.actualCount = actualCount;
		this.jobsForUser = jobsForUser;
		this.actualCurrentPageNumber = actualCurrentPageNumber;
		this.finalPageNumber = finalPageNumber;
	}

	public int getFinalPageNumber() {
		return finalPageNumber;
	}

	public int getActualCount() {
		return actualCount;
	}

	public int getActualCurrentPageNumber() {
		return actualCurrentPageNumber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<JobsForCandidateResult> getJobsForUser() {
		return jobsForUser;
	}

	public void setJobsForUser(List<JobsForCandidateResult> jobsForUser) {
		this.jobsForUser = jobsForUser;
	}

}
