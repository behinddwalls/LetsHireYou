package com.portal.job.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.portal.job.dao.UserDetailDao;
import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserEducationDetail;
import com.portal.job.dao.model.UserExperinceDetail;
import com.portal.job.enums.PaginationAction;
import com.portal.job.enums.ProcessingState;
import com.portal.job.mapper.UserDetailMapper;
import com.portal.job.mapper.UserSkillDetailMapper;
import com.portal.job.model.ExperienceProcessedData;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.ProfileHeaderDataModel;
import com.portal.job.model.RecruiterProfileDataModel;
import com.portal.job.model.SearchCandidatesCriteria;
import com.portal.job.model.SearchCandidatesResult;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.model.UserSearchResult;

/**
 * @author behinddwalls
 *
 */
@Service
public class UserDetailService {

	private static final Logger log = LoggerFactory
			.getLogger(UserDetailService.class);

	@Autowired
	private UserDetailMapper userDetailMapper;
	@Autowired
	private UserDetailDao userDetailDao;
	@Autowired
	private UserSkillDetailMapper userSkillDetailMapper;
	@Autowired
	private UserExperienceDetailsService userExperienceDetailsService;
	@Autowired
	private UserEducationDetailsService userEducationDetailsService;

	/**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves.
	 *
	 */
	private void getExperienceMonthsAndUpdateUserDetail(Long jsId)
			throws Exception {
		// 2 .then calculate the months of exeprience and latest company name
		ExperienceProcessedData expProcessedData = userExperienceDetailsService
				.getExperienceProcessedData(jsId);
		// 3. Update userdetail
		UserDetail oldUser = userDetailDao.find(jsId);
		oldUser.setPastExperienceMonths(expProcessedData
				.getExpMonthsIncludingCurrent());
		expProcessedData.getLatestCompanyName().ifPresent(
				compannyName -> oldUser.setLatestCompanyName(compannyName));
		expProcessedData.getLatestJobTitle().ifPresent(
				profileHeadline -> oldUser.setProfileHeadline(profileHeadline));
		userDetailDao.mergeEntity(oldUser);
	}

	public boolean isResumeParsed(Long userId) {
		UserDetail oldUser = userDetailDao.find(userId);
		return oldUser.getIsResumeParsed() == ((byte) 1) ? true : false;
	}

	public void resumeParsed(Long userId) {
		UserDetail oldUser = userDetailDao.find(userId);
		oldUser.setIsResumeParsed((byte) 1);
	}

	/**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves. This is for the use by frontEnd
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public UserExperienceDataModel saveUserExperienceMonthAndExperienceDetail(
			Long jsId, UserExperienceDataModel experienceDataModel)
			throws Exception {
		// Currently following algo is working, in general we need to flush on
		// saving the object..here it appears it is happening by default..AUTO
		// mode of flush...it appears from the query logs
		// that AUTO does the flush as i can see the SQL queries: update
		// jobPortal.user_experince_detail set company_name=?, description=?,
		// is_current_job=?, location_detail=?, role_name=?, time_period_end=?,
		// time_period_start=?, user_id=? where experince_id=?
		// when i thrw an exception just after trying to update

		// 1. Save/Update user Experience
		UserExperienceDataModel experienceDataModel2 = userExperienceDetailsService
				.createOrUpdate(experienceDataModel, jsId);
		// 2 & 3. calculate months of experience and save user detail
		getExperienceMonthsAndUpdateUserDetail(jsId);
		return experienceDataModel2;
	}

	/**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves. This is for ths use by resume parsing controller
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void saveUserExperienceMonthAndExperienceDetails(Long jsId,
			List<UserExperinceDetail> userExperienceDetails) throws Exception {

		// 1. first save all user experiences
		userExperienceDetailsService
				.saveUserExperienceDetails(userExperienceDetails);
		// 2 & 3. calculate months of experience and save user detail
		getExperienceMonthsAndUpdateUserDetail(jsId);
	}

	@Transactional
	public boolean deleteExperienceAndUpdateUserDetail(Long jsId,
			UserExperienceDataModel experienceData) throws Exception {
		// 1 delete userExperienceData
		boolean del = userExperienceDetailsService.delete(experienceData, jsId);
		// 2 & 3. calculate months of experience and save user detail
		getExperienceMonthsAndUpdateUserDetail(jsId);
		return del;
	}

	/**
	 * TODO:Change logic later
	 * 
	 * @param user
	 * @throws ParseException
	 */
	@Transactional
	public UserEducationDataModel saveUserInstituteTierAndEducationDetail(
			@NotNull Long jsId, @NotNull UserEducationDataModel educationData)
			throws ParseException {
		// 1. save user education
		UserEducationDataModel educationDataModel = userEducationDetailsService
				.createOrUpdate(educationData, jsId);
		// 2. & 3. getTop most institute tier amongst all education detains and
		// update user detail with it
		getTopMostInstituteTierAndUpdateUserDetail(jsId);
		return educationDataModel;
	}
	 @Transactional
	    public void setUserProcessingStateAndTimestamp(final long userId, ProcessingState processingState) {
	        UserDetail user = userDetailDao.find(userId);
	        if(processingState.name()==ProcessingState.PROCESSINGFAILED.name()&&user.getProcessedState()!=ProcessingState.PROCESSED.name()&&user.getProcessedState()==ProcessingState.PARTIALLYPROCESSED.name())
	        {
	        	user.setProcessedState(processingState.name());
	            //job.setLastProcessedTime(new Date());
	            userDetailDao.save(user);
	        }
	        else
	        {
	                user.setProcessedState(processingState.name());
	            //job.setLastProcessedTime(new Date());
	            userDetailDao.save(user);
	        }

	    }

