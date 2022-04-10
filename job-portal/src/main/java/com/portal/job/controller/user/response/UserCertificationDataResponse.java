package com.portal.job.controller.user.response;

import com.portal.job.model.UserCertificationDataModel;

public class UserCertificationDataResponse  extends AbstractUserDetailResponse{

	private UserCertificationDataModel userCertifications;

	public UserCertificationDataModel getUserCertifications() {
		return userCertifications;
	}

	public void setUserCertifications(UserCertificationDataModel userCertifications) {
		this.userCertifications = userCertifications;
	}

	@Override
	public String toString() {
		return "UserCertificationDataResponse [userCertifications="
				+ userCertifications + super.toString()+ "]";
	}
	
}
