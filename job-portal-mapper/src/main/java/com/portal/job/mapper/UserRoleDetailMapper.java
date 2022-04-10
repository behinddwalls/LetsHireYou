package com.portal.job.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.dao.model.RoleDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.model.RoleDataModel;

@Component
public class UserRoleDetailMapper {

	public RoleDetail getRoleDetail(
			final RoleDataModel userRoleDataModel,
			final Long jobSeekerId) {
		RoleDetail roleDetail = new RoleDetail();
		if(!StringUtils.isEmpty(userRoleDataModel.getRoleID())){

			roleDetail.setRoleId(Long.valueOf(userRoleDataModel.getRoleID()));
		}
		roleDetail.setRoleName(userRoleDataModel.getRoleName());
		roleDetail.setRoleDesc(userRoleDataModel.getRoleDescription());
		// Add the JobSeekerDetail object in it.
		UserDetail jobseekerDetail = new UserDetail();
		jobseekerDetail.setUserId(jobSeekerId);
		return roleDetail;
	}

	public RoleDataModel getRoleDataModel(
			final RoleDetail roleDetail) {
		RoleDataModel jobSeekerRoleDataModel = new RoleDataModel();
		jobSeekerRoleDataModel.setRoleName(roleDetail.getRoleName());
		jobSeekerRoleDataModel
				.setRoleID(String.valueOf(roleDetail.getRoleId()));
		return jobSeekerRoleDataModel;
	}

	public List<RoleDataModel> getRoleDataModelList(
			final List<RoleDetail> roleDetailList) {
		List<RoleDataModel> jobSeekerSkillDataModelList = new ArrayList<RoleDataModel>();
		for (RoleDetail skillDetail : roleDetailList) {
			jobSeekerSkillDataModelList
					.add(getRoleDataModel(skillDetail));
		}
		return jobSeekerSkillDataModelList;
	}

	// private method
	
	////////////////  MOCK

}
