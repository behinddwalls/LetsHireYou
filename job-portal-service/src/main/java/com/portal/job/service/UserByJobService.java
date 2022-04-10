package com.portal.job.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import com.portal.job.dao.UserByJobDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.UserByJob;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.PaginationAction;
import com.portal.job.model.ComputedUsersByJob;
import com.portal.job.model.JobsForCandidateResult;
import com.portal.job.model.JobsSearchResult;
import com.portal.job.model.SearchCandidatesCriteria;
import com.portal.job.model.SearchCandidatesResult;
import com.portal.job.model.SearchJobForCandidatesCriteria;
import com.portal.job.model.UserSearchResult;

@Service
public class UserByJobService {

    private static final Logger log = LoggerFactory.getLogger(UserByJobService.class);

    @Autowired
    private UserByJobDao userByJobDao;

    /**
     * Only returns lazy values ie ids only. For more information create new
     * method.
     * 
     * @param jobId
     * @return
     */
    @Transactional
    public List<UserByJob> getUsersForJob(long jobId) {
        Search search = new Search(UserByJob.class);
        search.addFilterEqual("jobDetail.jobId", jobId);
        return userByJobDao.getEntities(search);
    }

    @Transactional
    public UserByJob getUser(long userId, long jobId) {
        Search search = new Search(UserByJob.class);
        search.addFilterEqual("jobDetail.jobId", jobId);
        search.addFilterEqual("userDetail.userId", userId);
        List<UserByJob> usersByJob = userByJobDao.getEntities(search);
        if (usersByJob != null)
            return usersByJob.get(0);
        return null;
    }

    @Transactional
    public List<UserByJob> getUserByJobbyJobIdAndUserIds(long jobId, List<Long> userIds) {
        Search search = new Search(UserByJob.class);
        search.addFilterEqual("jobDetail.jobId", jobId);
        search.addFilterIn("userDetail.userId", userIds);
        return userByJobDao.getEntities(search);
    }