	/**
	 * TODO:Change logic later
	 * 
	 * @param user
	 */
	@Transactional
	public void saveUserInstituteTierAndEducationDetails(@NotNull Long jsId,
			List<UserEducationDetail> educationDetails) {
		// 1. save user education
		userEducationDetailsService.saveUserEducationDetails(educationDetails);
		// 2. & 3. getTop most institute tier amongst all education detains and
		// update user detail with it
		getTopMostInstituteTierAndUpdateUserDetail(jsId);

	}

	@Transactional
	public boolean deleteEducationAndUpdateInstituteTier(@NotNull Long jsId,
			UserEducationDataModel dataModel) throws ParseException {
		boolean del = userEducationDetailsService.delete(dataModel, jsId);
		getTopMostInstituteTierAndUpdateUserDetail(jsId);
		return del;
	}

	private void getTopMostInstituteTierAndUpdateUserDetail(Long jsId) {
		// 2. get top most institute tier
		int instituteTier = userEducationDetailsService
				.getTopMostInstituteForUser(jsId);
		// 3. Update user detail with top most institute tier
		UserDetail oldUser = userDetailDao.find(jsId);
		oldUser.setTopInstituteTier(instituteTier);
		userDetailDao.mergeEntity(oldUser);
	}

	/**
	 * This to be used only by resume upload service. We update only those
	 * fields which were null.
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public boolean saveUserDetailsFromResumeParser(UserDetail user) {
		UserDetail oldUser = userDetailDao.find(user.getUserId());
		log.info("zzz user before saving = " + user.getUserId() + " "
				+ user.getMobileNumber());
		userDetailMapper.updateOldUserNullFieldsWithNewFields(oldUser, user);
		// we re save the old user only
		boolean isNew = userDetailDao.save(oldUser);

		return isNew;
	}

	/**
	 * Currently being used saveResumeHandler when creating new useer from
	 * unprocessed resumes.
	 * 
	 * @return
	 */
	@Transactional
	public boolean saveNewUser(UserDetail user) {
		return userDetailDao.save(user);
	}

	@Transactional
	public UserBasicDataModel getUserBasicDataModelByAccountId(
			@NotNull final Long accountId) {
		final BasicAccountDetail basicAccountDetail = new BasicAccountDetail();
		basicAccountDetail.setAccountId(accountId);
		final List<UserDetail> jobseekerDetails = userDetailDao
				.getEntitiesByPropertyValue(new HashMap<String, Object>() {
					private static final long serialVersionUID = 1L;
					{
						put("basicAccountDetail", basicAccountDetail);
					}
				}, UserDetail.class);

		return (null == jobseekerDetails || jobseekerDetails.isEmpty()) ? null
				: userDetailMapper.getUserBasicDataModel(jobseekerDetails
						.get(0));
	}

	@Transactional
	public UserDataModel getUserDataModelByIdInTransaction(
			@NotNull final Long userId) {
		return getUserDataModelById(userId);
	}

	public UserDataModel getUserDataModelById(@NotNull final Long userId) {
		return userDetailMapper.getUserDataModel(userDetailDao.find(userId));
	}

	@Transactional
	public List<UserDataModel> getUserDataModelByIdsInTransaction(
			@NotNull final List<Long> userIds) {
		return getUserDataModelByIds(userIds);
	}

	public List<UserDataModel> getUserDataModelByIds(
			@NotNull final List<Long> userIds) {
		final List<UserDataModel> userDataModels = Lists.newArrayList();
		userIds.stream().forEach(
				uid -> userDataModels.add(getUserDataModelById(uid)));
		return userDataModels;
	}

	@Transactional
	public UserDetail getUserDetail(final long userId) {
		return userDetailDao.find(userId);
	}

	@Transactional
	public UserBasicDataModel getUserBasicDataModel(final Long userId) {
		return userDetailMapper.getUserBasicDataModel(userDetailDao
				.find(userId));
	}

	@Transactional
	public List<UserDataModel> getUserDataModelForEngineByIds(
			@NotNull final List<Long> userIds) {

		final List<UserDataModel> userDataModels = Lists.newArrayList();
		final List<UserDetail> userDetails = Lists.newArrayList();

		userDetails
				.addAll(Arrays.asList(userDetailDao
						.find((Serializable[]) userIds.toArray(new Long[userIds
								.size()]))));

		for (final UserDetail userDetail : userDetails) {
			userDataModels.add(userDetailMapper
					.getUserDataModelWithExperience(userDetail));
		}
		return userDataModels;
	}

	@Transactional
	public UserBasicDataModel getUserBasicDetailById(@NotNull final Long userId) {
		return userDetailMapper.getUserBasicDataModel(userDetailDao
				.find(userId));
	}

