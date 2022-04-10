package com.portal.job.context;

import com.portal.job.enums.UserType;

/**
 * @author behinddwalls
 *
 */
public class RequestContext {

	private final Long userId;
	private final Long accountId;
	private final boolean isUserAuthorized;
	private final UserType signedInAs;
	private final boolean isRecruiterProfileComplete;
	private final boolean isJobSeekerProfileComplete;
	private final boolean isRecruiterAccountVerified;
	private final boolean isJobseekerAccountVerified;
	private final boolean isExpertAccountVerified;
	private final boolean isWorkEmailVerified;
	private final boolean isEmailVerified;

	/**
	 * Provides a fluent builder to build a request context object.
	 * 
	 * @author preetam
	 * 
	 */
	public static class Builder {
		private Long userId;
		private Long accountId;
		private boolean isUserAuthorized;
		private UserType signedInAs;
		private boolean isRecruiterProfileComplete;
		private boolean isJobSeekerProfileComplete;
		private boolean isRecruiterAccountVerified;
		private boolean isJobseekerAccountVerified;
		private boolean isExpertAccountVerified;
		private boolean isWorkEmailVerified;
		private boolean isEmailVerified;

		public Builder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public Builder accountId(Long accountId) {
			this.accountId = accountId;
			return this;
		}

		public Builder isUserAuthorized(boolean isUserAuthorized) {
			this.isUserAuthorized = isUserAuthorized;
			return this;
		}

		public Builder signedInAs(final UserType signedInAs) {
			this.signedInAs = signedInAs;
			return this;
		}

		public Builder isRecruiterProfileComplete(
				boolean isRecruiterProfileComplete) {
			this.isRecruiterProfileComplete = isRecruiterProfileComplete;
			return this;
		}
		
		public Builder isJobSeekerProfileComplete(
				boolean isJobSeekerProfileComplete) {
			this.isJobSeekerProfileComplete = isJobSeekerProfileComplete;
			return this;
		}

		public Builder isRecruiterAccountVerified(
				boolean isRecruiterAccountVerified) {
			this.isRecruiterAccountVerified = isRecruiterAccountVerified;
			return this;
		}

		public Builder isJobseekerAccountVerified(
				boolean isJobseekerAccountVerified) {
			this.isJobseekerAccountVerified = isJobseekerAccountVerified;
			return this;
		}

		public Builder isExpertAccountVerified(boolean isExpertAccountVerified) {
			this.isExpertAccountVerified = isExpertAccountVerified;
			return this;
		}

		public Builder isWorkEmailVerified(boolean isWorkEmailVerified) {
			this.isWorkEmailVerified = isWorkEmailVerified;
			return this;
		}

		public Builder isEmailVerified(boolean isEmailVerified) {
			this.isEmailVerified = isEmailVerified;
			return this;
		}

		public RequestContext build() {
			return new RequestContext(this);
		}
	}

	private RequestContext(Builder builder) {
		this.userId = builder.userId;
		this.accountId = builder.accountId;
		this.isUserAuthorized = builder.isUserAuthorized;
		this.signedInAs = builder.signedInAs;
		this.isRecruiterProfileComplete = builder.isRecruiterProfileComplete;
		this.isJobSeekerProfileComplete = builder.isJobSeekerProfileComplete;
		this.isRecruiterAccountVerified = builder.isRecruiterAccountVerified;
		this.isJobseekerAccountVerified = builder.isJobseekerAccountVerified;
		this.isWorkEmailVerified = builder.isWorkEmailVerified;
		this.isEmailVerified = builder.isEmailVerified;
		this.isExpertAccountVerified = builder.isExpertAccountVerified;

	}

	public boolean isUserAuthorized() {
		return isUserAuthorized;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public boolean isJobseeker() {
		return (UserType.JOBSEEKER).equals(signedInAs);
	}

	public boolean isRecruiter() {
		return (UserType.RECRUITER).equals(signedInAs);
	}

	public boolean isExpert() {
		return (UserType.EXPERT).equals(signedInAs);
	}

	public UserType signedInAs() {
		return signedInAs;
	}

	public boolean isRecruiterProfileComplete() {
		return isRecruiterProfileComplete;
	}
	
	public boolean isJobSeekerProfileComplete() {
		return isJobSeekerProfileComplete;
	}

	public boolean isRecruiterAccountVerified() {
		return isRecruiterAccountVerified;
	}

	public boolean isJobseekerAccountVerified() {
		return isJobseekerAccountVerified;
	}

	public boolean isExpertAccountVerified() {
		return isExpertAccountVerified;
	}

	public boolean isWorkEmailVerified() {
		return isWorkEmailVerified;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	@Override
	public String toString() {
		return "RequestContext [userId=" + userId + ", accountId=" + accountId
				+ ", isUserAuthorized=" + isUserAuthorized + ", signedInAs="
				+ signedInAs + ", isRecruiterProfileComplete="
				+ isRecruiterProfileComplete + ", isJobSeekerProfileComplete="
				+ isJobSeekerProfileComplete + ", isRecruiterAccountVerified="
				+ isRecruiterAccountVerified + ", isJobseekerAccountVerified="
				+ isJobseekerAccountVerified + ", isExpertAccountVerified="
				+ isExpertAccountVerified + ", isWorkEmailVerified="
				+ isWorkEmailVerified + ", isEmailVerified=" + isEmailVerified
				+ "]";
	}
}
