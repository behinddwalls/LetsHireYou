package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum JobApplicationStatus {
	/*
	 * ShortListed("Recruiter short listed the candidate"),
	 * Rejected("Recruiter rejected the candidate");
	 */

	APPLIED("APPLIED"), SHORTLISTED("SHORTLISTED"), REJECTED("REJECTED"), SELECTED(
			"SELECTED"), WISHLISTED("WISHLISTED"),MAYBEWISHLISTED("MAYBEWISHLISTED");

	private final static Map<String, JobApplicationStatus> applicationStatusMap = new HashMap<String, JobApplicationStatus>();
	// initialize it statically.
	static {

		for (JobApplicationStatus statueEnum : JobApplicationStatus.values()) {
			applicationStatusMap.put(statueEnum.getStatus(), statueEnum);
		}
	}

	//
	private String applicationStatus;

	private JobApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getStatus() {
		return this.applicationStatus;
	}

	public static JobApplicationStatus getRecruiterApplicationStatus(
			final String applicationStatus) {
		if (applicationStatusMap.containsKey(applicationStatus)) {
			return applicationStatusMap.get(applicationStatus);
		}
		return null;
	}
}
