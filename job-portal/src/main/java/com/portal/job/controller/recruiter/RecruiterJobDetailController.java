package com.portal.job.controller.recruiter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.mapper.JobDetailMapper;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobFilterCriteria;
import com.portal.job.model.Pagination;
import com.portal.job.model.SearchJobsResult;
import com.portal.job.service.JobDetailService;
import com.portal.job.services.task.JobTaskExecutorService;
import com.portal.job.validator.JobDataModelValidator;

@Controller
@RequestMapping(value = "/recruiter/job")
public class RecruiterJobDetailController extends JobPortalBaseController {

    private static Logger log = LoggerFactory.getLogger(RecruiterJobDetailController.class);

    @Autowired
    private JobDetailService jobDetailSrv;
    @Autowired
    JobDetailMapper jobDetailMapper;
    @Autowired
    private JobTaskExecutorService taskExecutorService;
    @Autowired
    JobDataModelValidator jobDataModelValidator;
    @Autowired
    private ExecutorServiceManager executorServiceManager;

    @InitBinder("jobDataModel")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(jobDataModelValidator);
    }

    /**
     * Validate update request for job application status
     * 
     * @param jobId
     * @param jobseekerId
     */
    private void validateJobUpdate(final long userId, final String jobId) {
        if (StringUtils.isEmpty(jobId)) {
            throw new IllegalArgumentException("Invalid request.");
        }
        if (StringUtils.isEmpty(jobId) || !getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not allowed to do this operation.");
        }
    }

    @RequestMapping(value = { "/{jobId}" }, method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getJobDataModelByJobId(@PathVariable("jobId") final Long jobId, Model model) {
        final Map<String, Object> response = Maps.newHashMap();
        try {
            response.put("jobDataModel", jobDetailSrv.getJobDataModelByJobId(jobId));
            response.put("status", "success");
        } catch (Exception e) {
            log.error("failed to get job" + e);
            response.put("status", "error");
            response.put("errorMessage", "Failed to get job");
        }
        return response;
    }

    @RequestMapping(value = "viewJob")
    public ModelAndView viewJobForRecruiter(Model model) {
        model.addAttribute("jobDetailsList", new ArrayList<JobDataModel>());
        return getModelAndView(model, "recruiter/viewJob");

    }

    @RequestMapping(value = { "getJobs" }, method = RequestMethod.POST)
    public @ResponseBody SearchJobsResult getJobDetailsForRecruiter(final JobFilterCriteria jobFilterCriteria,
            Model model) {
        SearchJobsResult searchJobsResult = new SearchJobsResult();
        List<JobDataModel> jobDataList;
        try {
            final int pageSize = 10;
            if (jobFilterCriteria.getPagination() == null) {
                jobFilterCriteria.setPagination(new Pagination());
            }
            jobFilterCriteria.getPagination().setPageSize(pageSize);
            searchJobsResult = jobDetailSrv.getFilteredJobsPostedByRecruiter(getUserId(), jobFilterCriteria);
            model.addAttribute("searchJobsResult", searchJobsResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error:" + e);
            jobDataList = new ArrayList<JobDataModel>();
            searchJobsResult.setJobDataModelList(jobDataList);
            searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        }
        return searchJobsResult;
    }

    // Recruiter is creating the JOB
    @RequestMapping(value = "post-job", method = RequestMethod.GET)
    public ModelAndView addJob(Model model) {
        model.addAttribute("jobDataModel", new JobDataModel());
        return getModelAndView(model, "recruiter/post-job");
    }

    // Recruiter is creating the JOB
    @RequestMapping(value = { "edit/{jobId}" }, method = RequestMethod.GET)
    public ModelAndView editJob(@PathVariable("jobId") final Long jobId, Model model) {
        JobDataModel jobDataModel;
        try {
            jobDataModel = jobDetailSrv.getJobDataModelByJobId(jobId);
        } catch (Exception e) {
            log.error("failed to get job" + e);
            // Initialise the JobDataModel.
            jobDataModel = new JobDataModel();
        }
        model.addAttribute("jobDataModel", jobDataModel);
        return getModelAndView(model, "recruiter/post-job");
    }

    // Recruiter is deleting the JOB
    @RequestMapping(value = { "delete/{jobId}" }, method = RequestMethod.POST)
    public @ResponseBody Map<String, String> deleteJob(@PathVariable("jobId") final Long jobId, Model model)
            throws ParseException {
        final Map<String, String> response = Maps.newHashMap();
        try {
            response.put("status", "succes");
            this.jobDetailSrv.delete(jobId);
            model.addAttribute("successMessage", "Job is deleted successfully!!!");
            return response;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Some problem in deleting Job!!!");
            return response;
        }
    }

    @RequestMapping(value = "post-job", method = RequestMethod.POST)
    public ModelAndView saveJobDetails(@ModelAttribute("jobDataModel") @Validated final JobDataModel jobDataModel,
            BindingResult bindingResult, Model model) throws ParseException {
        JobDataModel dataModel = null;
        log.info("Detail lMODEL:" + jobDataModel);
        try {
            if (!bindingResult.hasErrors()) {
                // validate the recruiter who wants to update JOB.

                if (!StringUtils.isEmpty(jobDataModel.getJobId())) {
                    validateJobUpdate(getUserId(), jobDataModel.getJobId());
                }

                dataModel = jobDetailSrv.createOrUpdate(jobDataModel, getUserId());

                model.addAttribute("successMessage", "Job posted successfully. View your job on by My Jobs");
                model.addAttribute("jobDataModel", new JobDataModel());
                taskExecutorService.computeCandidatesForPostedJob(dataModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed tp save job.");
            model.addAttribute("jobDataModel", jobDataModel);
            log.error("Unable to save job", e);
        }
        return getModelAndView(model, "recruiter/post-job");
    }

    @RequestMapping(value = "getAllJobDetails")
    public @ResponseBody List<JobDataModel> getAllExperienceDetails(Model model) {
        List<JobDataModel> jobDataList = jobDetailSrv.getAllJobDetails();
        return jobDataList;
    }

    // ////// MOCK ////////////////////////////////////
    @RequestMapping(value = "mockfilterjob")
    public @ResponseBody SearchJobsResult mockSaveJobDetails() {
        JobFilterCriteria filterCriteria = new JobFilterCriteria();
        filterCriteria.setJobTitle("ALLTITLE");
        filterCriteria.setJobStatus("ALLJOBS");
        Pagination pagination = new Pagination();
        pagination.setPageNumber(0);
        // pagination.set
        filterCriteria.setPagination(pagination);
        return this.jobDetailSrv.getFilteredJobsPostedByRecruiter(Long.getLong("1"), filterCriteria);
    }

    @RequestMapping(value = "mockgetjobdetaiByJobId")
    public @ResponseBody JobDataModel mockSaveJobDetailsByJobId() {

        JobDataModel dataModel = this.jobDetailMapper.getMockJobDataModel("19", null, "1", null, null);
        return this.jobDetailSrv.getJobDetailForJobId(dataModel);
    }

    @RequestMapping(value = "mockgetjobdetaiByRecruiter")
    public @ResponseBody List<JobDataModel> mockSaveJobDetailsByRecruiterId() {

        JobDataModel dataModel = this.jobDetailMapper.getMockJobDataModel("19", null, "1", null, null);
        return this.jobDetailSrv.getJobsPostedByRecruiterId(Long.valueOf("1"));
    }

    @RequestMapping("mocksave")
    public @ResponseBody JobDataModel mockSaveJobDetails(HttpSession httpSession, Model model) throws ParseException {

        String recruiterId = "1";

        StringBuilder skillBuilder = new StringBuilder();
        skillBuilder.append("C++").append("AJAX");

        JobDataModel dataModel = this.jobDetailMapper.getMockJobDataModel("4", "Prinipal Enginer", "1",
                "Prinipal Enginer", skillBuilder.toString());

        JobDataModel viewJobData = jobDetailSrv.createOrUpdate(dataModel, Long.valueOf(recruiterId));
        return viewJobData;
    }
}
