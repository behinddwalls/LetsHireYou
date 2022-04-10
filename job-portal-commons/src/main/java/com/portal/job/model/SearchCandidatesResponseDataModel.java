package com.portal.job.model;

import java.util.List;

/**
 * @author preetam
 *
 */
public class SearchCandidatesResponseDataModel {

	private List<SearchCandidatesResult> searchCandidateResults;
	private Pagination pagination;

	public SearchCandidatesResponseDataModel(
			List<SearchCandidatesResult> searchCandidateResults,
			Pagination pagination) {
		super();
		this.searchCandidateResults = searchCandidateResults;
		this.pagination = pagination;
	}

	public List<SearchCandidatesResult> getSearchCandidateResults() {
		return searchCandidateResults;
	}

	public void setSearchCandidateResults(
			List<SearchCandidatesResult> searchCandidateResults) {
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
		return "SearchCandidatesResponseDataModel [searchCandidateResults="
				+ searchCandidateResults + ", pagination=" + pagination + "]";
	}
}
