package com.portal.job.controller.recruiter;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.portal.job.constants.ResponseConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.helper.JobRelationshipDataModelHelper;
import com.portal.job.mapper.JobRelationshipDetailMapper;
import com.portal.job.model.JobRelationshipAndUserDetailWrapper;
import com.portal.job.model.JobRelationshipDataModel;
import com.portal.job.service.JobRelationshipDetailService;
import com.portal.job.service.UserDetailService;

@Controller
@RequestMapping("/recruiter/jobapplicationstatus")
public class RecruiterJobRelationshipDetailController extends
		JobPortalBaseController {

	private static final Logger log = LoggerFactory
			.getLogger(RecruiterJobRelationshipDetailController.class);

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
		if (getUserId().equals(jobseekerId)) {
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
	 * @throws Exception
	 */
	private JobRelationshipDataModel updateJobApplicationStatus(
			final Long jobId, final Long jobseekerId,
			final JobApplicationStatus jobApplicationStatus,
			final boolean isInRecruiterWishlist) throws Exception {

		JobApplicationStatus jobApplicationStatusfinal = null;
		JobApplicationStatus recruiterApplicationStatusfinal = null;
		boolean isInRecruiterWishlistFinal = false;
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
		/*
		 * We are allowing To wishlist only those candidates who are not
		 * shortlisted for a JOB. If a candidate is already shorlisted for a JOB
		 * , we wouldn't allow him to to put same candidate in the 'wishlist'
		 * also.
		 * 
		 * 'MAYBEWISHLISTED' aaplication status identifes that from the 'use case specific API call'
		 * we are getting this call from either 'wislist()' or 'unwishlist ()' API.
		 */
		if (JobApplicationStatus.SHORTLISTED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus())
				&& JobApplicationStatus.MAYBEWISHLISTED
						.equals(jobApplicationStatus)) {
			throw new IllegalArgumentException("Operation failed.");

		}
		/*
		 * We could throw a constraint exception for cases where we want to
		 * reject the already rejected Candidates.
		 * 
		 * Since we are using the same API 'updateJobApplicationStatus ()' for
		 * all use cases so to decide what value should we take for the
		 * 'isWislisted' candidate flag before updating the 'DB data'. For this
		 * we pass the 'Application Status' from the 'use case Relevant API' and
		 * according to that we decide whether to fetch the
		 * 'isInRecruiterWishlistFinal' passed from the API or need to fetch the
		 * value form the 'DB'.
		 */
		else if (JobApplicationStatus.REJECTED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus())) {
			if (JobApplicationStatus.REJECTED.equals(jobApplicationStatus)) {
				throw new IllegalArgumentException(
						"Can not be rejected as candidate was rejected for this job.");
			}
			jobApplicationStatusfinal = jobRelationshipDataModelResult
					.getJobApplicationStatus();
			recruiterApplicationStatusfinal = jobRelationshipDataModelResult
					.getRecruiterApplicationStatus();
			if (JobApplicationStatus.MAYBEWISHLISTED
					.equals(jobApplicationStatus)) {
				isInRecruiterWishlistFinal = isInRecruiterWishlist;
			} else {
				isInRecruiterWishlistFinal = jobRelationshipDataModelResult
						.isInRecruiterWishlist();
			}
		} else if ((null == jobRelationshipDataModelResult
				.getJobApplicationStatus() || JobApplicationStatus.MAYBEWISHLISTED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus())||JobApplicationStatus.APPLIED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus()))
				&& JobApplicationStatus.MAYBEWISHLISTED
						.equals(jobApplicationStatus)) {
			jobApplicationStatusfinal = JobApplicationStatus.MAYBEWISHLISTED;
			recruiterApplicationStatusfinal = JobApplicationStatus.MAYBEWISHLISTED;
			isInRecruiterWishlistFinal = isInRecruiterWishlist;
		} else {
			/*
			 * We come to this block when we are sure that we want to set the 'Application
			 * Status' and trivial forward application status assignment.
			 * TODO- Need to verify this logic as it looks like, if a candidate is already 
			 * 'wishlisted' we are putting him in 'Shortlisted' then candidate would still 
			 * be in the 'wishlist'.(That shouldn't be the case.)
			 */
			jobApplicationStatusfinal = jobApplicationStatus;
			recruiterApplicationStatusfinal = jobApplicationStatus;
		}

		jobRelationshipDataModelHelper.setJobApplicationStatus(jobId,
				jobseekerId, getUserId(), jobApplicationStatusfinal,
				jobRelationshipDataModelResult.getJobSeekerApplicationStatus(),
				recruiterApplicationStatusfinal, isInRecruiterWishlistFinal,
				jobRelationshipDataModelResult.isInJobseekerWishlist(),
				jobRelationshipDataModelResult);

		jobRelationshipDetailService
				.addOrUpdate(jobRelationshipDataModelResult);

		return jobRelationshipDataModelResult;
	}

	/**
	 * Set "shortlist" status for a candidate.
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @return
	 */
	@RequestMapping("shortlist")
	public @ResponseBody Map<String, Object> shortlistJobApplicationStatus(
			@RequestParam("jobId") final Long jobId,
			@RequestParam("jobseekerId") final Long jobseekerId) {
		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final Map<String, Object> response = Maps.newHashMap();
		try {
			// validate
			jobRelationshipDataModelResult = updateJobApplicationStatus(jobId,
					jobseekerId, JobApplicationStatus.SHORTLISTED, false);

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

	/**
	 * Set "reject" status for candidate.
	 * 
	 * @param jobId
	 * @param jobseekerId
	 * @return
	 */
	@RequestMapping("reject")
	public @ResponseBody Map<String, Object> rejectJobApplicationStatus(
			@RequestParam("jobId") final Long jobId,
			@RequestParam("jobseekerId") final Long jobseekerId) {

		JobRelationshipDataModel jobRelationshipDataModelResult = null;
		final Map<String, Object> response = Maps.newHashMap();
		try {
			jobRelationshipDataModelResult = updateJobApplicationStatus(jobId,
					jobseekerId, JobApplicationStatus.REJECTED, false);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);
			response.put(ResponseConstants.SUCCESS_MESSAGE,
					"Candidate rejected successfully.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Reject operation failed.");
		}
		return response;
	}

	/**
	 * Add to candidate to "wishlist".
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
					jobseekerId, JobApplicationStatus.MAYBEWISHLISTED, true);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);
			response.put(ResponseConstants.SUCCESS_MESSAGE,
					"Candidate added to wishlist successfully.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Only rejected candidates can be wishlisted.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put(ResponseConstants.ERROR_MESSAGE,
					"Add to wishlist operation failed.");
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
					jobseekerId, JobApplicationStatus.MAYBEWISHLISTED, false);

			response.put("jobRelationshipDataModel",
					jobRelationshipDataModelResult);
			response.put(ResponseConstants.SUCCESS_MESSAGE,
					"Candidate removed from wishlist successfully.");
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

	@RequestMapping("save")
	public ModelAndView saveJobRelationshipDetail(
			@ModelAttribute("jobRelationshipDataModel") @Validated JobRelationshipDataModel jobRelationshipDataModel,
			Model model) throws Exception {

		jobRelationshipDataModel.setRecruiterId(getUserId());
		this.jobRelationshipDetailService.addOrUpdate(jobRelationshipDataModel);
		return new ModelAndView("save", "command", model);
	}

	@RequestMapping("view")
	public ModelAndView getJobRelationModelForJobId(
			HttpSession httpSession,
			@ModelAttribute("jobRelationshipDataModel") @Validated JobRelationshipDataModel jobRelationshipDataModel,
			Model model) throws ParseException {
		Long jobId = jobRelationshipDataModel.getJobId();
		Set<JobRelationshipDataModel> queriedRelationshipDataModel = this.jobRelationshipDetailService
				.getDataModelSetForJobId(jobId);

		model.addAttribute("queriedRelationshipDataModel",
				queriedRelationshipDataModel);
		return new ModelAndView("view", "command", model);
	}

	/*
	 * @RequestMapping(value = { "addtowishlist/{jobRelationshipId}" }, method =
	 * RequestMethod.POST) public @ResponseBody Map<String, String>
	 * addJobSeekerToWhishlist( HttpSession httpSession,
	 * 
	 * @PathVariable("jobRelationshipId") final Long jobRelationshipId, Model
	 * model) throws ParseException { final Map<String, String> response =
	 * Maps.newHashMap(); try { this.jobRelationshipDetailService
	 * .addJobSeekerToWishlist(jobRelationshipId); response.put("status",
	 * "success"); } catch (Exception e) { log.error("failed to get job" + e);
	 * // put an empty set. response.put("status",
	 * "Faliure:Not able to add jobSeeker in wishlist"); } return response; }
	 * 
	 * @RequestMapping(value = {
	 * "updatrrecruiterjobappliactionStatus/{jobRelationshipId}" }, method =
	 * RequestMethod.POST) public @ResponseBody Map<String, String>
	 * updatRrecruiterJobAppliactionStatus( HttpSession httpSession,
	 * 
	 * @PathVariable("jobRelationshipId") final Long jobRelationshipId, Model
	 * model) throws ParseException { final Map<String, String> response =
	 * Maps.newHashMap(); try { // TODO- Need to take the Application status
	 * value alos dynamically. // currently passing it from here only.
	 * this.jobRelationshipDetailService.updateRecuiterApplicationStatus(
	 * jobRelationshipId, JobApplicationStatus.REJECTED); response.put("status",
	 * "success"); } catch (Exception e) { log.error("failed to get job" + e);
	 * // put an empty set. response.put("status",
	 * "Faliure:Not able to add jobSeeker in wishlist"); } return response; }
	 */

	// MOCK
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// Saving the list of Skills .
	@RequestMapping("mocksave")
	public @ResponseBody boolean mockSaveJobSeekerDetail()
			throws ParseException {
		return true;
	}

	@RequestMapping("mockupdate")
	public @ResponseBody boolean mockUpdateJobSeekerDetail()
			throws ParseException {
		return true;
	}

	// Getting the list of Skills .
	@RequestMapping("mockgetall")
	public @ResponseBody Set<JobRelationshipDataModel> mockJobRelationModel()
			throws ParseException {
		return this.jobRelationshipDetailService
				.getAllJobRelationshipDataModel();

	}

	// Getting the list of Skills .
	@RequestMapping("mockgetmodelbyjobid")
	public @ResponseBody Set<JobRelationshipDataModel> mockJobRelationModelByJobId()
			throws ParseException {
		String jobId = "19";
		// fetch the list of relevant Skills.
		return this.jobRelationshipDetailService.getDataModelSetForJobId(Long
				.valueOf(jobId));
	}

	@RequestMapping("mockgetmodelByJobidAndApplicationStatus")
	public @ResponseBody Set<JobRelationshipAndUserDetailWrapper> mockJobRelationModelByJobIdAndApplicationStatus()
			throws ParseException {
		String jobId = "19";
		// fetch the list of relevant Skills.
		return this.jobRelationshipDetailService
				.getDataModelSetForJobApplicationStatus(Long.valueOf(jobId),
						JobApplicationStatus.SELECTED);
	}

	// Getting the list of Skills .
	@RequestMapping("mockgetmodelbyuserId")
	public @ResponseBody List<Long> mockJobRelationModelByUserId()
			throws ParseException {
		String userId = "1";
		// fetch the list of relevant Skills.
		return this.jobRelationshipDetailService
				.getIneligibleJobIdsForUsersJobRecommendation(Long
						.valueOf(userId));
	}

	// Getting the list of Skills .
	// @RequestMapping("mockgetmodelbyUserIdAndRecuiterApplicationStatus")
	// public @ResponseBody ShortlistedCandidatesResponseDataModel
	// mockJobRelationModelByUserIdWithRecruiterApplicationStatus()
	// throws ParseException {
	// String userId = "1";
	// // fetch the list of relevant Skills.
	// return this.jobRelationshipDetailService
	// .getDataSetOfJobApplicationStatusForRecruiter(
	// Long.valueOf(userId), JobApplicationStatus.REJECTED,
	// null);
	// }

	// Getting the list of Skills .
	@RequestMapping("mockgetmodelbyExpertrId")
	public @ResponseBody Set<Long> mockJobRelationModelByExpert()
			throws ParseException {
		String expertId = "1";
		// fetch the list of relevant Skills.
		return this.jobRelationshipDetailService
				.getDataModelSetForExpertId(Long.valueOf(expertId));
	}

	// @RequestMapping("mockuserfromwhishlistforrecruiter")
	// public @ResponseBody ShortlistedCandidatesResponseDataModel
	// mockGetUerFromWishlistForRecruiter()
	// throws ParseException {
	// String recruiterId = "1";
	// // fetch the list of relevant Skills.
	// return this.jobRelationshipDetailService
	// .getAllUsersFromRecruiterWishlist(Long.valueOf(recruiterId),
	// null);
	// }

	/*
	 * @RequestMapping("mockuserfromwhishlistforjobId") public @ResponseBody
	 * Set<JobRelationshipDataModel> mockGetUerFromWishlistForJobId() throws
	 * ParseException { String jobId = "2"; // fetch the list of relevant
	 * Skills. return this.jobRelationshipDetailService
	 * .getUsersFromRecruiterWishlistForJob(Long.valueOf(jobId)); }
	 * 
	 * @RequestMapping("mockAddToWhishlist") public @ResponseBody boolean
	 * mockAddToWhishlist() throws ParseException { String jobRelationshipId =
	 * "1"; // fetch the list of relevant Skills. return
	 * this.jobRelationshipDetailService.addJobSeekerToWishlist(Long
	 * .valueOf(jobRelationshipId)); }
	 * 
	 * @RequestMapping("mockChageRecruiterApplivcationStatus") public
	 * 
	 * @ResponseBody boolean mockChageRecruiterApplivcationStatus() throws
	 * ParseException { String jobRelationshipId = "2"; // fetch the list of
	 * relevant Skills. return this.jobRelationshipDetailService
	 * .updateRecuiterApplicationStatus( Long.valueOf(jobRelationshipId),
	 * JobApplicationStatus.REJECTED); }
	 */

}
