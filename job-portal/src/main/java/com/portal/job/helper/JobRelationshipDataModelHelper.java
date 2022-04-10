package com.portal.job.helper;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.model.JobRelationshipDataModel;

/**
 * @author behinddwalls
 *
 */
@Component
public class JobRelationshipDataModelHelper {

	public void setJobApplicationStatus(final Long jobId,
			final Long jobseekerId, final Long recruiterId,
			final JobApplicationStatus jobApplicationStatus,
			final JobApplicationStatus jobseekerApplicationStatus,
			final JobApplicationStatus recruiterApplicationStatus,
			final boolean isInRecruiterWishlist,
			final boolean isInJobseekerWishlist,
			final JobRelationshipDataModel jobRelationshipDataModel) {

		Preconditions.checkNotNull(jobRelationshipDataModel,
				"job relation data model object can not be empty");

		jobRelationshipDataModel.setJobId(jobId);
		jobRelationshipDataModel.setJobseekerId(jobseekerId);
		jobRelationshipDataModel.setRecruiterId(recruiterId);

		// set status
		jobRelationshipDataModel.setJobApplicationStatus(jobApplicationStatus);
		
		jobRelationshipDataModel
				.setRecruiterApplicationStatus(recruiterApplicationStatus);
		jobRelationshipDataModel
				.setJobSeekerApplicationStatus(jobseekerApplicationStatus);

		jobRelationshipDataModel.setInJobseekerWishlist(isInJobseekerWishlist);
		jobRelationshipDataModel.setInRecruiterWishlist(isInRecruiterWishlist);
	}

}
