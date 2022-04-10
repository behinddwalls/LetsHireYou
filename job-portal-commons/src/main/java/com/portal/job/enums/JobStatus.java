package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum JobStatus {

	ACTIVE("ACTIVE"),
	CLOSED("CLOSED"),
	DRAFT("DRAFT"),
	ALLJOBS("ALLJOBS"),
	OTHER("OTHER");
	
	private final static Map<String,JobStatus> applicationStatusMap =
			new HashMap<String, JobStatus>();
	//initialize it statically.
	static{

		for(JobStatus statueEnum : JobStatus.values()){
			applicationStatusMap.put(statueEnum.getStatus(),
					statueEnum);
		}
	}
	
	private JobStatus(String jobStatus){
		this.jobStatus = jobStatus;
	}

	public String getStatus(){
		return this.jobStatus;
	}
	//members field.
	private String jobStatus;
	
	public static JobStatus getJobStatus(
			final String applicationStatus){
		if(applicationStatusMap.containsKey(applicationStatus)){
			return applicationStatusMap.get(applicationStatus);
		}
		return JobStatus.OTHER;
	}
}
