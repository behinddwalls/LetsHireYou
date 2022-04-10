package com.portal.job.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserTestDetail;
import com.portal.job.model.UserTestDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserTestDetailMapper {
	public UserTestDetail getEntityFromDataModel(
			final UserTestDataModel jobSeekerTestDataModel,
			final Long jobSeekerId) throws ParseException {

		UserTestDetail jobseekerTestDetail = new UserTestDetail();
		if (jobSeekerTestDataModel.getTestId() != null) {
			jobseekerTestDetail.setTestId(Long.parseLong(jobSeekerTestDataModel
					.getTestId()));
		}
		jobseekerTestDetail.setTestName(jobSeekerTestDataModel.getTestName());
		jobseekerTestDetail.setTestScore(jobSeekerTestDataModel.getTestScore());
		jobseekerTestDetail.setTestDescription(jobSeekerTestDataModel
				.getTestDescription());

		if (!StringUtils.isEmpty(jobSeekerTestDataModel.getTestDate())) {
			jobseekerTestDetail.setTestDate(DateUtility.getDateFromString(
					jobSeekerTestDataModel.getTestDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		// set the JobSeekerDetail .
		UserDetail jobseekerDetail = new UserDetail();
		jobseekerDetail.setUserId(jobSeekerId);

		jobseekerTestDetail.setUserDetail(jobseekerDetail);

		return jobseekerTestDetail;
	}

	public UserTestDataModel getDataModelFromEntity(
			final UserTestDetail jobseekerTestDetail) {

		UserTestDataModel JobSeekerTestDataModel = new UserTestDataModel();
		JobSeekerTestDataModel.setTestId(String.valueOf(jobseekerTestDetail
				.getTestId()));
		JobSeekerTestDataModel.setTestName(jobseekerTestDetail.getTestName());
		JobSeekerTestDataModel.setTestScore(jobseekerTestDetail.getTestScore());
		JobSeekerTestDataModel.setTestDescription(jobseekerTestDetail
				.getTestDescription());
		if (!StringUtils.isEmpty(jobseekerTestDetail.getTestDate())) {
			JobSeekerTestDataModel.setTestDate(DateUtility.getStringFromDate(
					jobseekerTestDetail.getTestDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		return JobSeekerTestDataModel;
	}

	public List<UserTestDataModel> getDataModelListFromEntityList(
			final List<UserTestDetail> jobseekerTestDetailList) {

		List<UserTestDataModel> jobSeekerTestDataModelList = new ArrayList<UserTestDataModel>();
		for (UserTestDetail jobseekerTestDetail : jobseekerTestDetailList) {
			jobSeekerTestDataModelList
					.add(getDataModelFromEntity(jobseekerTestDetail));
		}
		return jobSeekerTestDataModelList;
	}

}
