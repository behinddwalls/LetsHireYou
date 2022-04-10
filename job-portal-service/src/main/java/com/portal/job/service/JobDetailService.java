package com.portal.job.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.portal.job.constants.JobPortalConstants;
import com.portal.job.dao.BaseSearchBuilder;
import com.portal.job.dao.JobDetailDao;
import com.portal.job.dao.JobRelationshipDetailsDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.JobStatus;
import com.portal.job.enums.PaginationAction;
import com.portal.job.enums.ProcessingState;
import com.portal.job.enums.SortingCriteria;
import com.portal.job.mapper.JobDetailMapper;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobFilterCriteria;
import com.portal.job.model.JobsForCandidateResult;
import com.portal.job.model.JobsSearchResult;
import com.portal.job.model.SearchJobForCandidatesCriteria;
import com.portal.job.model.SearchJobsResult;

@Service
public class JobDetailService {

    private static Logger log = LoggerFactory.getLogger(JobDetailService.class);

    @Autowired
    private JobDetailDao jobDetailDao;
    @Autowired
    private JobDetailMapper jobDetailMapper;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private OrganisationDetailService organisationDetailService;

    @Transactional
    public Long getRecruiterIdFromJobId(final Long jobId) {
        return this.jobDetailDao.find(jobId).getUserDetail().getUserId();
    }

    @Transactional
    public JobDataModel createOrUpdate(final JobDataModel jobData, final Long recruiterId) throws ParseException {
        try {
            JobDetail jobDetail = jobDetailMapper.getEntityFromDataModel(jobData, recruiterId);
            // Set the OrganizationTier for which the recruiter is
            // posting the JOB.
            int tier = this.organisationDetailService.getOrganisationTier(jobData.getOrganisationName());
            // Set the tier value in JobDetail.
            jobDetail.setOrganisationTier(tier);

            if (jobDetail.getJobId() != null) {
                JobDataModel alreadyPresentData = getJobDataModelByJobId(jobDetail.getJobId());
                jobDetail.setJobProcessingState(alreadyPresentData.getJobProcessingState());
                jobDetail.setLastProcessedTime(alreadyPresentData.getLastProcessedTime());
                jobDetailDao.mergeEntity(jobDetail);
                // return jobData;
            } else {

                // save the JobDetail
                boolean isSaved = this.jobDetailDao.save(jobDetail);

            }
            jobData.setJobId(String.valueOf(jobDetail.getJobId()));
            jobData.setOrganisationTier(jobDetail.getOrganisationTier());
            jobData.setRecruiterId(recruiterId.toString());
            return jobData;

        } catch (Exception e) {
            log.info("Exception in Update Shit:", e);
            throw new org.apache.http.ParseException();
        }

    }

    @Transactional
    public List<JobDataModel> getAllJobDetails() {
        List<JobDetail> jobDetailList = jobDetailDao.findAll();
        return jobDetailMapper.getDataModelListFromEntityList(jobDetailList);
    }

    @Transactional
    public List<JobDataModel> getJobDataModelForJobAndUser(final long userId, final long jobId) {

        return jobDetailMapper.getDataModelListFromEntityList(jobDetailDao.getEntitiesByPropertyValue(
                new HashMap<String, Object>() {
                    private static final long serialVersionUID = 1L;

                    {
                        put("userDetail.userId", userId);
                        put("jobId", jobId);
                    }
                }, JobDetail.class));

    }

    @Transactional
    public JobDataModel getJobDetailForJobId(final JobDataModel jobDataModel) {
        return this.jobDetailMapper
                .getDataModelFromEntity(this.jobDetailDao.find(Long.valueOf(jobDataModel.getJobId())));
    }

    @Transactional
    public Long getRecruiterIdFormJob(final Long jobId) {
        return this.jobDetailDao.find(jobId).getUserDetail().getUserId();
    }

    @Transactional
    public JobDataModel getJobDataModelByJobId(final Long jobId) {
        return this.jobDetailMapper.getDataModelFromEntity(this.jobDetailDao.find(jobId));
    }

    @Transactional
    public List<JobDataModel> getJobsPostedByRecruiterId(@NotNull final Long recruiterId) {
        return getAllJobDataModelsForRecruiter(recruiterId);
    }

