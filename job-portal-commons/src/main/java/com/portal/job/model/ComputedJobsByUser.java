package com.portal.job.model;

import java.util.Map;

/**
 * 
 * @author pandeysp
 *
 *         Provides a wrapper for Data returned form 'Engine'. Basically data
 *         coming form Engine is having the UserId and Map of Recommended JobId
 *         and Corresponding matching suitability Percentage of Jobs with the
 *         'UserData'.
 * 
 */
public class ComputedJobsByUser {

    private Map<Long, String> jobIdToPercentageMap;
    private Long userId;

    public Map<Long, String> getJobIdToPercentageMap() {
        return jobIdToPercentageMap;
    }

    public void setJobIdToPercentageMap(Map<Long, String> jobIdToPercentageMap) {
        this.jobIdToPercentageMap = jobIdToPercentageMap;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ComputedJobsByUser [jobIdToPercentageMap=" + jobIdToPercentageMap + ", userId=" + userId + "]";
    }

}
