package com.portal.job.model.engine;

import java.util.List;

/**
 * 
 * @author pandeysp This DataModel is related to Engine only. This carried the
 *         'jobSeeker' Info required by 'Engine' to process the candidate
 *         against the posted 'Job'.
 */
public class EngineUserData {

    private String userId;
    private List<EngineUserEducationData> userEducationList;
    private List<EngineUserExperianceData> userExperianceList;
    private List<String> skills;
    private List<String> skillsFoundInWorkHistory;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<EngineUserEducationData> getUserEducationList() {
        return userEducationList;
    }

    public void setUserEducationList(List<EngineUserEducationData> userEducationList) {
        this.userEducationList = userEducationList;
    }

    public List<EngineUserExperianceData> getUserExperianceList() {
        return userExperianceList;
    }

    public void setUserExperianceList(List<EngineUserExperianceData> userExperianceList) {
        this.userExperianceList = userExperianceList;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSkillsFoundInWorkHistory() {
        return skillsFoundInWorkHistory;
    }

    public void setSkillsFoundInWorkHistory(List<String> skillsFoundInWorkHistory) {
        this.skillsFoundInWorkHistory = skillsFoundInWorkHistory;
    }

    @Override
    public String toString() {
        return "EngineUserData [userId=" + userId + ", userEducationList=" + userEducationList
                + ", userExperianceList=" + userExperianceList + ", skills=" + skills
                + ", skillsFoundInWorkHistory=" + skillsFoundInWorkHistory + "]";
    }

}
