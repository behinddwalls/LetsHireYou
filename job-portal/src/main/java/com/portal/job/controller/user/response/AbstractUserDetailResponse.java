package com.portal.job.controller.user.response;

import java.util.Map;

public class AbstractUserDetailResponse {
	private String status;
	private Map<String, String> errorMap;

	@Override
	public String toString() {
		return "AbstractUserDetailResponse [status=" + status + ", errorMap="
				+ errorMap + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}

}
