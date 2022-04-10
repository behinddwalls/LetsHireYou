package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserProjectDetail;
import com.portal.job.model.UserProjectDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserProjectDetailMapper {

	public UserProjectDetail getProjecteDetail(
			final UserProjectDataModel userProjectDataModel,@NotNull final Long userId)
			throws ParseException {

		UserProjectDetail jobseekerProjectDetail = new UserProjectDetail();
		if (!StringUtils.isEmpty(userProjectDataModel.getProjectId())) {
			jobseekerProjectDetail.setProjectId(Long
					.parseLong(userProjectDataModel.getProjectId()));
		}
		jobseekerProjectDetail.setProjectName(userProjectDataModel
				.getProjectName());
		jobseekerProjectDetail.setProjectUrl(userProjectDataModel
				.getProjectURL());
		jobseekerProjectDetail.setProjectDescription(userProjectDataModel
				.getProjectDescription());
		if (!StringUtils.isEmpty(userProjectDataModel.getProjectDate())){
			jobseekerProjectDetail.setProjectDate(DateUtility.getDateFromString(
					userProjectDataModel.getProjectDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		
		// set the JobSeekerDetail .
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		jobseekerProjectDetail.setUserDetail(userDetail);

		return jobseekerProjectDetail;
	}

	public UserProjectDataModel getDataModelFromEntity(
			final UserProjectDetail projectDetail) {

		UserProjectDataModel jobSeekerProjectDataModel = new UserProjectDataModel();
		jobSeekerProjectDataModel
				.setProjectName(projectDetail.getProjectName());
		jobSeekerProjectDataModel.setProjectURL(projectDetail.getProjectUrl());
		jobSeekerProjectDataModel.setProjectDescription(projectDetail
				.getProjectDescription());
		jobSeekerProjectDataModel.setProjectId(String.valueOf(projectDetail
				.getProjectId()));
		if (!StringUtils.isEmpty(projectDetail.getProjectDate())) {
			jobSeekerProjectDataModel.setProjectDate(DateUtility
					.getStringFromDate(projectDetail.getProjectDate(),
							DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		return jobSeekerProjectDataModel;
	}

	public Set<UserProjectDataModel> getProjectDataModelSet(
			final Set<UserProjectDetail> projectDetailSet) {

		Set<UserProjectDataModel> projectDataModelSet = new HashSet<UserProjectDataModel>();
		for (UserProjectDetail jobseekerProjectDetail : projectDetailSet) {
			projectDataModelSet
					.add(getDataModelFromEntity(jobseekerProjectDetail));
		}
		return projectDataModelSet;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Mock data //
	public UserProjectDataModel getMockJobSeekerProjectDataModel(String id,
			String prName) {
		UserProjectDataModel jobSeekerProjectDataModel = new UserProjectDataModel();
		if (id != null) {
			jobSeekerProjectDataModel.setProjectId(id);
		}
		jobSeekerProjectDataModel.setProjectName(prName);
		jobSeekerProjectDataModel.setProjectDescription("pr project descr.");
		jobSeekerProjectDataModel.setProjectDate("2012-12-12");
		jobSeekerProjectDataModel.setProjectURL("url");

		return jobSeekerProjectDataModel;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////

	// private methods.
}
