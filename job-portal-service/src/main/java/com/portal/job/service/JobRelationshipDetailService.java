package com.portal.job.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.portal.job.constants.DateConstants;
import com.portal.job.dao.BaseSearchBuilder;
import com.portal.job.dao.JobRelationshipDetailsDao;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.JobRelationshipDetail;
import com.portal.job.dao.model.JobsByUser;
import com.portal.job.dao.model.UserByJob;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.enums.PaginationAction;
import com.portal.job.enums.SortingCriteria;
import com.portal.job.mapper.JobDetailMapper;
import com.portal.job.mapper.JobRelationshipDetailMapper;
import com.portal.job.mapper.UserDetailMapper;
import com.portal.job.model.CandidatesFilterCriteria;
import com.portal.job.model.JobFilterCriteria;
import com.portal.job.model.JobRelationshipAndUserDetailWrapper;
import com.portal.job.model.JobRelationshipDataModel;
import com.portal.job.model.JobSeekerJobsDataModel;
import com.portal.job.model.JobSeekerJobsResultDataModel;
import com.portal.job.model.MyCandidatesResponseDataModel;
import com.portal.job.model.Pagination;
import com.portal.job.utils.DateUtility;

@Service
public class JobRelationshipDetailService {
	private static final Logger log = LoggerFactory.getLogger(JobRelationshipDetailService.class);
	@Autowired
	private JobRelationshipDetailsDao jobRelationshipDetailsDao;
	@Autowired
	private JobRelationshipDetailMapper jobRelationshipDetailMapper;
	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private JobDetailMapper jobDetailMapper;
	@Autowired
	private UserDetailMapper userDetailMapper;

	@Autowired
	private UserByJobService userByJobService;
	
	@Autowired
	private JobsByUserService jobsByUserService;

	@SuppressWarnings("serial")
	@Transactional
	public JobRelationshipDataModel getJobRelationDataModel(final Long jobId, final Long jobseekerId) {
		final List<JobRelationshipDetail> jobRelationshipDetails = jobRelationshipDetailsDao
				.getEntitiesByPropertyValue(new HashMap<String, Object>() {
					{
						put("jobDetail.jobId", jobId);
						put("userDetailByJobseekerId.userId", jobseekerId);
					}
				}, JobRelationshipDetail.class);

		final Set<JobRelationshipDataModel> jobRelationshipDataModels = jobRelationshipDetailMapper
				.getDataModelsFromEntities(jobRelationshipDetails);
		return (null == jobRelationshipDataModels || jobRelationshipDataModels.isEmpty()) ? null
				: jobRelationshipDataModels.iterator().next();
	}

	// Read query.
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<Long> getIneligibleJobIdsForUsersJobRecommendation(final Long userId) {
		Search search = new Search(JobRelationshipDetail.class);
		search.addFilterEqual("userDetailByJobseekerId.userId", userId);
		Filter applicationStatusMaybeWishlistedStateFilter = Filter.equal("jobApplicationStatus",
				JobApplicationStatus.MAYBEWISHLISTED.name());
		Filter isInJobSeekerWishlistFilter = Filter.equal("isInJobseekerWishlist", (byte) 0);
		Filter eligiblieUserFilter = Filter.and(applicationStatusMaybeWishlistedStateFilter,
				isInJobSeekerWishlistFilter);
		Filter ineligibleUserFilter = Filter.not(eligiblieUserFilter);
		search.addFilter(ineligibleUserFilter);
		log.info("zzzzzzzz ineligible user query: " + search);
		List<JobRelationshipDetail> jobRelationshipDetailList = jobRelationshipDetailsDao.getEntities(search);
		return jobRelationshipDetailList.stream()
				.map(jobRelation -> jobRelation.getJobDetail().getJobId()).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public Set<Long> getDataModelSetForExpertId(final Long expertId) {
		final Set<Long> expertsIds = Sets.newHashSet();
		List<JobRelationshipDetail> jobRelationshipDetailList = this.jobRelationshipDetailsDao
				.getEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
					private static final long serialVersionUID = 1L;

					{
						put("userDetailByExpertId", new UserDetail() {
							private static final long serialVersionUID = 1L;

							{
								setUserId(expertId);
							}
						});
					}
				}, JobRelationshipDetail.class);
		for (JobRelationshipDetail detail : jobRelationshipDetailList) {
			expertsIds.add(detail.getUserDetailByJobseekerId().getUserId());
		}

