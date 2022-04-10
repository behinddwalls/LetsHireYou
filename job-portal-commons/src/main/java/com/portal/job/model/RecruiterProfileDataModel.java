package com.portal.job.model;

public class RecruiterProfileDataModel {

	private UserBasicDataModel userBasicDataModel;
	private UserExperienceDataModel userExperienceDataModel;

	public UserBasicDataModel getUserBasicDataModel() {
		return userBasicDataModel;
	}

	public void setUserBasicDataModel(UserBasicDataModel userBasicDataModel) {
		this.userBasicDataModel = userBasicDataModel;
	}

	public UserExperienceDataModel getUserExperienceDataModel() {
		return userExperienceDataModel;
	}

	public void setUserExperienceDataModel(
			UserExperienceDataModel userExperienceDataModel) {
		this.userExperienceDataModel = userExperienceDataModel;
	}

	@Override
	public String toString() {
		return "RecruiterProfileDataModel [userBasicDataModel="
				+ userBasicDataModel + ", userExperienceDataModel="
				+ userExperienceDataModel + "]";
	}

}
