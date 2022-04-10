package com.portal.job.model;


public class UserProjectDataModel {

	//Members fields
	private String projectName;
	private String projectURL;
	private String projectDescription;
	private String projectDate;
	private String projectId;
	
	//Getters and setters
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectURL() {
		return projectURL;
	}
	public void setProjectURL(String projectURL) {
		this.projectURL = projectURL;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public String getProjectDate() {
		return projectDate;
	}
	public void setProjectDate(String projectDate) {
		this.projectDate = projectDate;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	//Other method.
	@Override
	public String toString() {
		return "JobSeekerProjectDataModel [projectName=" + projectName
				+ ", projectURL=" + projectURL + ", projectDescription="
				+ projectDescription + ", projectDate=" + projectDate
				+ ", projectId=" + projectId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((projectDate == null) ? 0 : projectDate.hashCode());
		result = prime
				* result
				+ ((projectDescription == null) ? 0 : projectDescription
						.hashCode());
		result = prime * result
				+ ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result
				+ ((projectURL == null) ? 0 : projectURL.hashCode());
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
		UserProjectDataModel other = (UserProjectDataModel) obj;
		if (projectDate == null) {
			if (other.projectDate != null)
				return false;
		} else if (!projectDate.equals(other.projectDate))
			return false;
		if (projectDescription == null) {
			if (other.projectDescription != null)
				return false;
		} else if (!projectDescription.equals(other.projectDescription))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (projectURL == null) {
			if (other.projectURL != null)
				return false;
		} else if (!projectURL.equals(other.projectURL))
			return false;
		return true;
	}
	

	
}
