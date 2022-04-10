package com.portal.job.controller.user.response;

import com.portal.job.model.UserEducationDataModel;

public class UserEducationDataResponse extends AbstractUserDetailResponse{
	
	private UserEducationDataModel educationData;
//	private String status;
//	private Map<String, String> errorMap;

	public UserEducationDataModel getEducationData() {
		return educationData;
	}

	public void setEducationData(UserEducationDataModel educationData) {
		this.educationData = educationData;
	}

	@Override
	public String toString() {
		return "UserEducationDataResponse [educationData=" + educationData
				+ super.toString() + "]" ;
	}

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public Map<String, String> getErrorMap() {
//		return errorMap;
//	}
//
//	public void setErrorMap(Map<String, String> errorMap) {
//		this.errorMap = errorMap;
//	}
	


}
