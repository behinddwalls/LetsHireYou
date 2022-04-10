package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserExperinceDetail;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserExperienceDetailsMapper {
	private static Logger log = LoggerFactory.getLogger(UserExperienceDetailsMapper.class);

	public UserExperienceDataModel getDataModelFromEntity(final UserExperinceDetail jsExperienceDetails) {

		UserExperienceDataModel experienceData = new UserExperienceDataModel();
		experienceData.setCompanyName(jsExperienceDetails.getCompanyName());
		experienceData.setDescription(jsExperienceDetails.getDescription());
		experienceData.setOrganisationTier(jsExperienceDetails.getOrganisationTier());
		experienceData.setTotalExpMonth(jsExperienceDetails.getTotalExpMonth());
		if (jsExperienceDetails.getTimePeriodEnd() != null)
			experienceData.setEndDate(DateUtility.getStringFromDate(jsExperienceDetails.getTimePeriodEnd(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		if (jsExperienceDetails.getTimePeriodStart() != null)
			experienceData.setStartDate(DateUtility.getStringFromDate(jsExperienceDetails.getTimePeriodStart(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		experienceData.setExperienceId(jsExperienceDetails.getExperinceId().toString());
		experienceData.setLocation(jsExperienceDetails.getLocationDetail());
		experienceData.setRoleName(jsExperienceDetails.getRoleName());
		experienceData.setCurrent(
				jsExperienceDetails.getIsCurrentJob() == null || jsExperienceDetails.getIsCurrentJob() == (byte) 0
						? false : true);
		log.info("zzzzz experienceData = " + experienceData.toString());

		return experienceData;
	}

	public UserExperinceDetail getEntityFromDataModel(final UserExperienceDataModel experienceData, final Long jsId)
			throws ParseException {
		UserExperinceDetail jsExperienceDetail = new UserExperinceDetail();
		if (!StringUtils.isEmpty(experienceData.getExperienceId()))
			jsExperienceDetail.setExperinceId(Long.parseLong(experienceData.getExperienceId()));

		jsExperienceDetail.setRoleName(experienceData.getRoleName());

		jsExperienceDetail.setCompanyName(experienceData.getCompanyName());

		jsExperienceDetail.setLocationDetail(experienceData.getLocation());

		jsExperienceDetail.setDescription(experienceData.getDescription());
		// set jobSeeker
		UserDetail jsDetail = new UserDetail();
		jsDetail.setUserId(jsId);
		jsExperienceDetail.setUserDetail(jsDetail);

		if (!StringUtils.isEmpty(experienceData.getStartDate())) {
			log.info("zzzzz trying to parse startDate " + experienceData.getStartDate());
			jsExperienceDetail.setTimePeriodStart(DateUtility.getDateFromString(experienceData.getStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));

		}

		if (!StringUtils.isEmpty(experienceData.getEndDate())) {
			log.info("zzzzz trying to parse EndDate " + experienceData.getEndDate());
			jsExperienceDetail.setTimePeriodEnd(DateUtility.getDateFromString(experienceData.getEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));

		}
		if (!StringUtils.isEmpty(experienceData.getStartDate()) && StringUtils.isEmpty(experienceData.getEndDate())) {
			jsExperienceDetail.setIsCurrentJob((byte) 1);
		}

		return jsExperienceDetail;
	}

	public Set<UserExperienceDataModel> getDataModelSetFromEntitySet(
			final Set<UserExperinceDetail> jsExperienceDetailSet) {
		Set<UserExperienceDataModel> experienceDataSet = new HashSet<UserExperienceDataModel>();
		for (UserExperinceDetail jsExperienceDetail : jsExperienceDetailSet) {
			experienceDataSet.add(getDataModelFromEntity(jsExperienceDetail));
		}
		return experienceDataSet;
	}

	public UserExperienceDataModel getMockExperienceDataModel() {
		return null;
	}
}