	/**
	 * Do not use this method. There are many fields in UserDetail table which
	 * will get overriden. Please make separate small models of what you want to
	 * update.And update that portion only.
	 * 
	 */
	@Deprecated
	@Transactional
	public UserBasicDataModel addOrUpdateUserBasicDetail(
			final UserBasicDataModel jobseekerBasicDataModel, Long userId,
			Long accountId) throws ParseException {
		final UserDetail jobseekerDetail = userDetailMapper.getUserDetail(
				jobseekerBasicDataModel, userId, accountId);
		userDetailDao.save(jobseekerDetail);
		return userDetailMapper.getUserBasicDataModel(jobseekerDetail);
	}

	@Transactional
	public UserBasicDataModel updateUserProfileHeader(
			final ProfileHeaderDataModel profileHeader, final Long jsId,
			final Long accountId) throws ParseException {
		UserDetail userDetail = userDetailDao.find(jsId);
		userDetailMapper.updateProfileHeader(profileHeader, userDetail);
		userDetailDao.mergeEntity(userDetail);
		return userDetailMapper.getUserBasicDataModel(userDetail);
	}

	// mock remove later
	@Transactional
	public boolean updateEntity(UserDetail js) {
		userDetailDao.save(js);
		return true;
	}

	@Transactional
	public RecruiterProfileDataModel updateRecruiterProfile(
			final RecruiterProfileDataModel recruiterProfileDataModel,
			final Long userId, final Long accountId) throws ParseException {
		final UserBasicDataModel userBasicDataModel = addOrUpdateUserBasicDetail(
				recruiterProfileDataModel.getUserBasicDataModel(), userId,
				accountId);
		final UserExperienceDataModel userExperienceDataModel = userExperienceDetailsService
				.createOrUpdate(
						recruiterProfileDataModel.getUserExperienceDataModel(),
						userId);
		recruiterProfileDataModel
				.setUserExperienceDataModel(userExperienceDataModel);
		recruiterProfileDataModel.setUserBasicDataModel(userBasicDataModel);
		return recruiterProfileDataModel;
	}

	// other methods.

	/**
	 * returns comma separated String of skills. Can be null or empty.
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public String getSkillsForUser(final Long userId) {
		return userDetailDao.find(userId).getSkills();

	}

	/*
	 * This is to be used from Profile controller NOT FROM RESUME PARSER METHODS
	 * just get the user detail, add the skill and save. Skills can be null or
	 * empty.
	 */
	@Transactional
	public void addOrUpdateUserSkillDetails(final Long userId,
			final String skills) {
		UserDetail user = userDetailDao.find(userId);
		user.setSkills("," + skills + ",");
		userDetailDao.mergeEntity(user);
	}

	private Set<Long> getUserIdsByFilterInSkillName(final Set<String> skillNames) {

		List<Filter> skillFilters = new ArrayList<Filter>();
		skillNames.stream().forEach(skill -> {
			Filter f = Filter.ilike("skills", skill);
			skillFilters.add(f);
		});
		Search criteria = new Search(UserDetail.class);
		criteria.addFilterOr(skillFilters.toArray(new Filter[0]));
		criteria.addField("userId");
		return new HashSet<Long>(userDetailDao.getEntityIds(criteria));

	}

	@Transactional
	public Set<Long> getUserIdsByFilterInSkillNameInTransaction(
			final Set<String> skillNames) {
		return getUserIdsByFilterInSkillName(skillNames);
	}

	/**
	 * 
	 * @param jobDataModel
	 * @return
	 */
	@SuppressWarnings("static-access")
	private Search getSearchFilterForUsersForPostedJob(
			final @NotNull JobDataModel jobDataModel) {

		Search search = new Search(UserDetail.class);
		search.addFilterNotEqual("userId",
				Long.parseLong(jobDataModel.getRecruiterId()));
		String jobComapnyName = jobDataModel.getOrganisationName().replaceAll(
				"[^A-Za-z0-9]", "%");
		search.addFilter(Filter.not(
				Filter.ilike("latestCompanyName",
						"%" + jobComapnyName.toLowerCase() + "%")).or(
				Filter.equal("latestCompanyName", null)));

		// search.addFilterLessOrEqual("ctc",
		// Integer.parseInt(jobDataModel.getMaxSalary()));

		// do not add this for freshers
		if (jobDataModel.getJobExperience() >= 6) {
			log.info("zzzzzzzzzzz adding experience filter");
			search.addFilterGreaterOrEqual("pastExperienceMonths",
					jobDataModel.getJobExperience());
			search.addFilterGreaterOrEqual("pastExperienceMonths",
					jobDataModel.getJobExperience() - 36);
			log.info("zzzzzzzz experience filter = " + search);
		}
		log.info("zzzzzzzz search = " + search);

		List<Filter> skillFilters = new ArrayList<Filter>();

		final List<String> skills = StringUtils.isEmpty(jobDataModel
				.getSkills()) ? new ArrayList<String>() : Arrays
				.asList(jobDataModel.getSkills().split(","));

		if (null != skills && !skills.isEmpty()) {
			skills.stream().forEach(
					skill -> {
						Filter f = Filter.ilike("skills",
								"%," + skill.toLowerCase() + ",%");
						Filter sf = Filter.ilike("skillsFoundInWork", "%,"
								+ skill.toLowerCase() + ",%");
						skillFilters.add(f);
						skillFilters.add(sf);
					});
			search.addFilterOr(skillFilters.toArray(new Filter[0]));
		}

		// if (!StringUtils.isEmpty(jobDataModel.getJobFunction())) {
		// jobFunctionFilter = Filter.equal("jobFunction",
		// jobDataModel.getJobFunction());
		// }
		// if (skillFilters.isEmpty() && jobFunctionFilter == null)
		// throw new
		// RuntimeException("Skills and Job Function Filters both empty for the job "
		// + jobDataModel.getJobId());
		//
		// if (!skillFilters.isEmpty() && null != jobFunctionFilter) {
		// List<Filter> skillsOrJobFunction = new ArrayList<Filter>();
		// skillsOrJobFunction.addAll(skillFilters);
		// skillsOrJobFunction.add(jobFunctionFilter);
		// search.addFilterOr(skillsOrJobFunction.toArray(new Filter[0]));
		// } else if (!skillFilters.isEmpty()) {
		// search.addFilterOr(skillFilters.toArray(new Filter[0]));
		// } else if (null != jobFunctionFilter) {
		// search.addFilter(jobFunctionFilter);
		// }

		// Filter jobFunctionFilter = null;
		// Filter industryFilter = null;
		// if (!StringUtils.isEmpty(jobDataModel.getJobFunction())) {
		// jobFunctionFilter = Filter.equal("jobFunction",
		// jobDataModel.getJobFunction());
		// }
		// if (!StringUtils.isEmpty(jobDataModel.getIndustryName())) {
		// industryFilter = Filter.equal("industryName",
		// jobDataModel.getIndustryName());
		// }
		// if (null != jobFunctionFilter && null != industryFilter) {
		// search.addFilterOr(industryFilter, jobFunctionFilter);
		// } else if (null != jobFunctionFilter) {
		// search.addFilter(jobFunctionFilter);
		// } else {
		// search.addFilter(industryFilter);
		// }

		search.setDistinct(true);
		return search;

	}

