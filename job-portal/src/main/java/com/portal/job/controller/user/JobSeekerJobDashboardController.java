package com.portal.job.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.portal.job.constants.ResponseConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.recruiter.RecruiterSearchResumeController;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.helper.JobRelationshipDataModelHelper;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobFilterCriteria;
import com.portal.job.model.JobRelationshipDataModel;
import com.portal.job.model.JobSeekerJobsResultDataModel;
import com.portal.job.model.JobsForCandidateResponseModel;
import com.portal.job.model.JobsForCandidateResult;
import com.portal.job.model.Pagination;
import com.portal.job.model.SearchJobForCandidatesCriteria;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.JobRelationshipDetailService;
import com.portal.job.services.search.JobseekerSearchService;

@Controller
@RequestMapping("/jobseeker/job")
public class JobSeekerJobDashboardController extends JobPortalBaseController {
    private static final Logger log = LoggerFactory.getLogger(RecruiterSearchResumeController.class);
    @Autowired
    private JobDetailService jobDetailService;
    @Autowired
    private JobseekerSearchService jobseekerSearchService;
    @Autowired
    private JobRelationshipDataModelHelper jobRelationshipDataModelHelper;
    @Autowired
    private JobRelationshipDetailService jobRelationshipDetailService;

    /**
     * Validate update request for job application status
     * 
     * @param jobId
     * @param jobseekerId
     */
    private void validateJobApplicationStatusUpdate(final Long jobId, final Long jobseekerId) {
        if (jobId == null || jobId.longValue() == 0) {
            throw new IllegalArgumentException("Invalid request.");
        }
    }

    @RequestMapping(value = { "/{jobId}" }, method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getJobDataModelByJobId(@PathVariable("jobId") final Long jobId, Model model) {
        final Map<String, Object> response = Maps.newHashMap();
        try {
            final JobDataModel jobDataModel = jobDetailService.getJobDataModelByJobId(jobId);
            jobDataModel.setJobDescription(replaceEndOfLineWithBreakLine(jobDataModel.getJobDescription()));
            response.put("jobDataModel", jobDataModel);
            response.put("status", "success");
        } catch (Exception e) {
            log.error("failed to get job" + e);
            response.put("status", "error");
            response.put("errorMessage", "Failed to get job");
        }
        return response;
    }

    @RequestMapping(value = "recommendedjob")
    public ModelAndView viewRecommendedjob(Model model) {
        model.addAttribute("jobDetailsList", new ArrayList<JobDataModel>());
        return getModelAndView(model, "jobseeker/recommendedjob");

    }

    @RequestMapping(value = "viewjob")
    public ModelAndView viewJob(Model model) {
        model.addAttribute("jobDetailsList", new ArrayList<JobDataModel>());
        return getModelAndView(model, "jobseeker/viewjob");

    }

    @RequestMapping(value = { "search" }, method = RequestMethod.POST)
    public @ResponseBody JobsForCandidateResponseModel search(final SearchJobForCandidatesCriteria searchCriteria,
            Model model) {

        JobsForCandidateResponseModel responseModel = new JobsForCandidateResponseModel();
        responseModel.setJobsResult(Lists.newArrayList());
        responseModel.setPagination(searchCriteria.getPagination());
        try {
            System.out.println(searchCriteria);
            final int pageSize = 10;
            if (searchCriteria.getPagination() == null) {
                searchCriteria.setPagination(new Pagination());
            }
            searchCriteria.getPagination().setPageSize(pageSize);

            responseModel = new JobsForCandidateResponseModel(jobseekerSearchService.getPreferredJobsForJobseeker(
                    getUserId(), searchCriteria), searchCriteria.getPagination());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Some problem while fetching the Recommended JOBS for the User");
        }
        return responseModel;

        // return getMockData();
    }

    /**
     * Set "apply" status for a candidate JOB status.
     * 
     * @param jobId
     * @param jobseekerId
     * @return
     */
    @RequestMapping("apply")
    public @ResponseBody Map<String, Object> shortlistJobApplicationStatus(@RequestParam("jobId") final Long jobId) {
        JobRelationshipDataModel jobRelationshipDataModelResult = null;
        final Map<String, Object> response = Maps.newHashMap();
        try {
            // validate
            jobRelationshipDataModelResult = updateJobApplicationStatus(jobId, getUserId(),
                    JobApplicationStatus.APPLIED, false);
            response.put("jobRelationshipDataModel", jobRelationshipDataModelResult);
            response.put(ResponseConstants.SUCCESS_MESSAGE, "Job applied successfully!!!");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, "Some problem in applying for the JOB");
        }
        return response;
    }

