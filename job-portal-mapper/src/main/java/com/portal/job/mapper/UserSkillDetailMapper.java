package com.portal.job.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.dao.model.SkillDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.model.SkillDataModel;

@Component
public class UserSkillDetailMapper {
	
	private static final String DELIMETER = ",";	

	
	//TODO -Need to remove the unused methods.
	
	/**
	 * 
	 * @param jobSeekerSkillDataModel
	 * @param userId
	 * @return TODO- Instead of creating the List of UsreDetail , we can use a
	 *         single object of UserDetail.Change it accordingly.
	 */
	public SkillDetail getSkillDetail(
			final SkillDataModel jobSeekerSkillDataModel, final String userId) {
		SkillDetail skillDetail = new SkillDetail();
		if (!StringUtils.isEmpty(jobSeekerSkillDataModel.getSkillId())) {
			skillDetail.setSkillId(Long.valueOf(jobSeekerSkillDataModel
					.getSkillId()));
		}
		skillDetail.setSkillName(jobSeekerSkillDataModel.getSkillName());
		// Set userDetail Id.
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(Long.valueOf(userId));
		//
		Set<UserDetail> userDetailList = new HashSet<UserDetail>();
		userDetailList.add(userDetail);
		return skillDetail;
	}

	/**
	 * 
	 * @param skillDetail
	 * @return
	 */
	public SkillDataModel getSkillDataModel(final SkillDetail skillDetail) {
		SkillDataModel JobSeekerSkillDataModel = new SkillDataModel();
		JobSeekerSkillDataModel.setSkillName(skillDetail.getSkillName());
		JobSeekerSkillDataModel.setSkillId(String.valueOf(skillDetail
				.getSkillId()));
		return JobSeekerSkillDataModel;
	}

	/**
	 * 
	 * @param skillDetailList
	 * @return
	 */
	public Set<SkillDataModel> getSkillDataModelSet(
			final Set<SkillDetail> skillDetailList) {
		Set<SkillDataModel> jobSeekerSkillDataModelList = new HashSet<SkillDataModel>();
		for (SkillDetail skillDetail : skillDetailList) {
			jobSeekerSkillDataModelList.add(getSkillDataModel(skillDetail));
		}
		return jobSeekerSkillDataModelList;
	}

	/**
	 * 
	 * @param skillDataModelList
	 * @param userId
	 * @return
	 */
	public Set<SkillDetail> getSkillDetailSet(
			final List<SkillDataModel> skillDataModelList, final Long userId) {
		// Set userDetail Id.
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		//
		Set<UserDetail> userDetailList = new HashSet<UserDetail>();
		userDetailList.add(userDetail);
		//
		Set<SkillDetail> skillDetailList = new HashSet<SkillDetail>();
		for (SkillDataModel skillDataModel : skillDataModelList) {
			skillDetailList.add(getSkillDetail(skillDataModel, userDetailList));
		}
		return skillDetailList;
	}

	//TODO-
	/**
	 * 
	 * @param skillDataModelList
	 * @param userId
	 * @return
	 */
	public Set<SkillDetail> getSkillDetailSet(
			final Set<SkillDataModel> skillDataModelList) {
	
		Set<SkillDetail> skillDetailSet = new HashSet<SkillDetail>();
		for (SkillDataModel skillDataModel : skillDataModelList) {
			SkillDetail detail = new SkillDetail();
			detail.setSkillId(Long.valueOf(
					skillDataModel.getSkillId()));
			detail.setSkillName(skillDataModel.getSkillName());
			skillDetailSet.add(detail);
		}
		return skillDetailSet;
	}
	
	/*
	 * 
	 */
	private SkillDetail getSkillDetail(
			final SkillDataModel jobSeekerSkillDataModel,
			final Set<UserDetail> userDetailList) {
		SkillDetail skillDetail = new SkillDetail();
		if (!StringUtils.isEmpty(jobSeekerSkillDataModel.getSkillId())) {
			skillDetail.setSkillId(Long.valueOf(jobSeekerSkillDataModel
					.getSkillId()));
		}
		skillDetail.setSkillName(jobSeekerSkillDataModel.getSkillName());
		// add in the skill
		//skillDetail.setUserDetails(userDetailList);
		return skillDetail;
	}

	// //////////////////////// MOCK
	public SkillDataModel getMockSkillDetail(final String skillId,
			final String skillName){
		SkillDataModel skillDataModel  = new SkillDataModel();
		skillDataModel.setSkillId(skillId);
		skillDataModel.setSkillName(skillName);
		return skillDataModel;
	}

}
