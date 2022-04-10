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
import com.portal.job.dao.JobsByUserDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.JobsByUser;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.PaginationAction;
import com.portal.job.model.ComputedJobsByUser;
import com.portal.job.model.JobsForCandidateResult;
import com.portal.job.model.JobsSearchResult;
import com.portal.job.model.SearchCandidatesCriteria;
import com.portal.job.model.SearchCandidatesResult;
import com.portal.job.model.SearchJobForCandidatesCriteria;
import com.portal.job.model.UserSearchResult;

@Service
public class JobsByUserService {

	private static final Logger log = LoggerFactory
			.getLogger(JobsByUserService.class);

	@Autowired
	private JobsByUserDao jobsByUserDao;

	/**
	 * Only returns lazy values ie ids only. For more information create new
	 * method.
	 * 
	 * @param jobId
	 * @return
	 */
	@Transactional
	public List<JobsByUser> getJobsForUser(long userId) {
		Search search = new Search(JobsByUser.class);
		search.addFilterEqual("jobDetail.jobId", userId);
		return jobsByUserDao.getEntities(search);
	}

	@Transactional
	public JobsByUser getUser(long userId, long jobId) {
		Search search = new Search(JobsByUser.class);
		search.addFilterEqual("jobDetail.jobId", jobId);
		search.addFilterEqual("userDetail.userId", userId);
		List<JobsByUser> usersByJob = jobsByUserDao.getEntities(search);
		if (usersByJob.size() != 0)
			return usersByJob.get(0);
		return null;
	}

	@Transactional
	public List<JobsByUser> getJobsByUserbyJobIdAndUserIds(long jobId,
			List<Long> userIds) {
		Search search = new Search(JobsByUser.class);
		search.addFilterEqual("jobDetail.jobId", jobId);
		search.addFilterIn("userDetail.userId", userIds);
		return jobsByUserDao.getEntities(search);
	}

	@Transactional
	public UserSearchResult getProcessedUsersForPostedJobs(
			final SearchCandidatesCriteria criteria,
			final List<Long> ineligibleUserIds) {
		Search search = new Search(JobsByUser.class);
		search.addSort(Sort.desc("percentageMatch"));
		search.addFilterEqual("jobDetail.jobId", criteria.getJobId());
		search.addFilterGreaterOrEqual("percentageMatch", 10);
		int totalCount = jobsByUserDao.count(search);
		int actualCount = totalCount;
		int pageReductionValue = 0;
		int actualPageNumber = criteria.getPagination().getPageNumber();
		int finalPageNumber = criteria.getPagination().getPageNumber();
		if (ineligibleUserIds != null && !ineligibleUserIds.isEmpty()) {
			search.addFilterNotIn("userDetail.userId", ineligibleUserIds);
			actualCount = totalCount - ineligibleUserIds.size();
			pageReductionValue = ineligibleUserIds.size()
					/ criteria.getPagination().getPageSize();
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
		List<JobsByUser> usersByJob = jobsByUserDao.getEntities(search);
		List<SearchCandidatesResult> searchCandidateResults = usersByJob
				.stream()
				.map(jobsByUser -> getUserSearchResult(jobsByUser, false))
				.collect(Collectors.toList());
		return new UserSearchResult(totalCount, actualCount,
				searchCandidateResults, actualPageNumber, finalPageNumber);

	}

	private SearchCandidatesResult getUserSearchResult(JobsByUser jobsByUser,
			boolean fallback) {
		SearchCandidatesResult result = new SearchCandidatesResult();
		UserDetail user = jobsByUser.getUserDetail();
		// result.setCompany(user.getLastCompanyName());
		result.setExperience(user.getPastExperienceMonths() != null ? user
				.getPastExperienceMonths().toString() : "0");
		result.setFallback(fallback);
		result.setFirstName(user.getFirstName());
		result.setJobseekerId(user.getUserId());
		result.setLastName(user.getLastName());
		result.setLocation(user.getAddress());
		result.setMatch(jobsByUser.getPercentageMatch());
		result.setProfileHeadline(user.getProfileHeadline());
		result.setProfileImageUrl(user.getProfileImageUrl());
		result.setJobId(jobsByUser.getJobDetail().getJobId());
		result.setCompany(user.getLatestCompanyName());
		return result;

	}

	@Transactional
	public JobsSearchResult getProcessedJobsForUser(
			final SearchJobForCandidatesCriteria criteria, final Long userId,
			final List<Long> ineligibleJobIds) {
		Search search = new Search(JobsByUser.class);
		search.addSort(Sort.desc("percentageMatch"));
		search.addFilterEqual("userDetail.userId", userId);
		search.addFilterGreaterOrEqual("percentageMatch", 10);
		int totalCount = jobsByUserDao.count(search);
		int actualCount = totalCount;
		int pageReductionValue = 0;
		int actualPageNumber = criteria.getPagination().getPageNumber();
		int finalPageNumber = criteria.getPagination().getPageNumber();
		if (ineligibleJobIds != null && !ineligibleJobIds.isEmpty()) {
			search.addFilterNotIn("jobDetail.jobId", ineligibleJobIds);
			actualCount = totalCount - ineligibleJobIds.size();
			pageReductionValue = ineligibleJobIds.size()
					/ criteria.getPagination().getPageSize();
			if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT)
				actualPageNumber = (actualPageNumber - pageReductionValue >= 0) ? actualPageNumber
						- pageReductionValue
						: 0;
		}
		if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT) {
			finalPageNumber = actualPageNumber + 1;
		} else if (criteria.getPagination().getPaginationAction() == PaginationAction.PREV) {
			finalPageNumber = actualPageNumber - 1;
		}
		search.setPage(finalPageNumber);
		search.setMaxResults(criteria.getPagination().getPageSize());
		List<JobsByUser> jobsByUsers = jobsByUserDao.getEntities(search);
		System.out.println(jobsByUsers);
		List<JobsForCandidateResult> candidateResults = jobsByUsers.stream()
				.map(jobsByUser -> getJobCandidateResult(jobsByUser, false))
				.collect(Collectors.toList());
		candidateResults.stream()
				.forEach(
						candidateResult -> candidateResult.setUserId(userId
								.toString()));
		JobsSearchResult result = new JobsSearchResult(totalCount, actualCount,
				candidateResults, actualPageNumber, finalPageNumber);

