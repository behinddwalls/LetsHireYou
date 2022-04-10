package com.portal.job.mapper;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.ProcessingState;
import com.portal.job.model.ExpertRegisterDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.ProfileHeaderDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserDetailMapper {

	private static final Logger log = LoggerFactory
			.getLogger(UserDetailMapper.class);

	private static String Delimiter = ",";
	private static int SKILLS_SIZE = 15;

	@Autowired
	private UserEducationDetailMapper userEducationDetailMapper;
	@Autowired
	private UserExperienceDetailsMapper userExperienceDetailsMapper;
	@Autowired
	private UserPatentDetailsMapper userPatentDetailsMapper;
	@Autowired
	private UserProjectDetailMapper userProjectDetailMapper;
	@Autowired
	private UserCertificationDetailMapper userCertificationDetailMapper;
	@Autowired
	private UserPublicationDetailsMapper userPublicationDetailsMapper;

	@JsonIgnore
	public static boolean isUserProcessed(UserDetail user) {
		if (StringUtils.isEmpty(user.getProcessedState()))
			return false;
		if (ProcessingState.PROCESSED.name().equals(user.getProcessedState())) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public static boolean isUserPartiallyProcessed(UserDetail user) {
		if (StringUtils.isEmpty(user.getProcessedState()))
			return false;
		if (ProcessingState.valueOf(user.getProcessedState()) == ProcessingState.PARTIALLYPROCESSED)
			return true;
		return false;
	}

	public UserDetail getUserDetail(
			final UserBasicDataModel userBasicDataModel, Long userId,
			Long accountId) throws ParseException {
		final UserDetail userDetail = new UserDetail();

		if (userBasicDataModel != null) {
			if (userId != null && accountId != null) {
				userDetail.setUserId(userId);
				BasicAccountDetail accountDetail = new BasicAccountDetail();
				accountDetail.setAccountId(accountId);
				userDetail.setBasicAccountDetail(accountDetail);
			}
			userDetail.setFirstName(userBasicDataModel.getFirstName());
			userDetail.setLastName(userBasicDataModel.getLastName());
			userDetail.setProfileHeadline(userBasicDataModel
					.getProfileHeadline());
			userDetail.setSummary(userBasicDataModel.getSummary());
			userDetail.setMaritalStatus(userBasicDataModel.getMaritalStatus());
			userDetail.setGender(userBasicDataModel.getGender());
			if (!StringUtils.isEmpty(userBasicDataModel.getDateOfBirth())) {
				userDetail
						.setDateOfBirth(DateUtility
								.getDateFromString(userBasicDataModel
										.getDateOfBirth()));
			}
			userDetail.setInterests(userBasicDataModel.getInterests());
			userDetail.setMobileNumber(userBasicDataModel.getMobileNumber());
			userDetail.setOtherContactNumbers(userBasicDataModel
					.getOtherMobileNumber());
			userDetail.setAddress(userBasicDataModel.getAddress());
			if (!StringUtils.isEmpty(userBasicDataModel.getCtc()))
				userDetail.setCtc(Integer.valueOf(userBasicDataModel.getCtc()));
			userDetail.setTopInstituteTier(userBasicDataModel
					.getInstituteTier());
			userDetail.setPastExperienceMonths(userBasicDataModel
					.getPastExperienceMonths());
			userDetail.setJobFunction(userBasicDataModel.getJobFunction());
			// set the profileImage
			userDetail.setProfileImageUrl(userBasicDataModel
					.getProfileImageUrl());
			userDetail.setIndustryName(userBasicDataModel.getIndustryName());
			userDetail
					.setProcessedState(userBasicDataModel.getProcessedState());
			userDetail
					.setUserResumeLink(userBasicDataModel.getUserResumeLink());
			userDetail.setHideCtc((userBasicDataModel.isHideCtc() ? (byte) 1
					: (byte) 1));
			userDetail
					.setIsResumeParsed((userBasicDataModel.isResumeParsed() ? (byte) 1
							: (byte) 1));
			userDetail.setSkills(userBasicDataModel.getSkills());
			userDetail.setLanguage(userBasicDataModel.getLanguage());
			userDetail.setRecruiterType(userBasicDataModel.getRecruiterType());
			userDetail.setLatestCompanyName(userBasicDataModel
					.getLatestCompanyName());
		}
		return userDetail;

	}

	public UserBasicDataModel getUserBasicDataModel(final UserDetail userDetail) {

		String dob = null;
		if (userDetail.getDateOfBirth() != null) {
			dob = DateUtility.getStringFromDate(userDetail.getDateOfBirth());
		}
		final UserBasicDataModel userBasicDataModel = new UserBasicDataModel(
				userDetail.getUserId(), userDetail.getFirstName(),
				userDetail.getLastName(), userDetail.getGender(), dob,
				userDetail.getMaritalStatus(), userDetail.getProfileHeadline(),
				userDetail.getProfileImageUrl(), userDetail.getSummary(),
				userDetail.getInterests(), userDetail.getMobileNumber(),
				userDetail.getOtherContactNumbers(), userDetail.getAddress(),
				(userDetail.getCtc() == null) ? null : userDetail.getCtc()
						.toString(),
				(null == userDetail.getTopInstituteTier()) ? 0 : userDetail
						.getTopInstituteTier(),
				userDetail.getPastExperienceMonths(),
				userDetail.getJobFunction(), userDetail.getIndustryName(),
				userDetail.getProcessedState(), userDetail.getUserResumeLink(),
				(userDetail.getHideCtc() == 0) ? false : true,
				(userDetail.getIsResumeParsed() == 0) ? false : true,
				userDetail.getRecruiterProcessedState(),
				(userDetail.getHasProfileChanged() == 0) ? false : true,
				userDetail.getLanguage(), userDetail.getRecruiterType(),
				userDetail.getLatestCompanyName());

		userBasicDataModel.setProfileImageUrl(userDetail.getProfileImageUrl());
		userBasicDataModel.setSkills(userDetail.getSkills());
		userBasicDataModel.setSkillsFoundinWorkHistory(userDetail
				.getSkillsFoundInWork());

		return userBasicDataModel;

	}

	public void updateProfileHeader(final ProfileHeaderDataModel profileHeader,
			UserDetail user) {
		user.setAddress(profileHeader.getAddress());
		if (!StringUtils.isEmpty(profileHeader.getCtc()))
			user.setCtc(Integer.valueOf(profileHeader.getCtc()));
		user.setFirstName(profileHeader.getFirstName());
		user.setLastName(profileHeader.getLastName());
		user.setProfileHeadline(profileHeader.getProfileHeadline());
		user.setSummary(profileHeader.getSummary());
		user.setJobFunction(profileHeader.getJobFunction());
		user.setIndustryName(profileHeader.getIndustryName());
		user.setHideCtc(profileHeader.getHideCtc());
	}

	/**
	 * 
	 * @param newUser
	 * @param oldUser
	 */
	public UserDetail updateOldUserNullFieldsWithNewFields(
			final UserDetail oldUser, UserDetail newUser) {
		log.info("zzzzzzzz oldUser " + oldUser.toString());
		log.info("zzzzzzzzzz newUser " + newUser.toString());
		if (StringUtils.isEmpty(oldUser.getAddress()))
			oldUser.setAddress(newUser.getAddress());
		if (StringUtils.isEmpty(oldUser.getCtc()))
			oldUser.setCtc(newUser.getCtc());
		if (StringUtils.isEmpty(oldUser.getDateOfBirth()))
			oldUser.setDateOfBirth(newUser.getDateOfBirth());
		if (StringUtils.isEmpty(oldUser.getFirstName()))
			oldUser.setFirstName(newUser.getFirstName());
		if (StringUtils.isEmpty(oldUser.getGender()))
			oldUser.setGender(newUser.getGender());
		if (oldUser.getTopInstituteTier() == null)
			oldUser.setTopInstituteTier(newUser.getTopInstituteTier());
		if (StringUtils.isEmpty(oldUser.getInterests()))
			oldUser.setInterests(newUser.getInterests());
		if (StringUtils.isEmpty(oldUser.getLanguage()))
			oldUser.setLanguage(newUser.getLanguage());
		if (StringUtils.isEmpty(oldUser.getLastName()))
			oldUser.setLastName(newUser.getLastName());
		if (StringUtils.isEmpty(oldUser.getMaritalStatus()))
			oldUser.setMaritalStatus(newUser.getMaritalStatus());
		if (StringUtils.isEmpty(oldUser.getMobileNumber()))
			oldUser.setMobileNumber(newUser.getMobileNumber());
		if (StringUtils.isEmpty(oldUser.getOtherContactNumbers()))
			oldUser.setOtherContactNumbers(newUser.getOtherContactNumbers());
		if (StringUtils.isEmpty(oldUser.getJobFunction()))
			oldUser.setJobFunction(newUser.getJobFunction());
		if (oldUser.getPastExperienceMonths() == null)
			oldUser.setPastExperienceMonths(newUser.getPastExperienceMonths());
		if (oldUser.getProfileHeadline() == null)
			oldUser.setProfileHeadline(newUser.getProfileHeadline());
		if (StringUtils.isEmpty(oldUser.getProfileImageUrl()))
			oldUser.setProfileImageUrl(newUser.getProfileImageUrl());
		if (StringUtils.isEmpty(oldUser.getSummary()))
			oldUser.setSummary(newUser.getSummary());
		if (StringUtils.isEmpty(oldUser.getSkills()))
			oldUser.setSkills(newUser.getSkills());
		else {
			oldUser.setSkills(getSkillsStringToUpdate(oldUser.getSkills(),
					newUser.getSkills()));
		}
		if (StringUtils.isEmpty(oldUser.getSkillsFoundInWork())) {
			oldUser.setSkillsFoundInWork(newUser.getSkillsFoundInWork());
		} else {
			oldUser.setSkillsFoundInWork(getUniqueSkills(
					oldUser.getSkillsFoundInWork(),
					newUser.getSkillsFoundInWork()));
		}
		if (StringUtils.isEmpty(newUser.getLatestCompanyName()))
			oldUser.setLatestCompanyName(newUser.getLatestCompanyName());

		if (StringUtils.isEmpty(newUser.getProcessedState()))
			oldUser.setProcessedState(newUser.getProcessedState());

		return oldUser;

	}

	private String getUniqueSkills(String oldSkills, String newSkills) {
		if (StringUtils.isEmpty(newSkills))
			return oldSkills;
		Set<String> allSkills = new LinkedHashSet<String>(
				Arrays.asList(oldSkills));
		Arrays.asList(newSkills.split(Delimiter)).stream()
				.forEach(newSkill -> allSkills.add(newSkill));
		StringJoiner joiner = new StringJoiner(",");
		allSkills.stream().forEach(skill -> joiner.add(skill));
		return joiner.toString();
	}

	private String getSkillsStringToUpdate(String alreadyPresentSkills,
			String possibleToAddSkills) {
		List<String> alreadyPresentSkillsList = Arrays
				.asList(alreadyPresentSkills.split(Delimiter));
		List<String> possibleSkillsList = Arrays.asList(possibleToAddSkills
				.split(Delimiter));
		Set<String> allSkills = new LinkedHashSet<String>(
				alreadyPresentSkillsList);
		possibleSkillsList.stream().forEach(skill -> allSkills.add(skill));
		log.info("zzzzz already present skills = " + alreadyPresentSkills);
		log.info("zzzz possible skills = " + possibleToAddSkills);
		log.info("zzzzz set of all skills = " + allSkills);

		StringJoiner joiner = new StringJoiner(",");
		int i = 0;
		for (String s : allSkills) {
			joiner.add(s);
			i++;
			if (i >= SKILLS_SIZE)
				break;
		}
		log.info("zzzzz string of skills = " + joiner.toString());
		return joiner.toString();
	}

	public UserDataModel getUserDataModel(final UserDetail userDetail) {

		return new UserDataModel.Builder(getUserBasicDataModel(userDetail))
				.userEducationDataModels(
						userEducationDetailMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserEducationDetails()))
				.userExperienceDataModels(
						userExperienceDetailsMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserExperinceDetails()))
				.userCertificationDataModels(
						userCertificationDetailMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserCertificationDetails()))
				.userPatentDataModels(
						userPatentDetailsMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserPatentDetails()))
				.userProjectDataModels(
						userProjectDetailMapper
								.getProjectDataModelSet(userDetail
										.getUserProjectDetails()))
				.userPublicationDataModels(
						userPublicationDetailsMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserPublicationDetails())).build();
	}

	public Set<UserDataModel> getUserDataModels(
			@NotNull final List<UserDetail> userDetails) {

		final Set<UserDataModel> userDataModels = Sets.newLinkedHashSet();

		userDetails.stream().forEach(
				uds -> userDataModels.add(getUserDataModel(uds)));

		return userDataModels;
	}

	public UserDataModel getUserDataModelWithExperience(
			final UserDetail userDetail) {
		return new UserDataModel.Builder(getUserBasicDataModel(userDetail))
				.userExperienceDataModels(
						userExperienceDetailsMapper
								.getDataModelSetFromEntitySet(userDetail
										.getUserExperinceDetails())).build();
	}

	public UserDetail getUserDetail(
			final JobseekerRegisterDataModel jobseekerRegisterDataModel) {
		final UserDetail jobseekerDetail = new UserDetail();
		jobseekerDetail.setFirstName(jobseekerRegisterDataModel.getFirstName());
		jobseekerDetail.setLastName(jobseekerRegisterDataModel.getLastName());
		return jobseekerDetail;
	}

	public UserDetail getUserDetail(
			final ExpertRegisterDataModel expertRegisterDataModel) {
		final UserDetail jobseekerDetail = new UserDetail();
		jobseekerDetail.setFirstName(expertRegisterDataModel.getFirstName());
		jobseekerDetail.setLastName(expertRegisterDataModel.getLastName());
		return jobseekerDetail;
	}

	public UserDetail getUserDetail(
			final RecruiterRegisterDataModel recruiterRegisterDataModel) {

		final UserDetail userDetail = new UserDetail();

		userDetail.setFirstName(recruiterRegisterDataModel.getFirstName());
		userDetail.setLastName(recruiterRegisterDataModel.getLastName());
		userDetail.setRecruiterType(recruiterRegisterDataModel
				.getRecruiterType().name());
		userDetail
				.setMobileNumber(recruiterRegisterDataModel.getMobileNumber());
		return userDetail;
	}

	public UserDetail getUserDetail(final Long userId) {
		final UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		return userDetail;
	}
}
