package com.portal.job.model;

import java.util.List;

public class MyCandidatesResponseDataModel {

	private List<JobRelationshipAndUserDetailWrapper> searchCandidateResults;
	private Pagination pagination;

	public List<JobRelationshipAndUserDetailWrapper> getSearchCandidateResults() {
		return searchCandidateResults;
	}

	public void setSearchCandidateResults(
			List<JobRelationshipAndUserDetailWrapper> searchCandidateResults) {
		this.searchCandidateResults = searchCandidateResults;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	@Override
	public String toString() {
		return "ShortlistedCandidatesResponseDataModel [searchCandidateResults="
				+ searchCandidateResults + ", pagination=" + pagination + "]";
	}

}
