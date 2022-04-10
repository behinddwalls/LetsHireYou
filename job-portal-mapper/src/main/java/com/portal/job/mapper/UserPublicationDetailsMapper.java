package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserPublicationDetail;
import com.portal.job.model.UserPublicationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserPublicationDetailsMapper {

	public UserPublicationDetail getEntityFromDataModel(
			final UserPublicationDataModel userPublicationDataModel,
			final Long jobSeeker) throws ParseException {
		UserPublicationDetail userPublicationDetail = new UserPublicationDetail();
		if (!StringUtils.isEmpty(userPublicationDataModel.getPublicationId())) {
			userPublicationDetail.setPublicationId(Long
					.valueOf(userPublicationDataModel.getPublicationId()));
		}
		if(!StringUtils.isEmpty(userPublicationDataModel.getPublicationDate())){
			userPublicationDetail.setDate(DateUtility.getDateFromString(userPublicationDataModel
					.getPublicationDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
			
		}
		userPublicationDetail.setDescription(userPublicationDataModel
				.getPublicationDescription());
		userPublicationDetail.setPublicationOrganisation(null);
		userPublicationDetail.setTitle(userPublicationDataModel
				.getPublicationTitle());
		// Set userId
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(jobSeeker);
		userPublicationDetail.setUserDetail(userDetail);
		// set organisation, to be set usig typeHead or free text
		userPublicationDetail
				.setPublicationOrganisation(userPublicationDataModel
						.getPublicationOrganisation());

		return userPublicationDetail;
	}

	public UserPublicationDataModel getDataModelFromEntity(
			final UserPublicationDetail publicationDetail) {
		UserPublicationDataModel userPublicationDataModel = new UserPublicationDataModel();
		userPublicationDataModel.setPublicationId(String
				.valueOf(publicationDetail.getPublicationId()));
		userPublicationDataModel.setPublicationOrganisation(publicationDetail
				.getPublicationOrganisation());
		userPublicationDataModel.setPublicationTitle(publicationDetail
				.getTitle());
		if(!StringUtils.isEmpty(publicationDetail.getDate())){
			userPublicationDataModel
			.setPublicationDate(DateUtility.getStringFromDate(publicationDetail.getDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		
		userPublicationDataModel.setPublicationDescription(publicationDetail
				.getDescription());
		return userPublicationDataModel;
	}

	public Set<UserPublicationDataModel> getDataModelSetFromEntitySet(
			final Set<UserPublicationDetail> publicationDetail) {
		Set<UserPublicationDataModel> userPublicationDataModelSet = new HashSet<UserPublicationDataModel>();
		for (UserPublicationDetail userPublicationDetail : publicationDetail) {
			userPublicationDataModelSet
					.add(getDataModelFromEntity(userPublicationDetail));
		}
		return userPublicationDataModelSet;
	}

	// MOCK data for testing ...
	public UserPublicationDataModel getMockUserPublicationDataModel(
			String publicationId, String publicationTitle, String orgId,
			String publicatioDescription) {
		UserPublicationDataModel model = new UserPublicationDataModel();
		model.setPublicationOrganisation("mockOrganisation");
		model.setPublicationDescription(publicatioDescription);
		model.setPublicationTitle(publicationTitle);
		return model;
	}

}