	private Set<UserDataModel> getUserDataModelsForPostedJob(
			final @NotNull JobDataModel jobDataModel) {

		Search search = getSearchFilterForUsersForPostedJob(jobDataModel);
		search.addField("userId");
		System.out.println("###FILTER MAP ");
		final List<Long> userIds = userDetailDao.getEntityIds(search);
		System.out.println("###FILTER MAP " + userIds);

		final Map<String, Collection<?>> filterMap = Maps.newHashMap();
		filterMap.put("userId", new HashSet<Long>(userIds));

		return userDetailMapper.getUserDataModels(userDetailDao
				.getEntitiesByPropertyValues(filterMap, UserDetail.class));
	}

	/*
	 * This is used to fetch the the values in 'Chunk'of pageSize'.
	 */
	private Set<UserDataModel> getUserDetailsForPostedJob(
			final @NotNull JobDataModel jobDataModel, int pageOffset,
			int pageSize) {

		Search search = getSearchFilterForUsersForPostedJob(jobDataModel);
		// search.addField("userId");
		List<UserDetail> userDetailList = this.userDetailDao.getEntities(
				search, pageOffset, pageSize);
		if (userDetailList == null) {
			return Sets.newHashSet();
		} else {
			return this.userDetailMapper.getUserDataModels(userDetailList);
		}

	}

	@Transactional
	public Set<UserDataModel> getUserIdsForPostedJobsInTransaction(
			final JobDataModel jobDataModel) {
		return getUserDataModelsForPostedJob(jobDataModel);
	}

	@Transactional
	public Set<UserDataModel> getUserDetailsForPostedJobsInTransaction(
			final JobDataModel jobDataModel, int pageOffset, int pageSize) {
		return getUserDetailsForPostedJob(jobDataModel, pageOffset, pageSize);
	}

