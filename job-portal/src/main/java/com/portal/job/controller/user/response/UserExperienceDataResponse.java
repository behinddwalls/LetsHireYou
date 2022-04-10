package com.portal.job.controller.user.response;

import com.portal.job.model.UserExperienceDataModel;

public class UserExperienceDataResponse extends AbstractUserDetailResponse {

	private UserExperienceDataModel userExperience;

	public UserExperienceDataModel getUserExperience() {
		return userExperience;
	}

	public void setUserExperience(UserExperienceDataModel userExperience) {
		this.userExperience = userExperience;
	}

	@Override
	public String toString() {
		return "UserExpereinceDataResponse [userExperience=" + userExperience
				+ super.toString() +"]";
	}
}
