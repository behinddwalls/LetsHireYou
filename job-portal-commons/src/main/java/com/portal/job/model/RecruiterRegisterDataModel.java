package com.portal.job.model;

import com.portal.job.enums.RecruiterType;

public class RecruiterRegisterDataModel extends BaseRegisterDataModel {

	private RecruiterType recruiterType;
	private String mobileNumber;

	public RecruiterType getRecruiterType() {
		return recruiterType;
	}

	public void setRecruiterType(RecruiterType recruiterType) {
		this.recruiterType = recruiterType;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "RecruiterRegisterDataModel [recruiterType=" + recruiterType
				+ ", mobileNumber=" + mobileNumber + "]";
	}
}
