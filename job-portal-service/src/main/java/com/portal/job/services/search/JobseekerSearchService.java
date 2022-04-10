package com.portal.job.services.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.portal.job.constants.JobPortalConstants;
import com.portal.job.dao.JobDetailDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.JobsByUser;
import com.portal.job.dao.model.UserByJob;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.mapper.UserDetailMapper;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobsForCandidateResult;
import com.portal.job.model.JobsSearchResult;
import com.portal.job.model.SearchCandidatesCriteria;
import com.portal.job.model.SearchCandidatesResult;
import com.portal.job.model.SearchJobForCandidatesCriteria;
import com.portal.job.model.UserDataModel;
import com.portal.job.model.UserSearchResult;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.JobRelationshipDetailService;
import com.portal.job.service.UserByJobService;
import com.portal.job.service.JobsByUserService;
import com.portal.job.service.UserDetailService;

/**
 * @author behinddwalls
 *
 */
@Service
public class JobseekerSearchService {

	private static final Logger log = LoggerFactory
			.getLogger(JobseekerSearchService.class);

	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private JobRelationshipDetailService jobRelationshipDetailService;
	@Autowired
	private JobsByUserService jobsByUserService;
	@Autowired
	private UserByJobService userByJobService;
	@Autowired
	private JobDetailDao jobDetailDao;

	public List<JobsByUser> getJobsByUserId(final Long userId) {
		return jobsByUserService.getJobsForUser(userId);
	}

	public List<UserByJob> getUsersByJobId(final Long jobId) {
		return userByJobService.getUsersForJob(jobId);
	}

	public List<SearchCandidatesResult> getUsersBySearchCriteria(
			final SearchCandidatesCriteria searchCandidatesCriteria) {

		Preconditions.checkNotNull(searchCandidatesCriteria,
				"Search criteria can not be null");
		Preconditions.checkNotNull(searchCandidatesCriteria.getPagination(),
				"Pagination can not be null");
		Preconditions.checkNotNull(searchCandidatesCriteria.getJobId(),
				"JobId can not be null");
		final List<SearchCandidatesResult> searchCandidatesResults = Lists
				.newArrayList();
		try {

			int pageSize = searchCandidatesCriteria.getPagination()
					.getPageSize();
			long jobId = searchCandidatesCriteria.getJobId();

			JobDataModel jobData = jobDetailService
					.getJobDataModelByJobId(jobId);

			// fetch job relation and extract job seeker ids for filtering
			/**
			 * we are going to SQL DB to get all jobseekers for whom recruiter
			 * has taken some action, i.e. any jobseeker who have been
			 * whitelisted,shortlisted,rejected etc, will have a job
			 * relationship entry and hence should not be shown in search resume
			 * page.
			 */
			// final Set<JobRelationshipDataModel> jobRelationshipDataModels =
			// jobRelationshipDetailService
			// .getDataModelSetForJobId(jobId);
			List<Long> ineligibleUsers = jobRelationshipDetailService
					.getIneligibleUserIdsForResumeSearchRecommendation(jobId);
			log.info("zzzzzz ineligible users are " + ineligibleUsers);

			if (!jobData.isJobProcessed() && !jobData.isJobPartiallyProcessed()) {
				log.info("zzzzzzz job search fallback");
				// return the fallback
				UserSearchResult searchResult = userDetailService
						.getFallbackUserIdsForPostedJobsPaginated(jobData,
								searchCandidatesCriteria, ineligibleUsers);
				setPaginationOptions(searchCandidatesCriteria,
						searchResult.getActualCount(),
						searchResult.getFinalPageNumber(), pageSize);
				searchResult.getResult().stream()
						.forEach(r -> r.setJobId(jobId));
				return searchResult.getResult();
			}

			UserSearchResult searchResult = userByJobService
					.getProcessedUsersForPostedJobs(searchCandidatesCriteria,
							ineligibleUsers);
			setPaginationOptions(searchCandidatesCriteria,
					searchResult.getActualCount(),
					searchResult.getFinalPageNumber(), pageSize);
			return searchResult.getResult();
		} catch (Exception e) {
			log.error("Jobseeker search failed for for criteria "
					+ searchCandidatesCriteria, e);
		}
		return searchCandidatesResults;

	}

