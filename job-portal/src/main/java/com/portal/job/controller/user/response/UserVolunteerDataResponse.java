package com.portal.job.controller.user.response;

import com.portal.job.model.UserVolunteerDataModel;

public class UserVolunteerDataResponse extends AbstractUserDetailResponse {

	private UserVolunteerDataModel volunteerData;

	public UserVolunteerDataModel getVolunteerData() {
		return volunteerData;
	}

	public void setVolunteerData(UserVolunteerDataModel volunteerData) {
		this.volunteerData = volunteerData;
	}

	@Override
	public String toString() {
		return "UserVolunteerDataResponse [volunteerData=" + volunteerData
				+ super.toString() + "]";
	}
}