    /**
     * 
     * @param jobFilterCriteria
     * @param model
     * @return This Fetches the all entries from 'JobRelationshoipTable'
     *         according to the searching criteria.
     */
    @RequestMapping(value = { "getappliedjobs" }, method = RequestMethod.POST)
    public @ResponseBody JobSeekerJobsResultDataModel getAppliedJobsByJobseeker(
            final JobFilterCriteria jobFilterCriteria, Model model) {
        // log.info("### indie the getappliedjobs ..");
        JobSeekerJobsResultDataModel searchJobsResult = new JobSeekerJobsResultDataModel();
        try {
            final int pageSize = 10;
            if (jobFilterCriteria.getPagination() == null) {
                jobFilterCriteria.setPagination(new Pagination());
            }
            jobFilterCriteria.getPagination().setPageSize(pageSize);
            searchJobsResult = jobRelationshipDetailService.getAllJobsForJobseeker(getUserId(), jobFilterCriteria);
            model.addAttribute("searchJobsResult", searchJobsResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error:" + e);
            searchJobsResult.setJobSeekerJobsDataModelList(Lists.newArrayList());
            searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        }
        return searchJobsResult;
    }

    /**
     * 
     * @param jobFilterCriteria
     * @param model
     * @return
     */
    @RequestMapping(value = { "getrejectedjobs" }, method = RequestMethod.POST)
    public @ResponseBody JobSeekerJobsResultDataModel getRejectedJobsByJobseeker(
            final JobFilterCriteria jobFilterCriteria, Model model) {
        JobSeekerJobsResultDataModel searchJobsResult = new JobSeekerJobsResultDataModel();
        List<JobDataModel> jobDataList;
        try {
            final int pageSize = 10;
            if (jobFilterCriteria.getPagination() == null) {
                jobFilterCriteria.setPagination(new Pagination());
            }
            jobFilterCriteria.getPagination().setPageSize(pageSize);
            searchJobsResult = jobRelationshipDetailService.getAllJobsForJobseeker(getUserId(), jobFilterCriteria);
            model.addAttribute("searchJobsResult", searchJobsResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error:" + e);
            searchJobsResult.setJobSeekerJobsDataModelList(Lists.newArrayList());
            searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        }
        return searchJobsResult;
    }

    /**
     * Filters the Jobs for jobseeker which was put by him to his wishlist.
     * 
     * @param jobFilterCriteria
     * @param model
     * @return
     */
    @RequestMapping(value = { "getwishlistedjobs" }, method = RequestMethod.POST)
    public @ResponseBody JobSeekerJobsResultDataModel getWsishlistedJobsByJobseeker(
            final JobFilterCriteria jobFilterCriteria, Model model) {
        JobSeekerJobsResultDataModel searchJobsResult = new JobSeekerJobsResultDataModel();
        try {
            final int pageSize = 10;
            if (jobFilterCriteria.getPagination() == null) {
                jobFilterCriteria.setPagination(new Pagination());
            }
            jobFilterCriteria.getPagination().setPageSize(pageSize);
            searchJobsResult = jobRelationshipDetailService.getAllJobsForJobseeker(getUserId(), jobFilterCriteria);
            model.addAttribute("searchJobsResult", searchJobsResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error:" + e);
            searchJobsResult.setJobSeekerJobsDataModelList(Lists.newArrayList());
            searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        }
        return searchJobsResult;
    }

    /**
     * Set "reject" status for candidate.
     * 
     * @param jobId
     * @param jobseekerId
     * @return
     */
    @RequestMapping("reject")
    public @ResponseBody Map<String, Object> rejectJobApplicationStatus(@RequestParam("jobId") final Long jobId) {

        JobRelationshipDataModel jobRelationshipDataModelResult = null;
        final Map<String, Object> response = Maps.newHashMap();
        try {
            jobRelationshipDataModelResult = updateJobApplicationStatus(jobId, getUserId(),
                    JobApplicationStatus.REJECTED, false);
            response.put("jobRelationshipDataModel", jobRelationshipDataModelResult);
            response.put(ResponseConstants.SUCCESS_MESSAGE, "Job rejected successfully!!!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, "Some problem while rejecting JOB!!! Please try again.");
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
    public @ResponseBody Map<String, Object> wishlistJobApplicationStatus(@RequestParam("jobId") final Long jobId) {

        JobRelationshipDataModel jobRelationshipDataModelResult = null;
        final Map<String, Object> response = Maps.newHashMap();
        try {
            jobRelationshipDataModelResult = updateJobApplicationStatus(jobId, getUserId(), JobApplicationStatus.MAYBEWISHLISTED, true);
            response.put("jobRelationshipDataModel", jobRelationshipDataModelResult);
            response.put(ResponseConstants.SUCCESS_MESSAGE, "Job added to wishlist successfully.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, "Only rejected candidates can be wishlisted.");
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, "Add to wishlist operation failed.");
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
    public @ResponseBody Map<String, Object> unwishlistJobApplicationStatus(@RequestParam("jobId") final Long jobId) {

        JobRelationshipDataModel jobRelationshipDataModelResult = null;
        final Map<String, Object> response = Maps.newHashMap();
        try {
            jobRelationshipDataModelResult = updateJobApplicationStatus(jobId, getUserId(), JobApplicationStatus.MAYBEWISHLISTED, false);
            jobRelationshipDetailService.addOrUpdate(jobRelationshipDataModelResult);
            response.put("jobRelationshipDataModel", jobRelationshipDataModelResult);
            response.put(ResponseConstants.SUCCESS_MESSAGE, "Job removed from wishlist successfully.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ResponseConstants.ERROR_MESSAGE, "Remove from Wishlist operation failed.");
        }
        return response;
    }

    // private methods.
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
    private JobRelationshipDataModel updateJobApplicationStatus(final Long jobId, final Long jobseekerId,
            final JobApplicationStatus jobApplicationStatus, final boolean isInJoobseekerWishlist) throws Exception {

        JobApplicationStatus jobApplicationStatusfinal = null;
        JobApplicationStatus jobSeekerApplicationStatusfinal = null;
        boolean isInJobSeekerWishlistFinal = false;
        // validate
        validateJobApplicationStatusUpdate(jobId, jobseekerId);

        JobRelationshipDataModel jobRelationshipDataModelResult = null;
        final JobRelationshipDataModel jobRelationshipDataModel = jobRelationshipDetailService.getJobRelationDataModel(
                jobId, jobseekerId);

        if (null == jobRelationshipDataModel) {
            jobRelationshipDataModelResult = new JobRelationshipDataModel();
        } else {
            jobRelationshipDataModelResult = jobRelationshipDataModel;
        }
        // set job application status
        if (JobApplicationStatus.APPLIED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus())
				&& JobApplicationStatus.MAYBEWISHLISTED
						.equals(jobApplicationStatus)) {
			throw new IllegalArgumentException("Operation failed.");

		} else if (JobApplicationStatus.REJECTED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus())) {
			if (JobApplicationStatus.REJECTED.equals(jobApplicationStatus)) {
				throw new IllegalArgumentException(
						"Can not be rejected!!");
			}
			jobApplicationStatusfinal = jobRelationshipDataModelResult
					.getJobApplicationStatus();
			jobSeekerApplicationStatusfinal = jobRelationshipDataModelResult
					.getRecruiterApplicationStatus();
			if (JobApplicationStatus.MAYBEWISHLISTED
					.equals(jobApplicationStatus)) {
				isInJobSeekerWishlistFinal = isInJoobseekerWishlist;
			} else {
				isInJobSeekerWishlistFinal = jobRelationshipDataModelResult
						.isInJobseekerWishlist();
			}
		}else if ((null == jobRelationshipDataModelResult
				.getJobApplicationStatus() || JobApplicationStatus.MAYBEWISHLISTED
				.equals(jobRelationshipDataModelResult
						.getJobApplicationStatus()))
				&& JobApplicationStatus.MAYBEWISHLISTED
						.equals(jobApplicationStatus)) {
			jobApplicationStatusfinal = JobApplicationStatus.MAYBEWISHLISTED;
			jobSeekerApplicationStatusfinal = JobApplicationStatus.MAYBEWISHLISTED;
			isInJobSeekerWishlistFinal = isInJoobseekerWishlist;
		} else {
			jobApplicationStatusfinal = jobApplicationStatus;
			jobSeekerApplicationStatusfinal = jobApplicationStatus;
		}
        // Since we don't have recruiter available here.We need to Fetch the
        // recruiter form the
        // JobDetail info.

        jobRelationshipDataModelHelper.setJobApplicationStatus(jobId, jobseekerId,
                this.jobDetailService.getRecruiterIdFormJob(jobId), jobApplicationStatusfinal,
                jobSeekerApplicationStatusfinal, jobRelationshipDataModelResult.getRecruiterApplicationStatus(),
                jobRelationshipDataModelResult.isInRecruiterWishlist(), isInJobSeekerWishlistFinal,
                jobRelationshipDataModelResult);
        // set the recruiterId also in the Model , otherwise exception
        // would be thrown.
        jobRelationshipDataModelResult.setRecruiterId(getRecruiterFromJobId(jobId));

        jobRelationshipDetailService.addOrUpdate(jobRelationshipDataModelResult);

        return jobRelationshipDataModelResult;
    }

    private Long getRecruiterFromJobId(final Long jobId) {
        return this.jobDetailService.getRecruiterIdFromJobId(jobId);
    }

    // =========== MOCK DATA ======================
    // ========== ======================
    // private get mock data for testing purpose.
    private JobsForCandidateResponseModel getMockData() {
        JobsForCandidateResponseModel responseModel = new JobsForCandidateResponseModel();
        // set pagination
        Pagination pagination = new Pagination();
        pagination.setPageNumber(0);
        pagination.setPageSize(5);
        pagination.setShowNextButton(true);
        pagination.setShowPrevButton(false);
        responseModel.setPagination(pagination);
        // set rest of requierd data;
        List<JobsForCandidateResult> jobsForCandidateResults = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            JobsForCandidateResult jobsForCandidateResult = new JobsForCandidateResult();
            jobsForCandidateResult.setUserId(String.valueOf("1"));
            jobsForCandidateResult.setJobId(String.valueOf("3"));
            jobsForCandidateResult.setTitle("Software Development Engineer");
            jobsForCandidateResult.setLocation("bangalore");
            jobsForCandidateResult.setPercentageMatch(80.0);
            jobsForCandidateResult.setJobDescription("Required Developer who have worked already with"
                    + "the complex systems.");
            // add the result set.
            jobsForCandidateResults.add(jobsForCandidateResult);
        }
        responseModel.setJobsResult(jobsForCandidateResults);

        return responseModel;
    }
}
