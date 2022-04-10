package com.portal.job.model;

/**
 * 
 * 
 * Currently we are just taking the pagination. In future we can put the
 * parameter for which we want to do the filtering.
 */
public class SearchJobForCandidatesCriteria {

	private Pagination pagination;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
