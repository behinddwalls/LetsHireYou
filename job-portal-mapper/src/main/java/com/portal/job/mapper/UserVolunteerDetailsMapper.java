package com.portal.job.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserVolunteerDetail;
import com.portal.job.model.UserVolunteerDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserVolunteerDetailsMapper {

	private static Logger log = LoggerFactory
			.getLogger(UserVolunteerDetailsMapper.class);

	public UserVolunteerDataModel getDataModelFromEntity(
			UserVolunteerDetail jsVolunteerDetail) {
		UserVolunteerDataModel volunteerData = new UserVolunteerDataModel();
		volunteerData.setVolunteerId(jsVolunteerDetail.getVolunteerId()
				.toString());
		volunteerData.setVolunteerCause(jsVolunteerDetail.getCauseName());
		volunteerData.setVolunteerDescription(jsVolunteerDetail
				.getDescription());

		volunteerData.setOrganisationName(jsVolunteerDetail
				.getOrganisationName());
		volunteerData.setRoleName(jsVolunteerDetail.getRoleName());
		if (!StringUtils.isEmpty(jsVolunteerDetail.getEndDate())) {
			volunteerData.setVolunteerEndDate(DateUtility.getStringFromDate(
					jsVolunteerDetail.getEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(jsVolunteerDetail.getStartDate())) {
			volunteerData.setVolunteerStartDate(DateUtility.getStringFromDate(
					jsVolunteerDetail.getStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		return volunteerData;
	}

	public UserVolunteerDetail getEntityFromDataModel(
			UserVolunteerDataModel volunteerData, final Long jsId)
			throws ParseException {
		log.info("zzzzzzz volunteer data = "+ volunteerData.toString());
		UserVolunteerDetail jsVolunteerDetail = new UserVolunteerDetail();
		if (!StringUtils.isEmpty(volunteerData.getVolunteerId())){
			log.info("xxxxx u the fuck am i here");
			jsVolunteerDetail.setVolunteerId(Long.parseLong(volunteerData
					.getVolunteerId()));
		}
			
		// set JobSeekerDetail
		UserDetail jsDetail = new UserDetail();
		jsDetail.setUserId(jsId);
		jsVolunteerDetail.setUserDetail(jsDetail);
		
		jsVolunteerDetail.setOrganisationName(volunteerData
				.getOrganisationName());
		jsVolunteerDetail.setRoleName(volunteerData.getRoleName());

		// should be from drop-down
		jsVolunteerDetail.setCauseName(volunteerData.getVolunteerCause());
		jsVolunteerDetail.setDescription(volunteerData
				.getVolunteerDescription());
		if (!StringUtils.isEmpty(volunteerData.getVolunteerStartDate())) {
			jsVolunteerDetail.setStartDate(DateUtility.getDateFromString(
					volunteerData.getVolunteerStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(volunteerData.getVolunteerEndDate())) {
			jsVolunteerDetail.setEndDate(DateUtility.getDateFromString(
					volunteerData.getVolunteerEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		return jsVolunteerDetail;
	}

	public List<UserVolunteerDataModel> getDataModelListFromEntityList(
			List<UserVolunteerDetail> jsVolunteerDetailList) {
		List<UserVolunteerDataModel> volunteerDataList = new ArrayList<UserVolunteerDataModel>();
		for (UserVolunteerDetail jsVolunteerDetail : jsVolunteerDetailList) {
			volunteerDataList.add(getDataModelFromEntity(jsVolunteerDetail));
		}
		return volunteerDataList;
	}

	// mock
	public UserVolunteerDataModel getMockVolunteerDataModel() {
		return null;
	}

}
