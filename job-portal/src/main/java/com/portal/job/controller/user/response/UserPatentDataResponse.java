package com.portal.job.controller.user.response;

import com.portal.job.model.UserPatentDataModel;

public class UserPatentDataResponse extends AbstractUserDetailResponse{

	private UserPatentDataModel userPatent;

	@Override
	public String toString() {
		return "UserPatentDataResponse [userPatent=" + userPatent + super.toString()+ "]";
	}

	public UserPatentDataModel getUserPatent() {
		return userPatent;
	}

	public void setUserPatent(UserPatentDataModel userPatent) {
		this.userPatent = userPatent;
	}
}