    @Transactional
    public UserSearchResult getProcessedUsersForPostedJobs(final SearchCandidatesCriteria criteria,
            final List<Long> ineligibleUserIds) {
        Search search = new Search(UserByJob.class);
        search.addSort(Sort.desc("percentageMatch"));
        search.addFilterEqual("jobDetail.jobId", criteria.getJobId());
        search.addFilterGreaterOrEqual("percentageMatch", 10);
        int totalCount = userByJobDao.count(search);
        int actualCount = totalCount;
        int pageReductionValue = 0;
        int actualPageNumber = criteria.getPagination().getPageNumber();
        int finalPageNumber = criteria.getPagination().getPageNumber();
        if (ineligibleUserIds != null && !ineligibleUserIds.isEmpty()) {
            search.addFilterNotIn("userDetail.userId", ineligibleUserIds);
            actualCount = totalCount - ineligibleUserIds.size();
            pageReductionValue = ineligibleUserIds.size() / criteria.getPagination().getPageSize();
            // if (criteria.getPagination().getPaginationAction() ==
            // PaginationAction.NEXT)
            // actualPageNumber = (actualPageNumber - pageReductionValue >= 0) ?
            // actualPageNumber - pageReductionValue
            // : 0;
        }
        if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT) {
            finalPageNumber = actualPageNumber + 1;
        } else if (criteria.getPagination().getPaginationAction() == PaginationAction.PREV) {
            finalPageNumber = actualPageNumber - 1;
        }
        search.setMaxResults(criteria.getPagination().getPageSize());
        search.setPage(finalPageNumber);
        List<UserByJob> usersByJob = userByJobDao.getEntities(search);
        List<SearchCandidatesResult> searchCandidateResults = usersByJob.stream()
                .map(userByJob -> getUserSearchResult(userByJob, false)).collect(Collectors.toList());
        return new UserSearchResult(totalCount, actualCount, searchCandidateResults, actualPageNumber, finalPageNumber);

    }

    private SearchCandidatesResult getUserSearchResult(UserByJob userByJob, boolean fallback) {
        SearchCandidatesResult result = new SearchCandidatesResult();
        UserDetail user = userByJob.getUserDetail();
        // result.setCompany(user.getLastCompanyName());
        result.setExperience(user.getPastExperienceMonths() != null ? user.getPastExperienceMonths().toString() : "0");
        result.setFallback(fallback);
        result.setFirstName(user.getFirstName());
        result.setJobseekerId(user.getUserId());
        result.setLastName(user.getLastName());
        result.setLocation(user.getAddress());
        result.setMatch(userByJob.getPercentageMatch());
        result.setProfileHeadline(user.getProfileHeadline());
        result.setProfileImageUrl(user.getProfileImageUrl());
        result.setJobId(userByJob.getJobDetail().getJobId());
        result.setCompany(user.getLatestCompanyName());
        result.setUserResumeLink(user.getUserResumeLink());
        return result;

    }

    @Transactional
    public JobsSearchResult getProcessedJobsForUser(final SearchJobForCandidatesCriteria criteria, final Long userId,
            final List<Long> ineligibleJobIds) {
        Search search = new Search(UserByJob.class);
        search.addSort(Sort.desc("percentageMatch"));
        search.addFilterEqual("userDetail.userId", userId);
        int totalCount = userByJobDao.count(search);
        int actualCount = totalCount;
        int pageReductionValue = 0;
        int actualPageNumber = criteria.getPagination().getPageNumber();
        int finalPageNumber = criteria.getPagination().getPageNumber();
        if (ineligibleJobIds != null && !ineligibleJobIds.isEmpty()) {
            search.addFilterNotIn("jobDetail.jobId", ineligibleJobIds);
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
        search.setPage(finalPageNumber);
        search.setMaxResults(criteria.getPagination().getPageSize());
        List<UserByJob> userByJobs = userByJobDao.getEntities(search);
        System.out.println(userByJobs);
        List<JobsForCandidateResult> candidateResults = userByJobs.stream()
                .map(userByJob -> getJobCandidateResult(userByJob, false)).collect(Collectors.toList());
        candidateResults.stream().forEach(candidateResult -> candidateResult.setUserId(userId.toString()));
        JobsSearchResult result = new JobsSearchResult(totalCount, actualCount, candidateResults, actualPageNumber,
                finalPageNumber);

        return result;
    }

    private JobsForCandidateResult getJobCandidateResult(UserByJob userByJob, boolean fallback) {
        JobsForCandidateResult result = new JobsForCandidateResult();
        JobDetail job = userByJob.getJobDetail();
        result.setEmploymentType(job.getEmploymentType());
        result.setFallback(fallback);
        result.setJobDescription(job.getJobDescription());
        result.setJobExperiance(job.getJobExperiance().toString());
        result.setJobFunction(job.getJobFunction());
        result.setJobId(job.getJobId().toString());
        result.setLocation(job.getLocationDetail());
        if (job.getKeepSalayHidden() == 0) {
            result.setMaxSalary(job.getMaxSalary().toString());
            result.setMinSalary(job.getMinSalary().toString());
        }
        result.setOrganisationName(job.getOrganisationName());
        result.setPercentageMatch(userByJob.getPercentageMatch());
        result.setSalaryCurrencyCode(job.getSalaryCurrencyCode());
        result.setTitle(job.getTitle());
        return result;

    }

    /**
     * 
     * @param computedUsersByJob
     * @return Save the computed Data coming from Engine after processing.
     * @throws Exception
     */
    @Transactional
    public boolean saveComputedUsersByJob(final ComputedUsersByJob computedUsersByJob) throws Exception {

        try {
            // Build the Objects.
            if (computedUsersByJob != null && !computedUsersByJob.getUserIdToPercentageMap().isEmpty()) {
                // Create JobObject.
                JobDetail jobDetail = new JobDetail();
                jobDetail.setJobId(computedUsersByJob.getJobId());

                final Search search = new Search(UserByJob.class);
                search.addFilterEqual("jobDetail", jobDetail);
                final List<UserByJob> userByJobs = this.userByJobDao.search(search);
                final Map<Long, UserByJob> userByJobsMapToBeUpdated = Maps.newHashMap();

                final Set<Long> filteredUserIds = computedUsersByJob.getUserIdToPercentageMap().keySet();

                if (filteredUserIds != null && !filteredUserIds.isEmpty() && userByJobs != null
                        && !userByJobs.isEmpty()) {
                    System.out.println(filteredUserIds);
                    for (UserByJob userByJob : userByJobs) {
                        final UserDetail ud = userByJob.getUserDetail();
                        if (filteredUserIds.contains(ud.getUserId())) {
                            userByJob.setPercentageMatch(Double.valueOf(
                                    computedUsersByJob.getUserIdToPercentageMap().get(ud.getUserId())).intValue());
                            userByJobsMapToBeUpdated.put(ud.getUserId(), userByJob);
                            computedUsersByJob.getUserIdToPercentageMap().remove(ud.getUserId());
                        }
                    }
                }

                // Create the List of Object to Save.
                List<UserByJob> userByJobList = Lists.newLinkedList();
                if (filteredUserIds != null) {
                    for (Long userId : filteredUserIds) {
                        UserByJob userByJob = new UserByJob();
                        // set Job detail.
                        userByJob.setJobDetail(jobDetail);
                        // set User Detail
                        UserDetail userDetail = new UserDetail();
                        userDetail.setUserId(userId);
                        userByJob.setUserDetail(userDetail);
                        // set percentage match
                        // TODO- Need to check this.
                        String percentageMatch = computedUsersByJob.getUserIdToPercentageMap().get(userId);
                        int indexOf = percentageMatch.indexOf('.');
                        userByJob.setPercentageMatch(Long.valueOf(indexOf >= 0 ? percentageMatch.substring(0, indexOf)
                                : percentageMatch));

                        // add to the List.
                        userByJobList.add(userByJob);
                    }
                }
                userByJobList.addAll(userByJobsMapToBeUpdated.values());

                if (!userByJobList.isEmpty()) {
                    // save the Lsit to DB.
                    this.userByJobDao.save((UserByJob[]) userByJobList.toArray(new UserByJob[userByJobList.size()]));

                    log.info("USerByJobs is saved successsfully !!!");
                }
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Error:in saveing UserByJobs:" + ex);
            throw ex;
        }
        // Not able to Data save.
        return false;
    }

    @Transactional
    public void deleteObseleteUsersFromUserByJob(final Set<Long> computedUserIds, final Long jobId) {

        // Create JobObject.
        JobDetail jobDetail = new JobDetail();
        jobDetail.setJobId(jobId);

        final Search search = new Search(UserByJob.class);
        search.addFilterEqual("jobDetail", jobDetail);
        final List<UserByJob> userByJobs = this.userByJobDao.search(search);

        if (null != userByJobs) {
            List<Long> userByJobIds = userByJobs.stream()
                    .filter(u -> !computedUserIds.contains(u.getUserDetail().getUserId()))
                    .map(userByJob -> userByJob.getUserByJobId()).collect(Collectors.toList());

            if (userByJobIds != null && !userByJobIds.isEmpty()) {
                this.userByJobDao.removeByIds(userByJobIds.toArray(new Serializable[0]));
            }
        }

    }
}
