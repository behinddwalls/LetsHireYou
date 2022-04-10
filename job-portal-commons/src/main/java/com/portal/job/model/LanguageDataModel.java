package com.portal.job.model;

public class LanguageDataModel {

	//Member fields
	private String languageId;
	private String languageName;
	
	//Getters and setters
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	
	//Other Methods.
	@Override
	public String toString() {
		return "JobSeekerLanguageDataModel [languageId=" + languageId
				+ ", languageName=" + languageName + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result
				+ ((languageName == null) ? 0 : languageName.hashCode());
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
		LanguageDataModel other = (LanguageDataModel) obj;
		if (languageId == null) {
			if (other.languageId != null)
				return false;
		} else if (!languageId.equals(other.languageId))
			return false;
		if (languageName == null) {
			if (other.languageName != null)
				return false;
		} else if (!languageName.equals(other.languageName))
			return false;
		return true;
	}
	
}
