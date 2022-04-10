package com.portal.job.controller.recruiter;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.enums.SortingCriteria;
import com.portal.job.model.CandidatesFilterCriteria;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobRelationshipAndUserDetailWrapper;
import com.portal.job.model.MyCandidatesResponseDataModel;
import com.portal.job.model.Pagination;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.JobRelationshipDetailService;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/recruiter/candidate")
public class RecruiterSearchWhitelistedCandidatesController extends
		JobPortalBaseController {

	private static Logger log = LoggerFactory
			.getLogger(RecruiterSearchWhitelistedCandidatesController.class);

	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private JobRelationshipDetailService JobRelationshipDetailService;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView viewResumeSearchPage(final Model model) {
		final List<JobDataModel> jobDataModels = jobDetailService
				.getJobsPostedByRecruiterId(getUserId());

		final Map<String, String> jobMap = Maps.newHashMap();
		for (JobDataModel jobDataModel : jobDataModels) {
			jobMap.put(jobDataModel.getJobId(), jobDataModel.getTitle());
		}
		model.addAttribute("jobMap", jobMap);
		return getModelAndView(model, "recruiter/shortlisted-candidate");
	}

	@RequestMapping(value = "/wishlist", method = RequestMethod.GET)
	public ModelAndView viewAllWishlistCandidate(final Model model) {
		final List<JobDataModel> jobDataModels = jobDetailService
				.getJobsPostedByRecruiterId(getUserId());

		final Map<String, String> jobMap = Maps.newHashMap();
		for (JobDataModel jobDataModel : jobDataModels) {
			jobMap.put(jobDataModel.getJobId(), jobDataModel.getTitle());
		}
		model.addAttribute("jobMap", jobMap);
		return getModelAndView(model, "recruiter/wishlist-candidate");
	}

	@RequestMapping(value = { "wishlist/getAll" }, method = RequestMethod.POST)
	public @ResponseBody MyCandidatesResponseDataModel getAllUsersFromRecruiterWishlist(
			CandidatesFilterCriteria searchCriteria, Model model)
			throws ParseException {
		MyCandidatesResponseDataModel responseDataModel = new MyCandidatesResponseDataModel();
		try {
			final int pageSize = 10;
			if (searchCriteria.getPagination() == null) {
				searchCriteria.setPagination(new Pagination());
			}
			searchCriteria.getPagination().setPageSize(pageSize);
			responseDataModel = this.JobRelationshipDetailService
					.getAllUsersFromRecruiterWishlist(getUserId(),
							searchCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("failed to fetch data" + e);
			responseDataModel.setPagination(searchCriteria.getPagination());
			responseDataModel.setSearchCandidateResults(Lists.newArrayList());
		}
		return responseDataModel;
	}

	/**
	 * 
	 * @param httpSession
	 * @param jobId
	 * @param applicationStatus
	 * @param model
	 * @return
	 * @throws ParseException
	 *             Used to fetch the Candidates associated with a Job according
	 *             to their application processing status. This status is
	 *             assigned b the recruiter.
	 */
	@RequestMapping(value = { "shortlisted/getAll" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, MyCandidatesResponseDataModel> getAllShortlistedCandidate(
			CandidatesFilterCriteria searchCriteria, Model model)
			throws ParseException {
		final Map<String, MyCandidatesResponseDataModel> response = Maps
				.newHashMap();
		try {

			// Verify Status 'JobApplicationStatus' coming in the request
			validatInputData(searchCriteria);

			System.out
					.println("### Inside the shortlisted/getAll() to fecth the Candifdates value");

			final int pageSize = 10;
			if (searchCriteria.getPagination() == null) {
				searchCriteria.setPagination(new Pagination());
			}
			searchCriteria.getPagination().setPageSize(pageSize);
			MyCandidatesResponseDataModel queriedDataModel = this.JobRelationshipDetailService
					.getMyCandidatesResponseDataModelForRecruiter(getUserId(),
							searchCriteria);
			response.put("myCandidatesResponseDataModel", queriedDataModel);
		} catch (IllegalArgumentException ex) {
			System.out
					.println("Illegal Argument is coming in Input request from the Recriuter"
							+ ex + " \n" + " InutData:" + searchCriteria);
			log.error("Illegal Argument is coming in Input request from the Recriuter"
					+ ex + " \n" + " InutData:" + searchCriteria);
			// put an empty set.
			MyCandidatesResponseDataModel responseDataModel = new MyCandidatesResponseDataModel();
			responseDataModel.setPagination(searchCriteria.getPagination());
			responseDataModel.setSearchCandidateResults(Lists.newArrayList());
			response.put("myCandidatesResponseDataModel", responseDataModel);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Some Error while trying to Fetch the MyCandidates for the Recruiter"
					+ ex);
			// put an empty set.
			MyCandidatesResponseDataModel responseDataModel = new MyCandidatesResponseDataModel();
			responseDataModel.setPagination(searchCriteria.getPagination());
			responseDataModel.setSearchCandidateResults(Lists.newArrayList());
			response.put("myCandidatesResponseDataModel", responseDataModel);
		}
		return response;
	}

	/**
	 * 
	 * @param httpSession
	 * @param jobId
	 * @param applicationStatus
	 * @param model
	 * @return
	 * @throws ParseException
	 *             Used to fetch the Candidates associated with a Job according
	 *             to their application processing status. This status is
	 *             assigned b the recruiter.
	 */
	@RequestMapping(value = { "shortlisted/{jobId}" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, Set<JobRelationshipAndUserDetailWrapper>> getModelForShortlistedCandidate(
			HttpSession httpSession, @PathVariable("jobId") final Long jobId,
			Model model) throws ParseException {
		final Map<String, Set<JobRelationshipAndUserDetailWrapper>> response = Maps
				.newHashMap();
		try {
			/*
			 * final int pageSize = 10; if (searchCriteria.getPagination() ==
			 * null) { searchCriteria.setPagination(new Pagination()); }
			 */
			//
			Set<JobRelationshipAndUserDetailWrapper> queriedDataModel = this.JobRelationshipDetailService
					.getDataModelSetForJobApplicationStatus(jobId,
							JobApplicationStatus.SHORTLISTED);
			response.put("jobRelationshipDataModelSet", queriedDataModel);

		} catch (Exception e) {
			// put an empty set.
			response.put("status",
					new HashSet<JobRelationshipAndUserDetailWrapper>());
		}
		return response;
	}

	@RequestMapping(value = { "/search/shortlist/{jobId}" }, method = RequestMethod.GET)
	public ModelAndView viewSearchPageByJobId(
			@PathVariable("jobId") final Long jobId, final Model model) {
		try {
			log.info("inside the shortlisted candidate");
			// set the shortlisted candidates also.
			Set<JobRelationshipAndUserDetailWrapper> wrapperSet = this.JobRelationshipDetailService
					.getDataModelSetForJobApplicationStatus(jobId,
							JobApplicationStatus.SHORTLISTED);
			model.addAttribute("jonRelationshipMap", wrapperSet);
			model.addAttribute("candidatesForJobId", "candidatesForJobId");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("jonRelationshipMap", Sets.newHashSet());
			model.addAttribute("jobMap", Maps.newHashMap());
			model.addAttribute("candidatesForJobId", "candidatesForJobId");
		}

		return getModelAndView(model, "recruiter/shortlisted-candidate");
	}

	/*
	 * Validate Input Data coming form the Request..
	 */
	private void validatInputData(
			final CandidatesFilterCriteria candidatesFilterCriteria) {
		JobApplicationStatus jobApplicationStatus = JobApplicationStatus
				.getRecruiterApplicationStatus(candidatesFilterCriteria
						.getCandidateStatus());
		/*
		 * Onstead of throwing exception we will assign 'SHORTLISTED value'
		 */
		if (jobApplicationStatus == null) {
			/*
			 * throw new IllegalArgumentException(
			 * "Supplied Application status is not Valid:" +
			 * candidatesFilterCriteria.getCandidateStatus());
			 */
			candidatesFilterCriteria
					.setCandidateStatus(JobApplicationStatus.SHORTLISTED
							.getStatus());
		}
		// Look for the Sorting criteria.
		SortingCriteria sortingCriteria = SortingCriteria
				.getSortingCriteria(candidatesFilterCriteria
						.getCandidateSortCriteria());
		if (SortingCriteria.OTHER.equals(sortingCriteria)) {
			// If we are not able to find any kind of 'SortingCriteria' then
			// we would put the Default Criteria of 'Newest'.
			candidatesFilterCriteria
					.setCandidateSortCriteria(SortingCriteria.NEWEST_JOBCREATED
							.getName());
		}
	}
}