	private void setPaginationOptions(
			SearchCandidatesCriteria searchCandidatesCriteria,
			long actualTotalCount, int finalPageNumber, int pageSize) {
		searchCandidatesCriteria.getPagination().setTotalResultCount(
				actualTotalCount);
		searchCandidatesCriteria.getPagination().setPageNumber(finalPageNumber);
		searchCandidatesCriteria.getPagination().setTotalPageCount(
				actualTotalCount / pageSize);

		if (finalPageNumber > 0) {
			searchCandidatesCriteria.getPagination().setShowPrevButton(true);
		}
		if (actualTotalCount > (finalPageNumber + 1) * pageSize) {
			searchCandidatesCriteria.getPagination().setShowNextButton(true);

		}
	}

	/**
	 * 
	 * @param searchCriteria
	 * @param userId
	 * @return It fetch the best Job match that is already computed from the
	 *         Engine. It filters out those Jobs form this match for which user
	 *         has been considered already.
	 */
	public List<JobsForCandidateResult> getPreferredJobsForJobseeker(
			final Long userId,
			final SearchJobForCandidatesCriteria searchCriteria) {

		Preconditions.checkNotNull(searchCriteria,
				"Search criteria can not be null");
		Preconditions.checkNotNull(searchCriteria.getPagination(),
				"Pagination can not be null");
		Preconditions.checkNotNull(userId, "jokbseekerId can not be null");
		final List<JobsForCandidateResult> jobModelResults = Lists
				.newArrayList();
		try {

			int pageSize = searchCriteria.getPagination().getPageSize();

			UserDetail user = userDetailService.getUserDetail(userId);
			// fetch job relation and extract job seeker ids for filtering
			/**
			 * we are going to SQL DB to get all jobseekers for whom recruiter
			 * has taken some action, i.e. any jobseeker who have been
			 * whitelisted,shortlisted,rejected etc, will have a job
			 * relationship entry and hence should not be shown in search resume
			 * page.
			 */
			final List<Long> ineligibleJobIds = jobRelationshipDetailService
					.getIneligibleJobIdsForUsersJobRecommendation(userId);

			if (!UserDetailMapper.isUserProcessed(user)
					&& !UserDetailMapper.isUserPartiallyProcessed(user)) {
				log.info("zzzzz about to call fallback val="
						+ user.getProcessedState());
				JobsSearchResult jobSearchFallbackResult = jobDetailService
						.getFallBackJobsForUser(userId, searchCriteria,
								ineligibleJobIds);
				setPaginationOptionsForJobResults(searchCriteria,
						jobSearchFallbackResult.getActualCount(),
						jobSearchFallbackResult.getFinalPageNumber(), pageSize);
				return jobSearchFallbackResult.getJobsForUser();
			}

			JobsSearchResult searchResult = jobsByUserService
					.getProcessedJobsForUser(searchCriteria, userId,
							ineligibleJobIds);

			setPaginationOptionsForJobResults(searchCriteria,
					searchResult.getActualCount(),
					searchResult.getFinalPageNumber(), pageSize);
			return searchResult.getJobsForUser();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Jobseeker search failed for for criteria "
					+ searchCriteria, e);
		}
		return jobModelResults;

	}

	private void setPaginationOptionsForJobResults(
			SearchJobForCandidatesCriteria searchCriteria,
			long actualTotalCount, int finalPageNumber, int pageSize) {
		searchCriteria.getPagination().setTotalResultCount(actualTotalCount);
		searchCriteria.getPagination().setPageNumber(finalPageNumber);
		if (actualTotalCount % pageSize == 0)
			searchCriteria.getPagination().setTotalPageCount(
					actualTotalCount / pageSize);
		else
			searchCriteria.getPagination().setTotalPageCount(
					actualTotalCount / pageSize + 1);

		if (finalPageNumber > 0) {
			searchCriteria.getPagination().setShowPrevButton(true);
		}
		if (actualTotalCount > (finalPageNumber + 1) * pageSize) {
			searchCriteria.getPagination().setShowNextButton(true);

		}
	}

	/*
	 * @Transactional public List<UserDataModel> getUserDetailsForPostedJob(
	 * final JobDataModel jobDataModel) { String companyName =
	 * jobDataModel.getOrganisationName(); int experience =
	 * Integer.valueOf(jobDataModel.getJobExperience()); String skills[] =
	 * jobDataModel.getSkills().split(","); // get userIds by comapny and
	 * experience Set<Long> userIdSet = userExperienceDetailsService
	 * .getCurrentUserExperienceDataModel(companyName, experience); // filter
	 * userIds by skill userIdSet =
	 * userDetailService.getUserIdsByFilterInSkillNameAndUserId( new
	 * HashSet<String>(Arrays.asList(skills)), userIdSet);
	 * 
	 * return userDetailService.getUserDataModelByIds(new ArrayList<Long>(
	 * userIdSet)); }
	 */

	public UserByJob getUser(final @NotNull Long jobId,
			final @NotNull Long userId) {
		return userByJobService.getUser(userId, jobId);
	}

	/**
	 * 
	 * @param userDataModel
	 * @param pageOffset
	 * @param pageNumber
	 * @return
	 * 
	 *         This Function is used to fetch the 'JOB DATA SET' on the Basis of
	 *         Some 'Default suitable' Criteria of 'JobSeeker info' to show him
	 *         the 'Preferred Job' list on the 'JObSeeker Dashboard'. This Set
	 *         <JobdDetail> is being sent to 'Engine' for processing. Engine
	 *         performs some intelligence to show the 'best Job list' to
	 *         jobseeker dashboard.
	 */
	@SuppressWarnings("static-access")
	@Transactional
	public Set<JobDetail> getFilteredJobsForEngineComputation(
			final UserDataModel userDataModel, final int pageOffset,
			final int pageNumber) {
		log.info("## zzzzzz UserId :"
				+ userDataModel.getUserBasicDataModel().getUserId());
		// Call the 'JobRelationShipDetail Table to filter those <job data set>'
		// which is having 'JobSeekerId' entry in the Table. This indicates that
		// Jobseeker
		// has already performed some operation for that Job and don't need to
		// show 'Same Job'
		// Again.
		final List<Long> ineligibleJobIds = jobRelationshipDetailService
				.getIneligibleJobIdsForUsersJobRecommendation(userDataModel
						.getUserBasicDataModel().getUserId());
		UserDetail userDetail = userDetailService.getUserDetail(userDataModel
				.getUserBasicDataModel().getUserId());
		/*
		 * Search jobsSearchForUser =
		 * getSearchCriteriaToFetchJobsForEngineComputation(userDetail); if
		 * (null != ineligibleJobIds && !ineligibleJobIds.isEmpty()) {
		 * jobsSearchForUser.addFilterNotIn("jobId", ineligibleJobIds); }
		 */
		Search jobsSearchForUser = new Search();
		if (userDataModel.getUserBasicDataModel().getLatestCompanyName() != null) {
			String userComapnyName = userDataModel.getUserBasicDataModel()
					.getLatestCompanyName().replaceAll("[^A-Za-z0-9]", "%");
			jobsSearchForUser.addFilter(Filter.not(Filter.ilike(
					"organisationName", "%" + userComapnyName.toLowerCase()
							+ "%")));
		}

		if (userDataModel.getUserBasicDataModel().getJobFunction() != null
				&& userDataModel.getUserBasicDataModel().getJobFunction()
						.equals("Research and Analytics")) {
			jobsSearchForUser.addFilterEqual("jobFunction", userDataModel
					.getUserBasicDataModel().getJobFunction());

		} else {
			List<Filter> skillFilters = new ArrayList<Filter>();

			final List<String> skills = StringUtils.isEmpty(userDataModel
					.getUserBasicDataModel().getSkills()) ? new ArrayList<String>()
					: Arrays.asList(userDataModel.getUserBasicDataModel()
							.getSkills().split(","));

			if (null != skills && !skills.isEmpty()) {
				skills.stream().forEach(
						skill -> {
							Filter f = Filter.ilike("jobSkills",
									"%," + skill.toLowerCase() + ",%");
							skillFilters.add(f);
						});
				jobsSearchForUser.addFilterOr(skillFilters
						.toArray(new Filter[0]));

			}
		}

		if (userDataModel.getUserBasicDataModel().getPastExperienceMonths() != null) {
			log.info("zzzzzzzzzzz adding experience filter");
			jobsSearchForUser.addFilterLessOrEqual("jobExperiance",
					userDataModel.getUserBasicDataModel()
							.getPastExperienceMonths());
			jobsSearchForUser.addFilterGreaterOrEqual("jobExperiance",
					userDataModel.getUserBasicDataModel()
							.getPastExperienceMonths() - 36);
			log.info("zzzzzzzz experience filter = " + jobsSearchForUser);
		}else {
			jobsSearchForUser.addFilterLessOrEqual("jobExperiance",0);
		}
		try {
			if (userDataModel.getUserBasicDataModel().getCtc() != null) {
				int ctc = Integer.parseInt(userDataModel
						.getUserBasicDataModel().getCtc());
				log.info("zzzzzzzzzzz adding salary filter");
				Filter rangeSalaryFilter = Filter.greaterOrEqual("maxSalary",
						ctc).and(Filter.lessOrEqual("minSalary", ctc * 2));
				Filter missingSalaryFilter = Filter.equal("maxSalary", 0);
				jobsSearchForUser.addFilter(Filter.or(new Filter[] {
						rangeSalaryFilter, missingSalaryFilter }));

				log.info("zzzzzzzz salary filter = " + jobsSearchForUser);
			}
		} catch (Exception e) {
			log.error("failed salary filter", e);
		}
		Filter locationFilter=Filter.like("locationDetail", "Delhi").or(Filter.like("locationDetail", "Delhi/NCR(National Capital Region)")).or(Filter.like("locationDetail", "Bengaluru/Bangalore")).or(Filter.like("locationDetail", "Mumbai")).or(Filter.like("locationDetail", "Chennai")).or(Filter.like("locationDetail", "Pune")).or(Filter.like("locationDetail", "Hyderabad/Secunderabad")).or(Filter.like("locationDetail", "Ahmedabad"));
		jobsSearchForUser.addFilter(locationFilter);
		log.info("zzzzzzzz search = " + jobsSearchForUser);
		jobsSearchForUser.setPage(pageOffset);
		jobsSearchForUser.setMaxResults(pageNumber);
		List<JobDetail> jobsForUser = jobDetailDao.search(jobsSearchForUser);
		log.info("zzzzzzz fallback jobsForUSer" + jobsForUser);
		return new HashSet<JobDetail>(jobsForUser);
	}

	/*
	 * This 'Search' object is used for having the filtering criteria to fetch
	 * the 'Selective Jobs set' which would be sent to 'Engine for Computation
	 * of' 'Preferred Jobs Set' for JobSeeker that We want to him at his 'JOB
	 * DASHBOARD'.
	 */
	private Search getSearchCriteriaToFetchJobsForEngineComputation(
			UserDetail user) {
		Search search = new Search(JobDetail.class);
		log.info("### Get JOBS for Jobseeker which will be sent to Engine");
		Filter jobFunctionFilter = null;
		if (StringUtils.isEmpty(user.getJobFunction())) {
			// TODO- Uncomment after testing.
			/*
			 * throw new RuntimeException("JobFunction not present for User " +
			 * user.getUserId());
			 */
			jobFunctionFilter = Filter.equal("jobFunction",
					user.getJobFunction());
		}
		// uncomment after adding filed in db
		if (!StringUtils.isEmpty(user.getIndustryName())) {
			Filter industryFilter = Filter.equal("industryName",
					user.getIndustryName());
			search.addFilterOr(jobFunctionFilter, industryFilter);
		} else {
			search.addFilter(jobFunctionFilter);
		}
		if (user.getCtc() != null) {
			search.addFilterGreaterOrEqual("maxSalary", user.getCtc());
		}
		List<Filter> skillFilterList = new ArrayList<Filter>();
		List<String> userSkillList = Lists.newArrayList();
		if (null != user.getSkills()) {
			userSkillList = Arrays.asList(user.getSkills().split(
					JobPortalConstants.SKILLS_DELEMITER));
			userSkillList.stream().forEach(skill -> {
				Filter f = Filter.ilike("jobSkills", "%" + skill + "%");
				skillFilterList.add(f);
			});
			search.addFilterOr(skillFilterList.toArray(new Filter[0]));
		}

		// TODO- Need to add the Filtering for 'UserExperiance data' also.

		return search;
	}
}
