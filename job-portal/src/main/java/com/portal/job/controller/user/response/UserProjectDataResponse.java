package com.portal.job.controller.user.response;

import com.portal.job.model.UserProjectDataModel;

public class UserProjectDataResponse extends AbstractUserDetailResponse{
		
	private UserProjectDataModel projectData;

	@Override
	public String toString() {
		return "UserProjectDataResponse [projectData=" + projectData
				+ super.toString() + "]";
	}

	public UserProjectDataModel getProjectData() {
		return projectData;
	}

	public void setProjectData(UserProjectDataModel projectData) {
		this.projectData = projectData;
	}

}