		return result;
	}

	private JobsForCandidateResult getJobCandidateResult(JobsByUser jobsByUser,
			boolean fallback) {
		JobsForCandidateResult result = new JobsForCandidateResult();
		JobDetail job = jobsByUser.getJobDetail();
		result.setLink_to_site(job.getLinkToSite());
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
		result.setPercentageMatch(jobsByUser.getPercentageMatch());
		result.setSalaryCurrencyCode(job.getSalaryCurrencyCode());
		result.setTitle(job.getTitle());
		return result;

	}

	@Transactional
	public boolean saveComputedJobsByUser(
			final ComputedJobsByUser computedJobsByUser) {

		try {
			// Build the Objects.
			if (computedJobsByUser != null
					&& !computedJobsByUser.getJobIdToPercentageMap().isEmpty()) {
				// Create JobObject.
				UserDetail userDetail = new UserDetail();
				userDetail.setUserId(computedJobsByUser.getUserId());

				final Search search = new Search(JobsByUser.class);
				search.addFilterEqual("userDetail", userDetail);
				final List<JobsByUser> jobsByUsers = this.jobsByUserDao
						.search(search);
				final Map<Long, JobsByUser> jobsByUserMapToBeUpdated = Maps
						.newHashMap();

				final Set<Long> filteredJobIds = computedJobsByUser
						.getJobIdToPercentageMap().keySet();

				if (filteredJobIds != null && !filteredJobIds.isEmpty()
						&& jobsByUsers != null && !jobsByUsers.isEmpty()) {
					System.out.println(filteredJobIds);
					for (JobsByUser jobsByUser : jobsByUsers) {
						final JobDetail jd = jobsByUser.getJobDetail();
						if (filteredJobIds.contains(jd.getJobId())) {
							jobsByUser.setPercentageMatch(Double.valueOf(
									computedJobsByUser
											.getJobIdToPercentageMap().get(
													jd.getJobId())).intValue());
							jobsByUserMapToBeUpdated.put(jd.getJobId(),
									jobsByUser);
							computedJobsByUser.getJobIdToPercentageMap()
									.remove(jd.getJobId());
						}
					}
				}

				// Create the List of Object to Save.
				List<JobsByUser> jobsByUserList = Lists.newLinkedList();
				if (filteredJobIds != null) {
					for (Long jobId : filteredJobIds) {
						JobsByUser jobsByUser = new JobsByUser();
						// set Job detail.
						JobDetail jobDetail = new JobDetail();
						jobDetail.setJobId(jobId);
						jobsByUser.setJobDetail(jobDetail);
						// set User Detail
						jobsByUser.setUserDetail(userDetail);
						// set percentage match
						// TODO- Need to check this.
						String percentageMatch = computedJobsByUser
								.getJobIdToPercentageMap().get(jobId);
						int indexOf = percentageMatch.indexOf('.');
						jobsByUser.setPercentageMatch(Long
								.valueOf(indexOf >= 0 ? percentageMatch
										.substring(0, indexOf)
										: percentageMatch));

						// add to the List.
						jobsByUserList.add(jobsByUser);
					}
				}
				jobsByUserList.addAll(jobsByUserMapToBeUpdated.values());

				if (!jobsByUserList.isEmpty()) {
					// save the Lsit to DB.
					this.jobsByUserDao.save((JobsByUser[]) jobsByUserList
							.toArray(new JobsByUser[jobsByUserList.size()]));

					log.info("jobsByUser is saved successsfully !!!");
				}
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("Error:in saveing jobsByUser:" + ex);
			throw ex;
		}
		// Not able to Data save.
		return false;
	}

	@Transactional
	public void deleteObseleteJobsFromJobsByUser(
			final Set<Long> computedJobIds, final Long userId) {

		// Create JobObject.
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);

		final Search search = new Search(JobsByUser.class);
		search.addFilterEqual("userDetail", userDetail);
		final List<JobsByUser> jobsByUsers = this.jobsByUserDao.search(search);

		if (null != jobsByUsers) {
			List<Long> jobsByUserIds = jobsByUsers
					.stream()
					.filter(u -> !computedJobIds.contains(u.getJobDetail()
							.getJobId()))
					.map(jobsByUser -> jobsByUser.getJobByUserId())
					.collect(Collectors.toList());

			if (jobsByUserIds != null && !jobsByUserIds.isEmpty()) {
				this.jobsByUserDao.removeByIds(jobsByUserIds
						.toArray(new Serializable[0]));
			}
		}

	}
}
