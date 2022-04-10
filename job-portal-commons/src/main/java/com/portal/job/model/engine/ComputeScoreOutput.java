package com.portal.job.model.engine;

import java.util.List;

import com.portal.job.model.ComputedUsersByJob;

/**
 * @author behinddwalls
 *
 */
public final class ComputeScoreOutput {

	private List<ComputedUsersByJob> results;

	public List<ComputedUsersByJob> getResults() {
		return results;
	}

	public void setResults(List<ComputedUsersByJob> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "ComputeScoreOutput [results=" + results + "]";
	}

	public boolean isEmpty() {
		return results == null || results.isEmpty();
	}

}
