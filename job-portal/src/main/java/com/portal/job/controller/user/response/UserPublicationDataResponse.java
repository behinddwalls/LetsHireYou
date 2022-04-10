package com.portal.job.controller.user.response;

import com.portal.job.model.UserPublicationDataModel;

public class UserPublicationDataResponse extends AbstractUserDetailResponse{
	private UserPublicationDataModel publicationData;

	@Override
	public String toString() {
		return "UserPublicationDataResponse [publicationData="
				+ publicationData + super.toString()+ "]";
	}

	public UserPublicationDataModel getPublicationData() {
		return publicationData;
	}

	public void setPublicationData(UserPublicationDataModel publicationData) {
		this.publicationData = publicationData;
	}

}
