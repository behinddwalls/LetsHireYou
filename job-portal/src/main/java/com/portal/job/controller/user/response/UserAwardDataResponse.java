package com.portal.job.controller.user.response;

import com.portal.job.model.UserAwardDataModel;

public class UserAwardDataResponse extends AbstractUserDetailResponse {

	private UserAwardDataModel awardDataModel;

	@Override
	public String toString() {
		return "UserAwardDataResponse [awardDataModel=" + awardDataModel + super.toString()+ "]";
	}

	public UserAwardDataModel getAwardDataModel() {
		return awardDataModel;
	}

	public void setAwardDataModel(UserAwardDataModel awardDataModel) {
		this.awardDataModel = awardDataModel;
	}
}
