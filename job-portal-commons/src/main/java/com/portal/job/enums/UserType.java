package com.portal.job.enums;

public enum UserType {

	JOBSEEKER("jobseeker"), RECRUITER("recruiter"), EXPERT("expert"),RESUMEONLY("jobseekerResume");

	private final String userTypeName;

	private UserType(final String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

}
