package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserAwardDetail;
import com.portal.job.dao.model.UserCertificationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.model.UserAwardDataModel;
import com.portal.job.model.UserCertificationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserAwardDetailMapper {

	public UserAwardDetail getEntityFromDataModel(
			final UserAwardDataModel awardDataModel,
			final Long jsId) throws ParseException {
		UserAwardDetail awardDetail = new UserAwardDetail();
		if(!StringUtils.isEmpty(awardDataModel.getAwardId()))
			awardDetail.setAwardId(Long.parseLong(awardDataModel.getAwardId()));
		if(!StringUtils.isEmpty(awardDataModel.getDate()))
			awardDetail.setDate(DateUtility.getDateFromString(awardDataModel.getDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		awardDetail.setOrganisationName(awardDataModel.getOrganisationName());
		awardDetail.setTitle(awardDataModel.getTitle());
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(jsId);
		awardDetail.setUserDetail(userDetail);
		return awardDetail;		
	}
	
	public UserAwardDataModel getDataModelFromEntity(
			final UserAwardDetail awardDetail) {
		UserAwardDataModel awardDataModel = new UserAwardDataModel();
		awardDataModel.setAwardId(awardDetail.getAwardId().toString());
		awardDataModel.setTitle(awardDetail.getTitle());
		awardDataModel.setOrganisationName(awardDetail.getOrganisationName());
		awardDataModel.setDate(DateUtility.getStringFromDate(awardDetail.getDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		
		return awardDataModel;
		
	}
	
	public Set<UserAwardDataModel> getDataModelSetFromEntitySet(
			final Set<UserAwardDetail> awardDetails) {
		Set<UserAwardDataModel> awardDataModels = new HashSet<UserAwardDataModel>();
		for(UserAwardDetail awardDetail	: awardDetails ){
			awardDataModels.add(getDataModelFromEntity(awardDetail));
		}
		return awardDataModels;		
	}
}
