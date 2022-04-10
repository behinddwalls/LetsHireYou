package com.portal.job.model;

public class RoleDataModel {

	//Member fields
	private String roleName;
	private String roleDescription;
	private String roleID;
	
	//Getters and Setters
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	
	//Other Methods.
	@Override
	public String toString() {
		return "JobSeekerRoleDataModel [roleName=" + roleName
				+ ", roleDescription=" + roleDescription + ", roleID=" + roleID
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleDescription == null) ? 0 : roleDescription.hashCode());
		result = prime * result + ((roleID == null) ? 0 : roleID.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
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
		RoleDataModel other = (RoleDataModel) obj;
		if (roleDescription == null) {
			if (other.roleDescription != null)
				return false;
		} else if (!roleDescription.equals(other.roleDescription))
			return false;
		if (roleID == null) {
			if (other.roleID != null)
				return false;
		} else if (!roleID.equals(other.roleID))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
}