	@Transactional
	public UserSearchResult getFallbackUserIdsForPostedJobsPaginated(
			final JobDataModel jobData,
			final SearchCandidatesCriteria criteria,
			final List<Long> ineligibleUserIds) {

		Search search = getSearchFilterForUsersForPostedJob(jobData);
		int totalCount = userDetailDao.count(search);
		log.error("zzzzzzz total count fallback users = " + totalCount);
		int actualCount = totalCount;
		int pageReductionValue = 0;
		int actualPageNumber = criteria.getPagination().getPageNumber();
		int finalPageNumber = actualPageNumber;
		if (ineligibleUserIds != null && !ineligibleUserIds.isEmpty()) {
			log.error("zzzzzzz ineligble user list nbot empty");
			search.addFilterNotIn("userId", ineligibleUserIds);
			actualCount = totalCount - ineligibleUserIds.size();
			pageReductionValue = ineligibleUserIds.size()
					/ criteria.getPagination().getPageSize();
			if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT)
				actualPageNumber = (actualPageNumber - pageReductionValue >= 0) ? actualPageNumber
						- pageReductionValue
						: 0;
		}
		log.error("zzzzzzz totalCount,actualCount,padeReductionValue,actualPageNumber= "
				+ totalCount
				+ "  "
				+ actualCount
				+ "  "
				+ pageReductionValue
				+ "  " + actualPageNumber);
		log.error("zzzzzzzz page action is "
				+ criteria.getPagination().getPaginationAction());
		if (criteria.getPagination().getPaginationAction() == PaginationAction.NEXT) {
			finalPageNumber = actualPageNumber + 1;
		} else if (criteria.getPagination().getPaginationAction() == PaginationAction.PREV) {
			finalPageNumber = actualPageNumber - 1;
		}
		search.setPage(finalPageNumber);
		search.setMaxResults(criteria.getPagination().getPageSize());
		log.error("zzzzzzz search query = " + search);
		List<UserDetail> users = userDetailDao.getEntities(search);
		log.error("zzzzzzz searched users are " + users);
		List<SearchCandidatesResult> result = new ArrayList<SearchCandidatesResult>();
		result = users.stream().map(user -> getSearchResult(user, true))
				.collect(Collectors.toList());
		return new UserSearchResult(totalCount, actualCount, result,
				actualPageNumber, finalPageNumber);

	}

	private SearchCandidatesResult getSearchResult(UserDetail user,
			boolean fallback) {
		SearchCandidatesResult result = new SearchCandidatesResult();

		// Optional<UserExperinceDetail> exp = user.getUserExperinceDetails()
		// .stream()
		// .filter(experience -> experience.getIsCurrentJob() == 1)
		// .findFirst();

		// result.setCompany(user.getLastCompanyName());
		if (user.getPastExperienceMonths() != null)
			result.setExperience(user.getPastExperienceMonths().toString());
		result.setFallback(fallback);
		result.setFirstName(user.getFirstName());
		result.setLastName(user.getLastName());
		result.setJobseekerId(user.getUserId());
		result.setLocation(user.getAddress());
		result.setProfileHeadline(user.getProfileHeadline());
		result.setProfileImageUrl(user.getProfileImageUrl());

		return result;
	}
	/*
	 * ======= private static final Logger log =
	 * LoggerFactory.getLogger(UserDetailService.class);
	 * 
	 * @Autowired private UserDetailMapper userDetailMapper;
	 * 
	 * @Autowired private UserDetailDao userDetailDao;
	 * 
	 * @Autowired private UserSkillDetailMapper userSkillDetailMapper;
	 * 
	 * @Autowired private UserExperienceDetailsService
	 * userExperienceDetailsService;
	 * 
	 * @Autowired private UserEducationDetailsService
	 * userEducationDetailsService;
	 *//**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves.
	 *
	 */
	/*
	 * private void getExperienceMonthsAndUpdateUserDetail(Long jsId) throws
	 * Exception { // 2 .then calculate the months of exeprience int expMonths =
	 * userExperienceDetailsService.getExperienceMonthsIncludingCurrent(jsId);
	 * // 3. Update userdetail UserDetail oldUser = userDetailDao.find(jsId);
	 * oldUser.setPastExperienceMonths(expMonths);
	 * userDetailDao.mergeEntity(oldUser); }
	 *//**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves. This is for the use by frontEnd
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	/*
	 * @Transactional public UserExperienceDataModel
	 * saveUserExperienceMonthAndExperienceDetail(Long jsId,
	 * UserExperienceDataModel experienceDataModel) throws Exception { //
	 * Currently following algo is working, in general we need to flush on //
	 * saving the object..here it appears it is happening by default..AUTO //
	 * mode of flush...it appears from the query logs // that AUTO does the
	 * flush as i can see the SQL queries: update //
	 * jobPortal.user_experince_detail set company_name=?, description=?, //
	 * is_current_job=?, location_detail=?, role_name=?, time_period_end=?, //
	 * time_period_start=?, user_id=? where experince_id=? // when i thrw an
	 * exception just after trying to update
	 * 
	 * // 1. Save/Update user Experience UserExperienceDataModel
	 * experienceDataModel2 = userExperienceDetailsService.createOrUpdate(
	 * experienceDataModel, jsId); // 2 & 3. calculate months of experience and
	 * save user detail getExperienceMonthsAndUpdateUserDetail(jsId); return
	 * experienceDataModel2; }
	 *//**
	 * gets the months of experience in db and adds it that given in the
	 * userDetail and saves. This is for ths use by resume parsing controller
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	/*
	 * @Transactional public void
	 * saveUserExperienceMonthAndExperienceDetails(Long jsId,
	 * List<UserExperinceDetail> userExperienceDetails) throws Exception {
	 * 
	 * // 1. first save all user experiences
	 * userExperienceDetailsService.saveUserExperienceDetails
	 * (userExperienceDetails); // 2 & 3. calculate months of experience and
	 * save user detail getExperienceMonthsAndUpdateUserDetail(jsId); }
	 * 
	 * @Transactional public boolean deleteExperienceAndUpdateUserDetail(Long
	 * jsId, UserExperienceDataModel experienceData) throws Exception { // 1
	 * delete userExperienceData boolean del =
	 * userExperienceDetailsService.delete(experienceData, jsId); // 2 & 3.
	 * calculate months of experience and save user detail
	 * getExperienceMonthsAndUpdateUserDetail(jsId); return del; }
	 *//**
	 * TODO:Change logic later
	 * 
	 * @param user
	 * @throws ParseException
	 */
	/*
	 * @Transactional public UserEducationDataModel
	 * saveUserInstituteTierAndEducationDetail(@NotNull Long jsId,
	 * 
	 * @NotNull UserEducationDataModel educationData) throws ParseException { //
	 * 1. save user education UserEducationDataModel educationDataModel =
	 * userEducationDetailsService.createOrUpdate( educationData, jsId); // 2. &
	 * 3. getTop most institute tier amongst all education detains and // update
	 * user detail with it getTopMostInstituteTierAndUpdateUserDetail(jsId);
	 * return educationDataModel; }
	 *//**
	 * TODO:Change logic later
	 * 
	 * @param user
	 */
	/*
	 * @Transactional public void
	 * saveUserInstituteTierAndEducationDetails(@NotNull Long jsId,
	 * List<UserEducationDetail> educationDetails) { // 1. save user education
	 * userEducationDetailsService.saveUserEducationDetails(educationDetails);
	 * // 2. & 3. getTop most institute tier amongst all education detains and
	 * // update user detail with it
	 * getTopMostInstituteTierAndUpdateUserDetail(jsId);
	 * 
	 * }
	 * 
	 * @Transactional public boolean
	 * deleteEducationAndUpdateInstituteTier(@NotNull Long jsId,
	 * UserEducationDataModel dataModel) throws ParseException { boolean del =
	 * userEducationDetailsService.delete(dataModel, jsId);
	 * getTopMostInstituteTierAndUpdateUserDetail(jsId); return del; }
	 * 
	 * private void getTopMostInstituteTierAndUpdateUserDetail(Long jsId) { //
	 * 2. get top most institute tier InstituteTier instituteTier =
	 * userEducationDetailsService.getTopMostInstituteForUser(jsId); // 3.
	 * Update user detail with top most institute tier UserDetail oldUser =
	 * userDetailDao.find(jsId); oldUser.setInstituteTier(instituteTier);
	 * userDetailDao.mergeEntity(oldUser); }
	 *//**
	 * This to be used only by resume upload service. We update only those
	 * fields which were null.
	 * 
	 * @param user
	 * @return
	 */
	/*
	 * @Transactional public boolean saveUserDetailsFromResumeParser(UserDetail
	 * user) { UserDetail oldUser = userDetailDao.find(user.getUserId());
	 * log.info("zzz user before saving = " + user.getUserId() + " " +
	 * user.getMobileNumber());
	 * userDetailMapper.updateOldUserNullFieldsWithNewFields(oldUser, user); //
	 * we re save the old user only boolean isNew = userDetailDao.save(oldUser);
	 * 
	 * return isNew; }
	 * 
	 * @Transactional public UserBasicDataModel
	 * getUserBasicDataModelByAccountId(@NotNull final Long accountId) { final
	 * BasicAccountDetail basicAccountDetail = new BasicAccountDetail();
	 * basicAccountDetail.setAccountId(accountId); final List<UserDetail>
	 * jobseekerDetails = userDetailDao.getEntitiesByPropertyValue( new
	 * HashMap<String, Object>() { private static final long serialVersionUID =
	 * 1L; { put("basicAccountDetail", basicAccountDetail); } },
	 * UserDetail.class);
	 * 
	 * return (null == jobseekerDetails || jobseekerDetails.isEmpty()) ? null :
	 * userDetailMapper .getUserBasicDataModel(jobseekerDetails.get(0)); }
	 * 
	 * @Transactional public UserDataModel
	 * getUserDataModelByIdInTransaction(@NotNull final Long userId) { return
	 * getUserDataModelById(userId); }
	 * 
	 * public UserDataModel getUserDataModelById(@NotNull final Long userId) {
	 * return userDetailMapper.getUserDataModel(userDetailDao.find(userId)); }
	 * 
	 * @Transactional public List<UserDataModel>
	 * getUserDataModelByIdsInTransaction(@NotNull final List<Long> userIds) {
	 * return getUserDataModelByIds(userIds); }
	 * 
	 * public List<UserDataModel> getUserDataModelByIds(@NotNull final
	 * List<Long> userIds) { final List<UserDataModel> userDataModels =
	 * Lists.newArrayList(); userIds.stream().forEach(uid ->
	 * userDataModels.add(getUserDataModelById(uid))); return userDataModels; }
	 * 
	 * @Transactional UserDetail getUserDetail(final long userId) { return
	 * userDetailDao.find(userId); }
	 * 
	 * @Transactional public UserBasicDataModel getUserBasicDataModel(final Long
	 * userId) { return
	 * userDetailMapper.getUserBasicDataModel(userDetailDao.find(userId)); }
	 * 
	 * @Transactional public List<UserDataModel>
	 * getUserDataModelForEngineByIds(@NotNull final List<Long> userIds) {
	 * 
	 * final List<UserDataModel> userDataModels = Lists.newArrayList(); final
	 * List<UserDetail> userDetails = Lists.newArrayList();
	 * 
	 * userDetails.addAll(Arrays.asList(userDetailDao.find((Serializable[])
	 * userIds .toArray(new Long[userIds.size()]))));
	 * 
	 * for (final UserDetail userDetail : userDetails) {
	 * userDataModels.add(userDetailMapper
	 * .getUserDataModelWithExperience(userDetail)); } return userDataModels; }
	 * 
	 * @Transactional public UserBasicDataModel getUserBasicDetailById(@NotNull
	 * final Long userId) { return
	 * userDetailMapper.getUserBasicDataModel(userDetailDao.find(userId)); }
	 * 
	 * @Transactional public UserBasicDataModel addOrUpdateUserBasicDetail(
	 * final UserBasicDataModel jobseekerBasicDataModel, Long userId, Long
	 * accountId) throws ParseException { final UserDetail jobseekerDetail =
	 * userDetailMapper.getUserDetail(jobseekerBasicDataModel, userId,
	 * accountId); userDetailDao.save(jobseekerDetail); return
	 * userDetailMapper.getUserBasicDataModel(jobseekerDetail); }
	 * 
	 * @Transactional public UserBasicDataModel updateUserProfileHeader(final
	 * ProfileHeaderDataModel profileHeader, final Long jsId, final Long
	 * accountId) throws ParseException { UserDetail userDetail =
	 * userDetailDao.find(jsId);
	 * userDetailMapper.updateProfileHeader(profileHeader, userDetail);
	 * userDetailDao.mergeEntity(userDetail); return
	 * userDetailMapper.getUserBasicDataModel(userDetail); }
	 * 
	 * // mock remove later
	 * 
	 * @Transactional public boolean updateEntity(UserDetail js) {
	 * userDetailDao.save(js); return true; }
	 * 
	 * @Transactional public RecruiterProfileDataModel updateRecruiterProfile(
	 * final RecruiterProfileDataModel recruiterProfileDataModel, final Long
	 * userId, final Long accountId) throws ParseException { final
	 * UserBasicDataModel userBasicDataModel = addOrUpdateUserBasicDetail(
	 * recruiterProfileDataModel.getUserBasicDataModel(), userId, accountId);
	 * final UserExperienceDataModel userExperienceDataModel =
	 * userExperienceDetailsService
	 * .createOrUpdate(recruiterProfileDataModel.getUserExperienceDataModel(),
	 * userId);
	 * recruiterProfileDataModel.setUserExperienceDataModel(userExperienceDataModel
	 * ); recruiterProfileDataModel.setUserBasicDataModel(userBasicDataModel);
	 * return recruiterProfileDataModel; }
	 * 
	 * // other methods.
	 *//**
	 * returns comma separated String of skills. Can be null or empty.
	 * 
	 * @param userId
	 * @return
	 */
	/*
	 * @Transactional public String getSkillsForUser(final Long userId) { return
	 * userDetailDao.find(userId).getSkills();
	 * 
	 * }
	 * 
	 * 
	 * This is to be used from Profile controller NOT FROM RESUME PARSER METHODS
	 * just get the user detail, add the skill and save. Skills can be null or
	 * empty.
	 * 
	 * @Transactional public void addOrUpdateUserSkillDetails(final Long userId,
	 * final String skills) { UserDetail user = userDetailDao.find(userId);
	 * user.setSkills(skills); userDetailDao.mergeEntity(user); }
	 * 
	 * private Set<Long> getUserIdsByFilterInSkillName(final Set<String>
	 * skillNames) {
	 * 
	 * List<Filter> skillFilters = new ArrayList<Filter>();
	 * skillNames.stream().forEach(skill -> { Filter f = Filter.ilike("skills",
	 * skill); skillFilters.add(f); }); Search criteria = new
	 * Search(UserDetail.class); criteria.addFilterOr(skillFilters.toArray(new
	 * Filter[0])); criteria.addField("userId"); return new
	 * HashSet<Long>(userDetailDao.getEntityIds(criteria));
	 * 
	 * }
	 * 
	 * @Transactional public Set<Long>
	 * getUserIdsByFilterInSkillNameInTransaction(final Set<String> skillNames)
	 * { return getUserIdsByFilterInSkillName(skillNames); }
	 *//**
	 * 
	 * @param jobDataModel
	 * @return
	 */
	/*
	 * private Search getSearchFilterForUsersForPostedJob(final @NotNull
	 * JobDataModel jobDataModel) { Search search = new
	 * Search(UserDetail.class); search.addFilterNotEqual("userId",
	 * Long.parseLong(jobDataModel.getRecruiterId()));
	 * search.addFilterLessOrEqual("ctc",
	 * Integer.parseInt(jobDataModel.getMaxSalary()));
	 * log.info("zzzzz jobDataModel = " + jobDataModel); // do not add this for
	 * freshers if (jobDataModel.getJobExperience() > 12) {
	 * log.info("zzzzzzzzzzz adding experience filter");
	 * search.addFilterNotEqual("userExperinceDetails.companyName",
	 * jobDataModel.getOrganisationName());
	 * search.addFilterGreaterOrEqual("pastExperienceMonths",
	 * jobDataModel.getJobExperience());
	 * log.info("zzzzzzzz experience filter = " + search); }
	 * log.info("zzzzzzzz search = " + search);
	 * 
	 * // List<Filter> skillFilters = new ArrayList<Filter>(); // Filter
	 * jobFunctionFilter = null; // final List<String> skills =
	 * StringUtils.isEmpty(jobDataModel // .getSkills()) ? new
	 * ArrayList<String>() : Arrays //
	 * .asList(jobDataModel.getSkills().split(",")); // // if (null != skills &&
	 * !skills.isEmpty()) { // skills.stream().forEach(skill -> { // Filter f =
	 * Filter.ilike("skills", skill); // skillFilters.add(f); // }); // // } //
	 * if (!StringUtils.isEmpty(jobDataModel.getJobFunction())) { //
	 * jobFunctionFilter = Filter.equal("jobFunction", //
	 * jobDataModel.getJobFunction()); // } // if (skillFilters.isEmpty() &&
	 * jobFunctionFilter == null) // throw new RuntimeException( //
	 * "Skills and Job Function Filters both empty for the job " // +
	 * jobDataModel.getJobId()); // // if (!skillFilters.isEmpty() && null !=
	 * jobFunctionFilter) { // List<Filter> skillsOrJobFunction = new
	 * ArrayList<Filter>(); // skillsOrJobFunction.addAll(skillFilters); //
	 * skillsOrJobFunction.add(jobFunctionFilter); //
	 * search.addFilterOr(skillsOrJobFunction.toArray(new Filter[0])); // } else
	 * if (!skillFilters.isEmpty()) { //
	 * search.addFilterOr(skillFilters.toArray(new Filter[0])); // } else if
	 * (null != jobFunctionFilter) { // search.addFilter(jobFunctionFilter); //
	 * } Filter jobFunctionFilter = null; Filter industryFilter = null; if
	 * (!StringUtils.isEmpty(jobDataModel.getJobFunction())) { jobFunctionFilter
	 * = Filter.equal("jobFunction", jobDataModel.getJobFunction()); } if
	 * (!StringUtils.isEmpty(jobDataModel.getIndustryName())) {
	 *//**
	 * uncomment after adding field in db.
	 */
	/*
	 * industryFilter = Filter.equal("jobIndustry",
	 * jobDataModel.getIndustryName()); } if (null != jobFunctionFilter && null
	 * != industryFilter) { search.addFilterOr(industryFilter,
	 * jobFunctionFilter); } else if (null != jobFunctionFilter) {
	 * search.addFilter(jobFunctionFilter); } else {
	 * search.addFilter(industryFilter); } search.setDistinct(true); return
	 * search;
	 * 
	 * }
	 * 
	 * private Set<UserDataModel> getUserDataModelsForPostedJob( final @NotNull
	 * JobDataModel jobDataModel) {
	 * 
	 * Search search = getSearchFilterForUsersForPostedJob(jobDataModel);
	 * search.addField("userId"); System.out.println("###FILTER MAP "); final
	 * List<Long> userIds = userDetailDao.getEntityIds(search);
	 * System.out.println("###FILTER MAP " + userIds);
	 * 
	 * final Map<String, Collection<?>> filterMap = Maps.newHashMap();
	 * filterMap.put("userId", new HashSet<Long>(userIds));
	 * 
	 * return
	 * userDetailMapper.getUserDataModels(userDetailDao.getEntitiesByPropertyValues
	 * ( filterMap, UserDetail.class)); }
	 * 
	 * 
	 * 
	 * @Transactional public Set<UserDataModel>
	 * getUserIdsForPostedJobsInTransaction(final JobDataModel jobDataModel) {
	 * return getUserDataModelsForPostedJob(jobDataModel); }
	 *//**
	 * 
	 * @param jobDataModel
	 * @param pageOffset
	 * @param pageSize
	 * @return Used to fetch the Values for specified 'page Size' only.
	 */
	/*
	 * @Transactional public Set<UserDataModel>
	 * getUserIdsForPostedJobsInTransaction(final JobDataModel jobDataModel, int
	 * pageOffset, int pageSize) { return
	 * getUserDataModelsForPostedJob(jobDataModel, pageOffset, pageSize); }
	 * 
	 * @Transactional public UserSearchResult
	 * getUserIdsForPostedJobsPaginated(final JobDataModel jobData, final
	 * SearchCandidatesCriteria criteria) { Search search =
	 * getSearchFilterForUsersForPostedJob(jobData); int count =
	 * userDetailDao.count(search);
	 * search.setPage(criteria.getPagination().getPageNumber());
	 * search.setMaxResults(criteria.getPagination().getPageSize());
	 * log.info("zzzzzzz search query = " + search); List<UserDetail> users =
	 * userDetailDao.getEntities(search); log.info("zzzzzzz searched users are "
	 * + users); List<SearchCandidatesResult> result = new
	 * ArrayList<SearchCandidatesResult>(); result =
	 * getSearchCandidateResult(users); return new UserSearchResult(count,
	 * result);
	 * 
	 * }
	 * 
	 * private List<SearchCandidatesResult>
	 * getSearchCandidateResult(List<UserDetail> users) {
	 * log.info("zzzzzz here"); return users.stream().map(user ->
	 * getSearchResult(user, true)).collect(Collectors.toList());
	 * 
	 * }
	 * 
	 * private SearchCandidatesResult getSearchResult(UserDetail user, boolean
	 * fallback) { SearchCandidatesResult result = new SearchCandidatesResult();
	 * 
	 * Optional<UserExperinceDetail> exp =
	 * user.getUserExperinceDetails().stream() .filter(experience ->
	 * experience.getIsCurrentJob() == 1).findFirst(); if (exp.isPresent())
	 * result.setCompany(exp.get().getCompanyName());
	 * result.setExperience(user.getPastExperienceMonths().toString());
	 * result.setFallback(fallback); result.setFirstName(user.getFirstName());
	 * result.setLastName(user.getLastName());
	 * result.setJobseekerId(user.getUserId());
	 * result.setLocation(user.getAddress());
	 * result.setProfileHeadline(user.getProfileHeadline());
	 * result.setProfileImageUrl(user.getProfileImageUrl()); if (user.getCtc()
	 * != null) result.setSalary(user.getCtc().toString());
	 * 
	 * return result; } >>>>>>> Adding API to call Engine service.
	 */
}