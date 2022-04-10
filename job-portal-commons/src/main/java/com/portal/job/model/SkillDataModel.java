package com.portal.job.model;

public class SkillDataModel {
	
	//Members fields
	private String skillName;
	private String skillId;
	
	//Getters and Setters.
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getSkillId() {
		return skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	
	//Other Method.
	@Override
	public String toString() {
		return "JobSeekerSkillDataModel [skillName=" + skillName + ", skillId="
				+ skillId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skillId == null) ? 0 : skillId.hashCode());
		result = prime * result
				+ ((skillName == null) ? 0 : skillName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillDataModel other = (SkillDataModel) obj;
		if (skillId == null) {
			if (other.skillId != null)
				return false;
		} else if (!skillId.equals(other.skillId))
			return false;
		if (skillName == null) {
			if (other.skillName != null)
				return false;
		} else if (!skillName.equals(other.skillName))
			return false;
		return true;
	}
}