    private Search getSearchCriteriaForGettingJobsForUser(UserDetail user) {
        Search search = new Search(JobDetail.class);
        log.info("zzzzz get search for fallback called");
        // if (StringUtils.isEmpty(user.getJobFunction()))
        // throw new RuntimeException("JobFunction not present for User "
        // + user.getUserId());
        // Filter jobFunctionFilter = Filter.equal("jobFunction",
        // user.getJobFunction());
        // // uncomment after adding filed in db
        // if (!StringUtils.isEmpty(user.getIndustryName())) {
        // Filter industryFilter = Filter.equal("industryName",
        // user.getIndustryName());
        // search.addFilterOr(jobFunctionFilter, industryFilter);
        // } else {
        // search.addFilter(jobFunctionFilter);
        // }
        if (user.getCtc() != null) {
            search.addFilterGreaterOrEqual("maxSalary", user.getCtc());
        }
        List<Filter> skillFilterList = new ArrayList<Filter>();
        List<String> userSkillList = Lists.newArrayList();
        if (null != user.getSkills()) {
            userSkillList = Arrays.asList(user.getSkills().split(JobPortalConstants.SKILLS_DELEMITER));
            userSkillList.stream().forEach(skill -> {
                Filter f = Filter.ilike("jobSkills", "%" + skill + "%");
                skillFilterList.add(f);
            });
            search.addFilterOr(skillFilterList.toArray(new Filter[0]));
        }

        return search;
    }

