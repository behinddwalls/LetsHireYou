package com.portal.job.model;

import java.util.List;

public class UserSearchResult {
	private int count;
	private int actualCount;
	private List<SearchCandidatesResult> result;
	private int actualCurrentPageNumber;
	private int finalPageNumber;

	public UserSearchResult(int count, int actualCount,
			List<SearchCandidatesResult> result, int actualCurrentPageNumber,
			int finalPageNumber) {
		super();
		this.count = count;
		this.actualCount = actualCount;
		this.result = result;
		this.actualCurrentPageNumber = actualCurrentPageNumber;
		this.finalPageNumber = finalPageNumber;
	}

	public int getActualCount() {
		return actualCount;
	}

	public int getActualCurrentPageNumber() {
		return actualCurrentPageNumber;
	}

	public int getFinalPageNumber() {
		return finalPageNumber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<SearchCandidatesResult> getResult() {
		return result;
	}

	public void setResult(List<SearchCandidatesResult> result) {
		this.result = result;
	}

}
