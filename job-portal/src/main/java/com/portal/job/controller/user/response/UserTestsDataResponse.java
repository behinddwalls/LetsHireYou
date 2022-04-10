package com.portal.job.controller.user.response;

import com.portal.job.model.UserTestDataModel;

public class UserTestsDataResponse extends AbstractUserDetailResponse{

	private UserTestDataModel userTest;

	@Override
	public String toString() {
		return "UserTestsDataResponse [userTest=" + userTest + super.toString()+"]";
	}

	public UserTestDataModel getUserTest() {
		return userTest;
	}

	public void setUserTest(UserTestDataModel userTest) {
		this.userTest = userTest;
	}
}
