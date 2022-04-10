package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.portal.job.constants.ResponseConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.helper.JobRelationshipDataModelHelper;
import com.portal.job.mapper.JobRelationshipDetailMapper;
import com.portal.job.model.JobRelationshipDataModel;
import com.portal.job.service.JobRelationshipDetailService;
import com.portal.job.service.UserDetailService;

@Controller
@RequestMapping("/jobseeker/jobapplicationstatus")
public class UserJobRelationshipDetailController extends
		JobPortalBaseController {

	private static final Logger log = LoggerFactory
			.getLogger(UserJobRelationshipDetailController.class);

	@Autowired
	private JobRelationshipDetailMapper jobRelationshipDetailMapper;
	@Autowired
	private JobRelationshipDetailService jobRelationshipDetailService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private JobRelationshipDataModelHelper jobRelationshipDataModelHelper;

	/**
	 * Validate update request for job application status
	 * 
	 * @param jobId
	 * @param jobseekerId
	 */
	private void validateJobApplicationStatusUpdate(final Long jobId,
			final Long jobseekerId) {
		if (jobId == null || jobId.longValue() == 0 || jobseekerId == null
				|| jobseekerId.longValue() == 0) {
			throw new IllegalArgumentException("Invalid request.");
		}
		if (!getUserId().equals(jobseekerId)) {
			throw new IllegalArgumentException(
					"You are not allowed to do this operation.");
		}
	}

	/**
	 * Update DB with the new application status and wishlist
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @param jobApplicationStatus
	 * @param recruiterApplicationStatus
	 * @param isInRecruiterWishlist
	 * @return
	 * @throws ParseException
	 */
	private JobRelationshipDataModel updateJobApplicationStatus(
			final Long jobId, final Long jobseekerId,
			final JobApplicationStatus jobApplicationStatus,
			final boolean isInJobseekerWishlist) throws ParseException {

		JobApplicationStatus jobApplicationStatusfinal = null;
		JobApplicationStatus jobSeekerApplicationStatusfinal = null;
		// validate
		validateJobApplicationStatusUpdate(jobId, jobseekerId);

		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final JobRelationshipDataModel jobRelationshipDataModel = jobRelationshipDetailService
				.getJobRelationDataModel(jobId, jobseekerId);

		if (null == jobRelationshipDataModel) {
			jobRelationshipDataModelResult = new JobRelationshipDataModel();
		} else {
			jobRelationshipDataModelResult = jobRelationshipDataModel;
		}
		// set job application status
		if (null != jobRelationshipDataModelResult.getJobApplicationStatus()
				&& jobRelationshipDataModelResult.getJobApplicationStatus()
						.equals(JobApplicationStatus.REJECTED)) {
			jobApplicationStatusfinal = jobRelationshipDataModelResult
					.getJobApplicationStatus();
			jobSeekerApplicationStatusfinal = jobRelationshipDataModelResult
					.getRecruiterApplicationStatus();

		} else if (null == jobApplicationStatus) {
			if(isInJobseekerWishlist){
				//throw an exception, only rejected candidates can by 
				//be added to the wishlist.
				throw new IllegalArgumentException("Can't perfom add to wishlist operation.");
			}
			jobApplicationStatusfinal = jobRelationshipDataModelResult
					.getJobApplicationStatus();
			jobSeekerApplicationStatusfinal = jobRelationshipDataModelResult
					.getRecruiterApplicationStatus();
		} else {
			jobApplicationStatusfinal = jobApplicationStatus;
			jobSeekerApplicationStatusfinal = jobApplicationStatus;
		}

		jobRelationshipDataModelHelper.setJobApplicationStatus(jobId,
				jobseekerId, getUserId(), jobApplicationStatusfinal,
				jobSeekerApplicationStatusfinal,
				jobRelationshipDataModel.getRecruiterApplicationStatus(),
				jobRelationshipDataModelResult.isInRecruiterWishlist(),
				isInJobseekerWishlist,
				jobRelationshipDataModelResult);

/*		jobRelationshipDetailService
				.addOrUpdate(jobRelationshipDataModelResult);*/

		return jobRelationshipDataModelResult;
	}

	/**
	 * Add Job to candidate (JobSeeker) wishlist..
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @return
	 */
	@RequestMapping("wishlist")
	public @ResponseBody Map<String, Object> wishlistJobApplicationStatus(
			@RequestParam("jobId") final Long jobId,
			@RequestParam("jobseekerId") final Long jobseekerId) {

		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final Map<String, Object> response = Maps.newHashMap();
		try {

			jobRelationshipDataModelResult = updateJobApplicationStatus(jobId,
					jobseekerId, null, true);

			jobRelationshipDetailService
					.addOrUpdate(jobRelationshipDataModelResult);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);
			response.put(ResponseConstants.SUCCESS_MESSAGE,
					"Job is added to wishlist successfully.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Only rejected candidates can be wishlisted.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Add to job wishlist operation failed.");
		}
		return response;
	}

	/**
	 * Remove candidate from the wishlist
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @return
	 */
	@RequestMapping("unwishlist")
	public @ResponseBody Map<String, Object> unwishlistJobApplicationStatus(
			@RequestParam("jobId") final Long jobId,
			@RequestParam("jobseekerId") final Long jobseekerId) {

		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final Map<String, Object> response = Maps.newHashMap();
		try {

			jobRelationshipDataModelResult = updateJobApplicationStatus(jobId,
					jobseekerId, null, false);

			jobRelationshipDetailService
					.addOrUpdate(jobRelationshipDataModelResult);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);
			response.put(ResponseConstants.SUCCESS_MESSAGE,
					"Job is removed foprm the wishlist successfully.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Remove from Wishlist operation failed.");
		}
		return response;
	}
	
	/**
	 * Set "shortlist" status for a candidate.
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @return
	 */
	@RequestMapping("apply")
	public @ResponseBody Map<String, Object> shortlistJobApplicationStatus(
			@RequestParam("jobId") final Long jobId,
			@RequestParam("jobseekerId") final Long jobseekerId) {
		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final Map<String, Object> response = Maps.newHashMap();
		try {
			// validate
			jobRelationshipDataModelResult = updateJobApplicationStatus(jobId,
					jobseekerId, JobApplicationStatus.SHORTLISTED, false);

			jobRelationshipDetailService
					.addOrUpdate(jobRelationshipDataModelResult);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);

			if (jobRelationshipDataModelResult.getJobApplicationStatus()
					.equals(JobApplicationStatus.REJECTED)) {
				response.put(ResponseConstants.ERROR_MESSAGE,
						"Can not be shortlisted as candidate was rejected for this job.");
			} else {
				response.put(ResponseConstants.SUCCESS_MESSAGE,
						"Candidate shortlisted successfully.");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Shortlist operation failed.");
		}
		return response;
	}

}
