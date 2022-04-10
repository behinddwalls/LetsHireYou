package com.portal.job.model;

import java.util.Map;


public class ComputedUsersByJob {

	private Map<Long, String> userIdToPercentageMap;
	private Long jobId;

	public Map<Long, String> getUserIdToPercentageMap() {
		return userIdToPercentageMap;
	}

	public void setUserIdToPercentageMap(Map<Long, String> userIdToPercentageMap) {
		this.userIdToPercentageMap = userIdToPercentageMap;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "ComputedUsersByJob [userIdToPercentageMap="
				+ userIdToPercentageMap + ", jobId=" + jobId + "]";
	}

}
