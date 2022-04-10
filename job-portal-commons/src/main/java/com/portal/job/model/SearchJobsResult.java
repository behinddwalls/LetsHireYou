package com.portal.job.model;

import java.util.List;

/**
 * 
 * @author pandeysp
 *
 */
public class SearchJobsResult {

	private List<JobDataModel> JobDataModelList;
	private Pagination pagination;
	public List<JobDataModel> getJobDataModelList() {
		return JobDataModelList;
	}
	public void setJobDataModelList(List<JobDataModel> jobDataModelList) {
		JobDataModelList = jobDataModelList;
	}
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	@Override
	public String toString() {
		return "SearchJobsResult [JobDataModelList=" + JobDataModelList
				+ ", pagination=" + pagination + "]";
	}
	
	
}