    @Transactional
    public JobsSearchResult getFallBackJobsForUser(final Long userId, final SearchJobForCandidatesCriteria criteria,
            List<Long> ineligibleJobIds) {
        log.info("zzzzzzzzz getFallbackJobsForUser called");
        UserDetail user = userDetailService.getUserDetail(userId);
        Search jobsSearchForUser = getSearchCriteriaForGettingJobsForUser(user);
        log.info("zzzzzz fallback search = " + jobsSearchForUser);
        int totalCount = jobDetailDao.count(jobsSearchForUser);
        log.info("zzzzz total count of fallback jobs:" + totalCount);
        int actualCount = totalCount;
        int pageReductionValue = 0;
        int actualPageNumber = criteria.getPagination().getPageNumber();
        int finalPageNumber = actualPageNumber;
        if (null != ineligibleJobIds && !ineligibleJobIds.isEmpty()) {
            jobsSearchForUser.addFilterNotIn("jobId", ineligibleJobIds);
            actualCount = totalCount - ineligibleJobIds.size();
            pageReductionValue = ineligibleJobIds.size() / criteria.getPagination().getPageSize();
            if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT)
                actualPageNumber = (actualPageNumber - pageReductionValue >= 0) ? actualPageNumber - pageReductionValue
                        : 0;
        }
        if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT) {
            finalPageNumber = actualPageNumber + 1;
        } else if (criteria.getPagination().getPaginationAction() == PaginationAction.PREV) {
            finalPageNumber = actualPageNumber - 1;
        }
        jobsSearchForUser.setPage(finalPageNumber);
        jobsSearchForUser.setMaxResults(criteria.getPagination().getPageSize());
        List<JobDetail> jobsForUser = jobDetailDao.search(jobsSearchForUser);
        log.info("zzzzzzz fallback jobsForUSer" + jobsForUser);
        List<JobsForCandidateResult> jobsResult = jobsForUser.stream().map(job -> convertToJobsSearchResult(job))
                .collect(Collectors.toList());
        jobsResult.stream().forEach(job -> job.setUserId(userId.toString()));
        JobsSearchResult result = new JobsSearchResult(totalCount, actualCount, jobsResult, actualPageNumber,
                finalPageNumber);
        return result;

    }

    private JobsForCandidateResult convertToJobsSearchResult(JobDetail job) {
        JobsForCandidateResult jobsForCandidateResult = new JobsForCandidateResult();
        jobsForCandidateResult.setJobDescription(job.getJobDescription());
        jobsForCandidateResult.setLink_to_site(job.getLinkToSite());
        jobsForCandidateResult.setJobExperiance(job.getJobExperiance().toString());
        jobsForCandidateResult.setEmploymentType(job.getEmploymentType());
        jobsForCandidateResult.setJobFunction(job.getJobFunction());
        jobsForCandidateResult.setJobId(job.getJobId().toString());
        jobsForCandidateResult.setLocation(job.getLocationDetail());
        jobsForCandidateResult.setMaxSalary(job.getMaxSalary().toString());
        jobsForCandidateResult.setMinSalary(job.getMinSalary().toString());
        jobsForCandidateResult.setOrganisationName(job.getOrganisationName());
        jobsForCandidateResult.setTitle(job.getTitle());
        jobsForCandidateResult.setFallback(true);
        log.info("zzzzzzzz jobsForCandidateResult = " + jobsForCandidateResult.toString());
        return jobsForCandidateResult;
    }

    @Transactional
    public List<JobDataModel> getJobsFilteredByTitle(@NotNull final String jobTitle) {
        List<JobDetail> jobDetailList = jobDetailDao.getEntitiesSimilarToPropertyValue(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("title", jobTitle);
            }
        }, JobDetail.class);
        return this.jobDetailMapper.getDataModelListFromEntityList(jobDetailList);
    }

    @Transactional
    public List<JobDataModel> getJobsByStatus(@NotNull final String jobStatus) {
        List<JobDetail> jobDetailList = jobDetailDao.getEntitiesByPropertyValue(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("jobStatus", jobStatus);
            }
        }, JobDetail.class);
        return this.jobDetailMapper.getDataModelListFromEntityList(jobDetailList);
    }

    // Put pblic methods which could be used form the outside service.
    // These are methods without Transactions.

    List<JobDataModel> getAllJobDataModelsForRecruiter(final Long recruiterId) {
        final UserDetail user = new UserDetail();
        user.setUserId(recruiterId);
        List<JobDetail> jobDetailList = jobDetailDao.getEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
            private static final long serialVersionUID = 1L;

            {
                // userDetail Here represent the Recruiter
                put("userDetail", user);
            }
        }, JobDetail.class);
        return this.jobDetailMapper.getDataModelListFromEntityList(jobDetailList);
    }

    @SuppressWarnings("serial")
    public List<JobDetail> getJobsByRecruiterIdForRecruiterSearch(final Long recruiterId) {

        return jobDetailDao.getEntitiesByPropertyValue(new HashMap<String, Object>() {
            {
                put("userDetail.userId", recruiterId);
                put("jobStatus", JobStatus.ACTIVE);
            }
        }, JobDetail.class);
    }

    @Transactional
    public List<JobDetail> getJobsByRecruiterIdForRecruiterSearchInTransaction(final Long recruiterId) {
        return getJobsByRecruiterIdForRecruiterSearch(recruiterId);
    }

    @Transactional
    public void setJobProcessingStateAndTimestamp(final long jobId, ProcessingState processingState) {
        JobDetail job = jobDetailDao.find(jobId);
        job.setJobProcessingState(processingState.name());
        job.setLastProcessedTime(new Date());
        jobDetailDao.save(job);
    }

    /**
     * 
     * @param recruiterId
     * @param jobTitle
     * @param jobStatus
     * @return TODO- Do the searching based on title more flexible. We can sort
     *         the Data according to the JobCreation data.
     */
    @Transactional
    public SearchJobsResult getFilteredJobsPostedByRecruiter(@NotNull final Long recruiterId,
            @NotNull final JobFilterCriteria jobFilterCriteria) {
        SearchJobsResult searchJobsResult = new SearchJobsResult();
        searchJobsResult.setJobDataModelList(Lists.newArrayList());
        searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        try {

            int pageNumber = 0;
            int finalPageNumber = 0;

            if (PaginationAction.NEXT.equals(jobFilterCriteria.getPagination().getPaginationAction())) {
                pageNumber = jobFilterCriteria.getPagination().getPageNumber() + 1;
                finalPageNumber = pageNumber;
            } else if (PaginationAction.PREV.equals(jobFilterCriteria.getPagination().getPaginationAction())) {
                pageNumber = jobFilterCriteria.getPagination().getPageNumber() - 1;
                finalPageNumber = pageNumber;
            }

            int pageSize = jobFilterCriteria.getPagination().getPageSize();
            // create BaseSearchBuilder.
            BaseSearchBuilder baseSearchBuilder = new BaseSearchBuilder().searchEntitiesSimilarToPropertyValue(
                    new HashMap<String, String>() {
                        private static final long serialVersionUID = 1L;

                        {
                            if (JobStatus.ALLJOBS.getStatus().equals(jobFilterCriteria.getJobStatus())) {
                                put("jobStatus", "%%");
                            } else {
                                put("jobStatus", "%" + jobFilterCriteria.getJobStatus() + "%");
                            }
                        }
                    }).searchEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
                private static final long serialVersionUID = 1L;

                {
                    UserDetail recruiterDetail = new UserDetail();
                    recruiterDetail.setUserId(recruiterId);
                    // userDetail Here represent the Recruiter
                    put("userDetail", recruiterDetail);
                }
            });

            baseSearchBuilder = getBaseSearchBuilderWithJobSortingCriteria(baseSearchBuilder,
                    jobFilterCriteria.getJobSortCriteria());

            // Now search the object.
            List<JobDetail> jobDetailList = this.jobDetailDao.getEntities(baseSearchBuilder.buildSearchObject(),
                    finalPageNumber, pageSize);
            // find the total number of possible result.
            long totalResultCount = this.jobDetailDao.getEntitiesCount(baseSearchBuilder.buildSearchObject());

            jobFilterCriteria.getPagination().setTotalResultCount(totalResultCount);
            jobFilterCriteria.getPagination().setPageNumber(finalPageNumber);
            jobFilterCriteria.getPagination().setTotalPageCount(totalResultCount / pageSize);

            if (pageNumber > 0) {
                jobFilterCriteria.getPagination().setShowPrevButton(true);
            }
            if (totalResultCount > (pageNumber + 1) * pageSize) {
                jobFilterCriteria.getPagination().setShowNextButton(true);

            }
            // set the values for search result.
            searchJobsResult.setJobDataModelList(this.jobDetailMapper.getDataModelListFromEntityList(jobDetailList));
            searchJobsResult.setPagination(jobFilterCriteria.getPagination());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Exception while fetching the JOB detail..." + ex);
        }
        return searchJobsResult;
    }

    // Put public methods which could be used form the outside service.
    // These are methods without Transactions.

    /**
     * 
     * @param jobData
     * @param recruiterId
     * @return
     * @throws ParseException
     *             Here we would just change the 'status' of JOB to 'CLOSED'
     *             without deleting JOB entry form the table.
     */
    @Transactional
    public boolean delete(@NotNull final Long jobId) throws ParseException {
        try {
            JobDetail jobDetail = this.jobDetailDao.find(jobId);
            // Change the JOB status.
            jobDetail.setJobStatus(JobStatus.CLOSED.getStatus());
            // save the JobDetail
            return this.jobDetailDao.save(jobDetail);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception in Deleting JOB!!!", e);
            throw new org.apache.http.ParseException();
        }
    }

    // private
    private BaseSearchBuilder getBaseSearchBuilderWithJobSortingCriteria(BaseSearchBuilder baseSearchBuilder,
            String jobSerachCriteria) {
        if (SortingCriteria.OLDEST_JOBCREATED.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
            baseSearchBuilder.sortAscEntitiesByPropertyValue("createDate");
        } else if (SortingCriteria.NAME_A_TO_Z.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
            baseSearchBuilder.sortAscEntitiesByPropertyValue("title");
        } else if (SortingCriteria.NAME_Z_TO_A.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
            baseSearchBuilder.sortDescEntitiesByPropertyValue("title");
        } else if (SortingCriteria.LOCAL_A_TO_Z.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
            baseSearchBuilder.sortAscEntitiesByPropertyValue("locationDetail");
        } else if (SortingCriteria.LOCAL_Z_TO_A.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
            baseSearchBuilder.sortDescEntitiesByPropertyValue("locationDetail");
        } else {
            // Default sort with the 'job_create date'
            baseSearchBuilder.sortDescEntitiesByPropertyValue("createDate");
        }
        return baseSearchBuilder;
    }

}
