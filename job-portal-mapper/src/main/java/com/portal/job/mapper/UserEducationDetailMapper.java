package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserEducationDetail;
import com.portal.job.enums.Degree;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserEducationDetailMapper {

	public UserEducationDetail getEntityFromDataModel(
			final UserEducationDataModel educationData, final Long jobSeekerId)
			throws ParseException {
		UserEducationDetail jsEducationDetail = new UserEducationDetail();
		if (!StringUtils.isEmpty(educationData.getEducationId()))
			jsEducationDetail.setEducationId(Long.parseLong(educationData
					.getEducationId()));
		jsEducationDetail.setDescription(educationData.getDescription());
		// degree will be either typeHead or dropDown
		jsEducationDetail.setDegree(educationData.getDegreeValue());

		jsEducationDetail.setMajorSubject(educationData.getMajorSubject());
		// setting the jobSeekerId
		UserDetail jobSeekerDetail = new UserDetail();
		jobSeekerDetail.setUserId(jobSeekerId);
		jsEducationDetail.setUserDetail(jobSeekerDetail);

		// setting the organisationId
		jsEducationDetail
				.setEducationalOrg(educationData.getOrganisationName());
		if (!StringUtils.isEmpty(educationData.getStartDate())) {
			jsEducationDetail.setTimePeriodStart(DateUtility.getDateFromString(
					educationData.getStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(educationData.getEndDate())) {
			jsEducationDetail.setTimePeriodEnd(DateUtility.getDateFromString(
					educationData.getEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		
		jsEducationDetail.setDegreeType(educationData.getDegreeType());

		return jsEducationDetail;
	}

	public UserEducationDataModel getDataModelFromEntity(
			final UserEducationDetail jsEducationDetail) {
		UserEducationDataModel educationData = new UserEducationDataModel();
		educationData.setDegreeValue(jsEducationDetail.getDegree());
		educationData.setDescription(jsEducationDetail.getDescription());
		educationData.setEducationId(jsEducationDetail.getEducationId()
				.toString());
		if (!StringUtils.isEmpty(jsEducationDetail.getTimePeriodStart())) {
			educationData.setStartDate(DateUtility.getStringFromDate(
					jsEducationDetail.getTimePeriodStart(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(jsEducationDetail.getTimePeriodEnd())) {
			educationData.setEndDate(DateUtility.getStringFromDate(
					jsEducationDetail.getTimePeriodEnd(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		educationData.setMajorSubject(jsEducationDetail.getMajorSubject());

		educationData
				.setOrganisationName(jsEducationDetail.getEducationalOrg());
		educationData.setDegreeType(jsEducationDetail.getDegreeType());
		educationData.setOrganisationTier(jsEducationDetail.getOrganisationTier());
		return educationData;
	}

	public Set<UserEducationDataModel> getDataModelSetFromEntitySet(
			final Set<UserEducationDetail> userEducationDetails) {
		Set<UserEducationDataModel> educationDataSet = new HashSet<UserEducationDataModel>();
		for (UserEducationDetail jsEducationDetail : userEducationDetails) {
			educationDataSet.add(getDataModelFromEntity(jsEducationDetail));
		}
		return educationDataSet;
	}

	// Mocks
	public UserEducationDataModel getMockEducationData(String educationId,
			String description, Degree degree) {
		return null;
	}

}