		return expertsIds;
	}

	/**
	 * 
	 * @param jobId
	 * @return
	 */
	@Transactional
	public Set<JobRelationshipDataModel> getDataModelSetForJobId(final Long jobId) {
		return this.jobRelationshipDetailMapper.getDataModelsFromEntities(
				(this.jobRelationshipDetailsDao.getEntitiesByPropertyValue(new HashMap<String, JobDetail>() {
					private static final long serialVersionUID = 1L;

					{
						put("jobDetail", new JobDetail() {
							private static final long serialVersionUID = 1L;

							{
								setJobId(jobId);
							}
						});
					}
				}, JobRelationshipDetail.class)));
	}

	@Transactional
	public List<Long> getIneligibleUserIdsForResumeSearchRecommendation(final Long jobId) {
		Search search = new Search(JobRelationshipDetail.class);
		search.addFilterEqual("jobDetail.jobId", jobId);
		Filter applicationStatusMaybeWishlistedStateFilter = Filter.equal("jobApplicationStatus",
				JobApplicationStatus.MAYBEWISHLISTED.name());
		Filter isInRecruiterWishlistFilter = Filter.equal("isInRecruiterWishlist", (byte) 0);
		Filter eligiblieUserFilter = Filter.and(applicationStatusMaybeWishlistedStateFilter,
				isInRecruiterWishlistFilter);
		Filter ineligibleUserFilter = Filter.not(eligiblieUserFilter);
		search.addFilter(ineligibleUserFilter);
		log.info("zzzzzzzz ineligible user query: " + search);
		List<JobRelationshipDetail> jobRelationshipDetailList = jobRelationshipDetailsDao.getEntities(search);
		return jobRelationshipDetailList.stream()
				.map(jobRelation -> jobRelation.getUserDetailByJobseekerId().getUserId()).collect(Collectors.toList());

	}

	/**
	 * 
	 * @return
	 */
	@Transactional
	public Set<JobRelationshipDataModel> getAllJobRelationshipDataModel() {
		return this.jobRelationshipDetailMapper.getDataModelsFromEntities(this.jobRelationshipDetailsDao.findAll());
	}

	/**
	 * 
	 * @param jobId
	 * @return This filters according to the Application Status.
	 */
	@Transactional
	public Set<JobRelationshipAndUserDetailWrapper> getDataModelSetForJobApplicationStatus(final Long jobId,
			final JobApplicationStatus jobApplicationStatus) {
		List<JobRelationshipDetail> jobRelationshipDetails = (this.jobRelationshipDetailsDao
				.getEntitiesByPropertyValue(new HashMap<String, Object>() {
					private static final long serialVersionUID = 1L;

					{
						put("jobDetail.jobId", jobId);
						put("jobApplicationStatus", jobApplicationStatus.getStatus());

					}
				}, JobRelationshipDetail.class));

		// JobRelationshipAndUserDetailWrapper
		return getJobRelationshipAndUserDetailWrapper(jobRelationshipDetails);
	}

	/**
	 * 
	 * @param userId
	 * @param recruiterApplicationStatus
	 * @return
	 */
	@Transactional
	public MyCandidatesResponseDataModel getMyCandidatesResponseDataModelForRecruiter(final Long recruiterId,
			final CandidatesFilterCriteria searchCriteria) {
		MyCandidatesResponseDataModel responseDataModel = new MyCandidatesResponseDataModel();
		responseDataModel.setSearchCandidateResults(new ArrayList<JobRelationshipAndUserDetailWrapper>());
		responseDataModel.setPagination(new Pagination());

		try {
			int pageNumber = 0;
			int finalPageNumber = 0;
			if (PaginationAction.NEXT.equals(searchCriteria.getPagination().getPaginationAction())) {
				pageNumber = searchCriteria.getPagination().getPageNumber() + 1;
				finalPageNumber = pageNumber;
			} else if (PaginationAction.PREV.equals(searchCriteria.getPagination().getPaginationAction())) {
				pageNumber = searchCriteria.getPagination().getPageNumber() - 1;
				finalPageNumber = pageNumber;
			}

			int pageSize = searchCriteria.getPagination().getPageSize();

			// create BaseSearchBuilder.
			BaseSearchBuilder baseSearchBuilder = new BaseSearchBuilder()
					.searchEntitiesByPropertyValue(new HashMap<String, Object>() {
						private static final long serialVersionUID = 1L;

						{
							put("userDetailByRecruiterId.userId", recruiterId);
							// Getting all shortlisted users
							// for
							// a particular Job by
							// recruiter.
							/*
							 * This is support the use cases (Future scope)
							 * where we want 'Recruiter' to give flexibility
							 * about finding the 'Jobseeker Action' when we had
							 * sent an invitation to JobSeeker after
							 * 'Shortlisting him.'. Currently this gives the
							 * only 'JOBSEEKER candidate' with application
							 * status as [APPLIED] only.
							 */
							if (JobApplicationStatus.APPLIED.getStatus().equals(searchCriteria.getCandidateStatus())) {
								put("jobSeekerApplicationStatus", JobApplicationStatus.APPLIED.getStatus());
							}
							/*
							 * Currently values coming form the Recruiter Input
							 * request is only for [SHORTLISTED , REJECTED]
							 */
							else if (JobApplicationStatus.REJECTED.getStatus()
									.equals(searchCriteria.getCandidateStatus())) {
								put("jobApplicationStatus", searchCriteria.getCandidateStatus());
								//put("isInRecruiterWishlist", (byte) 0);
							} else {
								put("jobApplicationStatus", searchCriteria.getCandidateStatus());
							}
						}
					});
			// provide the sorting criteria.
			// fetch the data
			baseSearchBuilder = getBaseSearchBuilderWithJobSortingCriteriaForCandidates(baseSearchBuilder,
					searchCriteria.getCandidateSortCriteria());

			List<JobRelationshipDetail> jobRelationshipDetails = this.jobRelationshipDetailsDao
					.getEntities(baseSearchBuilder.buildSearchObject(), finalPageNumber, pageSize);
			// find the total number of possible result.
			long totalResultCount = this.jobRelationshipDetailsDao
					.getEntitiesCount(baseSearchBuilder.buildSearchObject());

			searchCriteria.getPagination().setTotalResultCount(totalResultCount);
			searchCriteria.getPagination().setPageNumber(finalPageNumber);
			searchCriteria.getPagination().setTotalPageCount(totalResultCount / pageSize);

			if (pageNumber > 0) {
				searchCriteria.getPagination().setShowPrevButton(true);
			}
			if (totalResultCount > (pageNumber + 1) * pageSize) {
				searchCriteria.getPagination().setShowNextButton(true);

			}
			// set the values
			responseDataModel.setSearchCandidateResults(new ArrayList<JobRelationshipAndUserDetailWrapper>(
					getJobRelationshipAndUserDetailWrapper(jobRelationshipDetails)));
			responseDataModel.setPagination(searchCriteria.getPagination());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return responseDataModel;
	}

	/**
	 * 
	 * @param jobId
	 * @return This gives the all Users who are whitelisted by the Recruiter.
	 * @throws ParseException
	 */
	@Transactional
	public MyCandidatesResponseDataModel getAllUsersFromRecruiterWishlist(final Long recruiterId,
			final CandidatesFilterCriteria searchCriteria) throws ParseException {
		MyCandidatesResponseDataModel responseDataModel = new MyCandidatesResponseDataModel();
		responseDataModel.setSearchCandidateResults(new ArrayList<JobRelationshipAndUserDetailWrapper>());
		responseDataModel.setPagination(new Pagination());
		try {
			int pageNumber = 0;
			int finalPageNumber = 0;

			if (PaginationAction.NEXT.equals(searchCriteria.getPagination().getPaginationAction())) {
				pageNumber = searchCriteria.getPagination().getPageNumber() + 1;
				finalPageNumber = pageNumber;
			} else if (PaginationAction.PREV.equals(searchCriteria.getPagination().getPaginationAction())) {
				pageNumber = searchCriteria.getPagination().getPageNumber() - 1;
				finalPageNumber = pageNumber;
			}

			int pageSize = searchCriteria.getPagination().getPageSize();

			// create BaseSearchBuilder.
			BaseSearchBuilder baseSearchBuilder = new BaseSearchBuilder()
					.searchEntitiesByPropertyValue(new HashMap<String, Object>() {
						private static final long serialVersionUID = 1L;

						{
							put("userDetailByRecruiterId.userId", recruiterId);
							// Getting all users whitelisted
							// for
							// a particular Job by
							// recruiter.
							put("isInRecruiterWishlist", 1);
						}
					});
			baseSearchBuilder = getBaseSearchBuilderWithJobSortingCriteria(baseSearchBuilder,
					searchCriteria.getCandidateSortCriteria());
			List<JobRelationshipDetail> jobRelationshipDetails = this.jobRelationshipDetailsDao
					.getEntities(baseSearchBuilder.buildSearchObject(), finalPageNumber, pageSize);

			// find the total number of possible result.
			long totalResultCount = this.jobRelationshipDetailsDao
					.getEntitiesCount(baseSearchBuilder.buildSearchObject());

			searchCriteria.getPagination().setTotalResultCount(totalResultCount);
			searchCriteria.getPagination().setPageNumber(finalPageNumber);
			searchCriteria.getPagination().setTotalPageCount(totalResultCount / pageSize);

			if (pageNumber > 0) {
				searchCriteria.getPagination().setShowPrevButton(true);
			}
			if (totalResultCount > (pageNumber + 1) * pageSize) {
				searchCriteria.getPagination().setShowNextButton(true);

			}
			// set the values
			responseDataModel.setSearchCandidateResults(new ArrayList<JobRelationshipAndUserDetailWrapper>(
					getJobRelationshipAndUserDetailWrapper(jobRelationshipDetails)));
			responseDataModel.setPagination(searchCriteria.getPagination());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return responseDataModel;
	}

	// Add or Update queries.
	/**
	 * 
	 * @param dataModel
	 * @return TODO- Need to update it accordingly.
	 * @throws Exception
	 */
	@Transactional
	public boolean addOrUpdate(final JobRelationshipDataModel dataModel) throws Exception {

		return this.jobRelationshipDetailsDao.save(this.jobRelationshipDetailMapper.getEntityFromDataModel(dataModel));
	}

	/**
	 * 
	 * @param jobseekerId
	 * @param jobFilterCriteria
	 * @return
	 * @throws ParseException
	 *             This function basically find the all Jobs for Jobseeker for
	 *             which jobseeker has applied (performed some action). Function
	 *             will first fetch all the entries present in the 'JobRelation
	 *             Table' and then applies the filter passed as input parameter.
	 */
	@Transactional
	public JobSeekerJobsResultDataModel getAllJobsForJobseeker(final Long jobseekerId,
			final JobFilterCriteria jobFilterCriteria) throws ParseException {
		JobSeekerJobsResultDataModel responseDataModel = new JobSeekerJobsResultDataModel();
		responseDataModel.setJobSeekerJobsDataModelList(new ArrayList<JobSeekerJobsDataModel>());
		responseDataModel.setPagination(new Pagination());
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
			BaseSearchBuilder baseSearchBuilder = new BaseSearchBuilder()
					.searchEntitiesByPropertyValue(new HashMap<String, Object>() {
						private static final long serialVersionUID = 1L;

						{
							put("userDetailByJobseekerId.userId", jobseekerId);
							if (JobApplicationStatus.APPLIED.getStatus().equals(jobFilterCriteria.getJobStatus())
									|| JobApplicationStatus.REJECTED.getStatus()
											.equals(jobFilterCriteria.getJobStatus())) {
								put("jobSeekerApplicationStatus", jobFilterCriteria.getJobStatus());
							}
							// Getting all whitelisted Job
							// a particular jobseeker.
							if (JobApplicationStatus.WISHLISTED.getStatus().equals(jobFilterCriteria.getJobStatus())) {
								put("isInJobseekerWishlist", 1);
							}
						}
					});

			// sort the Jobs according to the Search criteria present in the
			// input.
			baseSearchBuilder = getBaseSearchBuilderWithJobSortingCriteria(baseSearchBuilder,
					jobFilterCriteria.getJobSortCriteria());

			List<JobRelationshipDetail> jobRelationshipDetails = this.jobRelationshipDetailsDao
					.getEntities(baseSearchBuilder.buildSearchObject(), finalPageNumber, pageSize);

			// find the total number of possible result.
			long totalResultCount = this.jobRelationshipDetailsDao
					.getEntitiesCount(baseSearchBuilder.buildSearchObject());

			jobFilterCriteria.getPagination().setTotalResultCount(totalResultCount);
			jobFilterCriteria.getPagination().setPageNumber(finalPageNumber);
			jobFilterCriteria.getPagination().setTotalPageCount(totalResultCount / pageSize);

			if (pageNumber > 0) {
				jobFilterCriteria.getPagination().setShowPrevButton(true);
			}
			if (totalResultCount > (pageNumber + 1) * pageSize) {
				jobFilterCriteria.getPagination().setShowNextButton(true);

			}
			// set the values
			responseDataModel.setJobSeekerJobsDataModelList(
					new ArrayList<JobSeekerJobsDataModel>(getJobSeekerJobsDataModelSet(jobRelationshipDetails)));
			responseDataModel.setPagination(jobFilterCriteria.getPagination());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return responseDataModel;
	}

	// Deletion query

	// private methods.
	private LinkedHashSet<JobRelationshipAndUserDetailWrapper> getJobRelationshipAndUserDetailWrapper(
			final List<JobRelationshipDetail> jobRelationshipDetails) {
		// now set the JobRelationshipAndUserDetailWrapper
		// JobRelationshipAndUserDetailWrapper
		LinkedHashSet<JobRelationshipAndUserDetailWrapper> jobRelationshipAndUserDetailWrappers = new LinkedHashSet<JobRelationshipAndUserDetailWrapper>();
		for (JobRelationshipDetail jobRelationshipDetail : jobRelationshipDetails) {
			JobRelationshipAndUserDetailWrapper wrapper = new JobRelationshipAndUserDetailWrapper();
			UserDetail userDetail = jobRelationshipDetail.getUserDetailByJobseekerId();
			JobDetail jobDetail = jobRelationshipDetail.getJobDetail();
			// set values related to JobRelationship
			wrapper.setWishlisted(jobRelationshipDetail.getIsInRecruiterWishlist());
			wrapper.setJobId(String.valueOf(jobDetail.getJobId()));
			wrapper.setJobTitle(jobDetail.getTitle());
			wrapper.setJobRelationshipId(String.valueOf(jobRelationshipDetail.getJobRelationshipId()));
			wrapper.setJobApplicationStatus(jobRelationshipDetail.getJobApplicationStatus());
			wrapper.setJobseekerId(String.valueOf(userDetail.getUserId()));
			// set values related to user detail
			wrapper.setFirstName(userDetail.getFirstName());
			wrapper.setUserResumeLink(userDetail.getUserResumeLink());
			wrapper.setLastName(userDetail.getLastName());
			wrapper.setPastExperienceMonths(userDetail.getPastExperienceMonths());
			wrapper.setMobileNumber(userDetail.getMobileNumber());
			wrapper.setProfileImageUrl(userDetail.getProfileImageUrl());
			wrapper.setAddress(userDetail.getAddress());
			wrapper.setJobFunction(userDetail.getJobFunction());
			wrapper.setProfileHeadline(userDetail.getProfileHeadline());
			wrapper.setCompany(userDetail.getLatestCompanyName());
			wrapper.setJobCreateDate(
					DateUtility.getStringFromDate(jobDetail.getCreateDate(), DateConstants.DD_MM_YYYY_FORMAT));

			// get percentage match
			UserByJob userbyJob = this.userByJobService.getUser(userDetail.getUserId(), jobDetail.getJobId());
			wrapper.setPercentageMatch(String.valueOf(userbyJob.getPercentageMatch()));

			// Add in the set
			jobRelationshipAndUserDetailWrappers.add(wrapper);
		}
		return jobRelationshipAndUserDetailWrappers;
	}

	/*
	 * 
	 */
	private LinkedHashSet<JobSeekerJobsDataModel> getJobSeekerJobsDataModelSet(
			final List<JobRelationshipDetail> jobRelationshipDetails) {
		LinkedHashSet<JobSeekerJobsDataModel> JobSeekerJobsDataModelSet = new LinkedHashSet<JobSeekerJobsDataModel>();
		for (JobRelationshipDetail jobRelationshipDetail : jobRelationshipDetails) {
			JobSeekerJobsDataModel jobSeekerJobsDataModel = new JobSeekerJobsDataModel();
			jobSeekerJobsDataModel.setJobId(String.valueOf(jobRelationshipDetail.getJobDetail().getJobId()));
			jobSeekerJobsDataModel.setLink_to_site(jobRelationshipDetail.getJobDetail().getLinkToSite());
			jobSeekerJobsDataModel.setJobRelationshipId(String.valueOf(jobRelationshipDetail.getJobRelationshipId()));
			jobSeekerJobsDataModel.setJobOrganisationName(jobRelationshipDetail.getJobDetail().getOrganisationName());
			jobSeekerJobsDataModel.setJobTitle(jobRelationshipDetail.getJobDetail().getTitle());
			jobSeekerJobsDataModel.setJobLocation(jobRelationshipDetail.getJobDetail().getLocationDetail());
			jobSeekerJobsDataModel.setJobWishlistedByUser(
					jobRelationshipDetail.getIsInJobseekerWishlist() == (byte) 1 ? true : false);
			jobSeekerJobsDataModel.setJobseekerJobApplicationStatusApplied(JobApplicationStatus.APPLIED.getStatus()
					.equals(jobRelationshipDetail.getJobSeekerApplicationStatus()));
			jobSeekerJobsDataModel.setJobseekerJobApplicationStatusRejected(JobApplicationStatus.REJECTED.getStatus()
					.equals(jobRelationshipDetail.getJobSeekerApplicationStatus()));
			// get percentage match
			UserDetail userDetail = jobRelationshipDetail.getUserDetailByJobseekerId();
			JobsByUser jobsbyUser = this.jobsByUserService.getUser(userDetail.getUserId(), jobRelationshipDetail.getJobDetail().getJobId());
			if(jobsbyUser==null)
			{
				jobSeekerJobsDataModel.setFallback(true);
				jobSeekerJobsDataModel.setPercentageMatch(null);
			}
			else
			{
				jobSeekerJobsDataModel.setFallback(false);
				jobSeekerJobsDataModel.setPercentageMatch(String.valueOf(jobsbyUser.getPercentageMatch()));
			}
			// adding model to set.
			JobSeekerJobsDataModelSet.add(jobSeekerJobsDataModel);
		}
		return JobSeekerJobsDataModelSet;
	}

	// private
	private BaseSearchBuilder getBaseSearchBuilderWithJobSortingCriteria(BaseSearchBuilder baseSearchBuilder,
			String jobSerachCriteria) {
		if (SortingCriteria.OLDEST_JOBCREATED.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortAscEntitiesByPropertyValue("jobDetail.createDate");
		} else if (SortingCriteria.NAME_A_TO_Z.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortAscEntitiesByPropertyValue("jobDetail.title");
		} else if (SortingCriteria.NAME_Z_TO_A.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortDescEntitiesByPropertyValue("jobDetail.title");
		} else if (SortingCriteria.LOCAL_A_TO_Z.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortAscEntitiesByPropertyValue("jobDetail.locationDetail");
		} else if (SortingCriteria.LOCAL_Z_TO_A.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortDescEntitiesByPropertyValue("jobDetail.locationDetail");
		} else {
			// Default sort with the 'job_create date'
			baseSearchBuilder.sortDescEntitiesByPropertyValue("jobDetail.createDate");
		}
		return baseSearchBuilder;
	}

	// private
	private BaseSearchBuilder getBaseSearchBuilderWithJobSortingCriteriaForCandidates(
			BaseSearchBuilder baseSearchBuilder, String jobSerachCriteria) {
		if (SortingCriteria.OLDEST_JOBCREATED.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortAscEntitiesByPropertyValue("createDate");
		} else if (SortingCriteria.NAME_A_TO_Z.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortAscEntitiesByPropertyValue("userDetailByJobseekerId.firstName");
		} else if (SortingCriteria.NAME_Z_TO_A.equals(SortingCriteria.getSortingCriteria(jobSerachCriteria))) {
			baseSearchBuilder.sortDescEntitiesByPropertyValue("userDetailByJobseekerId.firstName");
		} else {
			// Default sort with the 'job_create date'
			baseSearchBuilder.sortDescEntitiesByPropertyValue("createDate");
		}
		return baseSearchBuilder;
	}
}
