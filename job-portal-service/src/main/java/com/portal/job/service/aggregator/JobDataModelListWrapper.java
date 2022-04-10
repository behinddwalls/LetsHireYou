package com.portal.job.service.aggregator;

import java.util.LinkedList;

import com.portal.job.model.JobDataModel;

public class JobDataModelListWrapper {
	
	private LinkedList<JobDataModel> jobDataModelList;

	public LinkedList<JobDataModel> getJobDataModelList() {
		return jobDataModelList;
	}

	public void setJobDataModelList(LinkedList<JobDataModel> jobDataModelList) {
		this.jobDataModelList = jobDataModelList;
	}

}
