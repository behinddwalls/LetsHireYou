package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserCertificationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.model.UserCertificationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserCertificationDetailMapper {

	public UserCertificationDetail getEntityFromDataModel(
			final UserCertificationDataModel certificationDataModel,
			final Long jsId) throws ParseException {
		UserCertificationDetail jsCertificaitonDetail = new UserCertificationDetail();
		// set user detail
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(jsId);
		jsCertificaitonDetail.setUserDetail(userDetail);

		jsCertificaitonDetail.setName(certificationDataModel
				.getCertificationName());
		jsCertificaitonDetail.setUrl(certificationDataModel
				.getCertificationUrl());
		if (!StringUtils.isEmpty(certificationDataModel.getCertificationId()))
			jsCertificaitonDetail.setCertificationId(Long.parseLong(certificationDataModel.getCertificationId()));
		jsCertificaitonDetail.setOrganisationName(certificationDataModel
				.getOrganisationName());
		if (!StringUtils.isEmpty(certificationDataModel
				.getCertificationStartDate())) {
			jsCertificaitonDetail.setStartDate(DateUtility.getDateFromString(
					certificationDataModel.getCertificationStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(certificationDataModel
				.getCertificationEndDate())) {
			jsCertificaitonDetail.setEndDate(DateUtility.getDateFromString(
					certificationDataModel.getCertificationEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));

		}

		return jsCertificaitonDetail;

	}

	public UserCertificationDataModel getDataModelFromEntity(
			final UserCertificationDetail jsCertificationDetail) {
		UserCertificationDataModel certificationDataModel = new UserCertificationDataModel();
		certificationDataModel.setCertificationId(jsCertificationDetail.getCertificationId().toString());
		certificationDataModel.setCertificationUrl(jsCertificationDetail
				.getUrl());
		certificationDataModel.setCertificationName(jsCertificationDetail
				.getName());
		if (!StringUtils.isEmpty(jsCertificationDetail.getStartDate())) {
			certificationDataModel.setCertificationStartDate(DateUtility
					.getStringFromDate(jsCertificationDetail.getStartDate(),
							DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		if (!StringUtils.isEmpty(jsCertificationDetail.getEndDate())) {
			certificationDataModel.setCertificationEndDate(DateUtility
					.getStringFromDate(jsCertificationDetail.getEndDate(),
							DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}

		certificationDataModel.setOrganisationName(jsCertificationDetail
				.getOrganisationName());

		return certificationDataModel;
	}

	public Set<UserCertificationDataModel> getDataModelSetFromEntitySet(
			final Set<UserCertificationDetail> jsCertificationSet) {
		Set<UserCertificationDataModel> certificationDataSet = new HashSet<UserCertificationDataModel>();
		for (UserCertificationDetail jsCertification : jsCertificationSet) {
			certificationDataSet.add(getDataModelFromEntity(jsCertification));
		}
		return certificationDataSet;
	}

	public UserCertificationDataModel getMockCertificationDataModel() {
		return null;
	}
}
