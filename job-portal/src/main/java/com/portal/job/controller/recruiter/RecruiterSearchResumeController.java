package com.portal.job.controller.recruiter;

import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Maps;
import com.portal.job.constants.DateConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.dao.JobDetailDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.model.Pagination;
import com.portal.job.model.SearchCandidatesCriteria;
import com.portal.job.model.SearchCandidatesResponseDataModel;
import com.portal.job.service.JobDetailService;
import com.portal.job.services.search.JobseekerSearchService;
import com.portal.job.utils.DateUtility;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/recruiter")
public class RecruiterSearchResumeController extends JobPortalBaseController {

    private static final Logger log = LoggerFactory.getLogger(RecruiterSearchResumeController.class);
    @Autowired
    private JobDetailService jobDetailService;
    @Autowired
    private JobDetailDao jobDetailDao;
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private JobseekerSearchService jobseekerSearchService;
    @Autowired
    private JobseekerSearchService jobPortalEngineService;

    private void getJobDataModelMap(final Model model) {

        final List<JobDetail> jobDetailList = jobDetailService
                .getJobsByRecruiterIdForRecruiterSearchInTransaction(getUserId());
        System.out.println(jobDetailList.size());
        final Map<Long, String> jobMap = Maps.newHashMap();
        for (JobDetail jobDataModel : jobDetailList) {
            jobMap.put(
                    jobDataModel.getJobId(),
                    jobDataModel.getTitle()
                            + " - "
                            + DateUtility.getStringFromDate(jobDataModel.getCreateDate(),
                                    DateConstants.DD_MM_YYYY_FORMAT));
        }
        model.addAttribute("jobMap", jobMap);
    }

    @RequestMapping("/search")
    public ModelAndView viewResumeSearchPage(final Model model) {
        try {
            getJobDataModelMap(model);
            return getModelAndView(model, "recruiter/search-resume");
        } catch (Exception e) {
            log.error("Fatal Error: ", e);
            return getModelAndView(null, "redirect:/error/500");
        }
    }

    @RequestMapping("/search/{jobId}")
    public ModelAndView viewResumeSearchPageForJobId(@PathVariable("jobId") final Long jobId, final Model model) {
        try {
            getJobDataModelMap(model);
            model.addAttribute("jobId", jobId);
            return getModelAndView(model, "recruiter/search-resume");
        } catch (Exception e) {
            log.error("Fatal Error: ", e);
            return getModelAndView(null, "redirect:/error/500");
        }
    }

    @RequestMapping(value = { "search" }, method = RequestMethod.POST)
    public @ResponseBody SearchCandidatesResponseDataModel search(
            final SearchCandidatesCriteria searchCandidatesCriteria, Model model) {

        System.out.println(searchCandidatesCriteria);

        final int pageSize = 10;
        if (searchCandidatesCriteria.getPagination() == null) {
            searchCandidatesCriteria.setPagination(new Pagination());
        }
        searchCandidatesCriteria.getPagination().setPageSize(pageSize);

        final SearchCandidatesResponseDataModel searchCandidatesResponseDataModel = new SearchCandidatesResponseDataModel(
                jobseekerSearchService.getUsersBySearchCriteria(searchCandidatesCriteria),
                searchCandidatesCriteria.getPagination());

        return searchCandidatesResponseDataModel;
    }

}
