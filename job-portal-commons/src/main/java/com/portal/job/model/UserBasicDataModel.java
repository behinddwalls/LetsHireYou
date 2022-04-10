package com.portal.job.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portal.job.constants.JobPortalConstants;

public class UserBasicDataModel {

	private Long userId;
	private String firstName;
	private String lastName;
	private String gender;
	private String dateOfBirth;
	private String maritalStatus;
	private String profileHeadline;
	private String profileImageUrl;
	private String summary;
	private String interests;
	private String mobileNumber;
	private String otherMobileNumber;
	private String address;
	private String ctc;
	private int instituteTier;
	private Integer pastExperienceMonths;
	private String jobFunction;
	private String industryName;
	private String processedState;
	private String skills;
	private String skillsFoundinWorkHistory;
	private String userResumeLink;
	private boolean hideCtc;
	private boolean isResumeParsed;
	private String recruiterProcessedState;
	private boolean hasProfileChanged;
	private String language;
	private String recruiterType;
	private String latestCompanyName;

	public String getLatestCompanyName() {
		return latestCompanyName;
	}

	public void setLatestCompanyName(String latestCompanyName) {
		this.latestCompanyName = latestCompanyName;
	}

	@Override
	public String toString() {
		return "UserBasicDataModel [userId=" + userId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", dateOfBirth=" + dateOfBirth + ", maritalStatus="
				+ maritalStatus + ", profileHeadline=" + profileHeadline
				+ ", profileImageUrl=" + profileImageUrl + ", summary="
				+ summary + ", interests=" + interests + ", mobileNumber="
				+ mobileNumber + ", otherMobileNumber=" + otherMobileNumber
				+ ", address=" + address + ", ctc=" + ctc + ", instituteTier="
				+ instituteTier + ", pastExperienceMonths="
				+ pastExperienceMonths + ", jobFunction=" + jobFunction
				+ ", industryName=" + industryName + ", processedState="
				+ processedState + ", skills=" + skills
				+ ", skillsFoundinWorkHistory=" + skillsFoundinWorkHistory
				+ ", userResumeLink=" + userResumeLink + ", hideCtc=" + hideCtc
				+ ", isResumeParsed=" + isResumeParsed
				+ ", recruiterProcessedState=" + recruiterProcessedState
				+ ", hasProfileChanged=" + hasProfileChanged + ", language="
				+ language + ", recruiterType=" + recruiterType
				+ ", latestCompanyName=" + latestCompanyName + "]";
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRecruiterType() {
		return recruiterType;
	}

	public void setRecruiterType(String recruiterType) {
		this.recruiterType = recruiterType;
	}

	public String getRecruiterProcessedState() {
		return recruiterProcessedState;
	}

	public UserBasicDataModel(Long userId, String firstName, String lastName,
			String gender, String dateOfBirth, String maritalStatus,
			String profileHeadline, String profileImageUrl, String summary,
			String interests, String mobileNumber, String otherMobileNumber,
			String address, String ctc, int instituteTier,
			Integer pastExperienceMonths, String jobFunction,
			String industryName, String processedState, String userResumeLink,
			boolean hideCtc, boolean isResumeParsed,
			String recruiterProcessedState, boolean hasProfileChanged,
			String language, String recruiterType, String latestCompanyName) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.maritalStatus = maritalStatus;
		this.profileHeadline = profileHeadline;
		this.profileImageUrl = profileImageUrl;
		this.summary = summary;
		this.interests = interests;
		this.mobileNumber = mobileNumber;
		this.otherMobileNumber = otherMobileNumber;
		this.address = address;
		this.ctc = ctc;
		this.instituteTier = instituteTier;
		this.pastExperienceMonths = pastExperienceMonths;
		this.jobFunction = jobFunction;
		this.industryName = industryName;
		this.processedState = processedState;
		this.userResumeLink = userResumeLink;
		this.hideCtc = hideCtc;
		this.isResumeParsed = isResumeParsed;
		this.recruiterProcessedState = recruiterProcessedState;
		this.hasProfileChanged = hasProfileChanged;
		this.latestCompanyName = latestCompanyName;
		this.language = language;
		this.recruiterType = recruiterType;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCtc() {
		return ctc;
	}

	public void setCtc(String ctc) {
		this.ctc = ctc;
	}

	public boolean isFirstNamePresent() {
		return !isEmpty(this.firstName);
	}

	public boolean isLastNamePresent() {
		return !isEmpty(this.lastName);
	}

	public boolean isGenderPresent() {
		return !isEmpty(this.gender);
	}

	public boolean isDOBPresent() {
		return !isEmpty(this.dateOfBirth);
	}

	public boolean isMaritalStatusPresent() {
		return !isEmpty(this.maritalStatus);
	}

	public boolean isMobileNumberPresent() {
		return !isEmpty(this.mobileNumber);
	}

	public boolean isInterestsPresent() {
		return !isEmpty(this.interests);
	}

	public boolean isOtherMobileNumberPresent() {
		return !isEmpty(this.otherMobileNumber);
	}

	private boolean isEmpty(Object o) {
		return o == null;
	}

	public UserBasicDataModel() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long jobseekerId) {
		this.userId = jobseekerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBrith) {
		this.dateOfBirth = dateOfBrith;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getProfileHeadline() {
		return profileHeadline;
	}

	public void setProfileHeadline(String profileHeadline) {
		this.profileHeadline = profileHeadline;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOtherMobileNumber() {
		return otherMobileNumber;
	}

	public void setOtherMobileNumber(String otherMobileNumber) {
		this.otherMobileNumber = otherMobileNumber;
	}

	public int getInstituteTier() {
		return instituteTier;
	}

	public void setInstituteTier(int instituteTier) {
		this.instituteTier = instituteTier;
	}

	public Integer getPastExperienceMonths() {
		return pastExperienceMonths;
	}

	public void setPastExperienceMonths(Integer pastExperienceMonths) {
		this.pastExperienceMonths = pastExperienceMonths;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	@JsonIgnore
	public List<String> getSkillsAsList() {
		return Arrays.asList(skills.split(JobPortalConstants.SKILLS_DELEMITER));
	}

	@JsonIgnore
	public List<String> getSkillsFoundInWorkHistoryAsList() {
		return Arrays.asList(skillsFoundinWorkHistory
				.split(JobPortalConstants.SKILLS_DELEMITER));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((ctc == null) ? 0 : ctc.hashCode());
		result = prime * result
				+ ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((industryName == null) ? 0 : industryName.hashCode());
		result = prime * result + instituteTier;
		result = prime * result
				+ ((interests == null) ? 0 : interests.hashCode());
		result = prime * result
				+ ((jobFunction == null) ? 0 : jobFunction.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
		result = prime * result
				+ ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime
				* result
				+ ((otherMobileNumber == null) ? 0 : otherMobileNumber
						.hashCode());
		result = prime
				* result
				+ ((pastExperienceMonths == null) ? 0 : pastExperienceMonths
						.hashCode());
		result = prime * result
				+ ((processedState == null) ? 0 : processedState.hashCode());
		result = prime * result
				+ ((profileHeadline == null) ? 0 : profileHeadline.hashCode());
		result = prime * result
				+ ((profileImageUrl == null) ? 0 : profileImageUrl.hashCode());
		result = prime * result + ((skills == null) ? 0 : skills.hashCode());
		result = prime
				* result
				+ ((skillsFoundinWorkHistory == null) ? 0
						: skillsFoundinWorkHistory.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userResumeLink == null) ? 0 : userResumeLink.hashCode());
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
		UserBasicDataModel other = (UserBasicDataModel) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (ctc == null) {
			if (other.ctc != null)
				return false;
		} else if (!ctc.equals(other.ctc))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (industryName == null) {
			if (other.industryName != null)
				return false;
		} else if (!industryName.equals(other.industryName))
			return false;
		if (instituteTier != other.instituteTier)
			return false;
		if (interests == null) {
			if (other.interests != null)
				return false;
		} else if (!interests.equals(other.interests))
			return false;
		if (jobFunction == null) {
			if (other.jobFunction != null)
				return false;
		} else if (!jobFunction.equals(other.jobFunction))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (maritalStatus == null) {
			if (other.maritalStatus != null)
				return false;
		} else if (!maritalStatus.equals(other.maritalStatus))
			return false;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (otherMobileNumber == null) {
			if (other.otherMobileNumber != null)
				return false;
		} else if (!otherMobileNumber.equals(other.otherMobileNumber))
			return false;
		if (pastExperienceMonths == null) {
			if (other.pastExperienceMonths != null)
				return false;
		} else if (!pastExperienceMonths.equals(other.pastExperienceMonths))
			return false;
		if (processedState == null) {
			if (other.processedState != null)
				return false;
		} else if (!processedState.equals(other.processedState))
			return false;
		if (profileHeadline == null) {
			if (other.profileHeadline != null)
				return false;
		} else if (!profileHeadline.equals(other.profileHeadline))
			return false;
		if (profileImageUrl == null) {
			if (other.profileImageUrl != null)
				return false;
		} else if (!profileImageUrl.equals(other.profileImageUrl))
			return false;
		if (skills == null) {
			if (other.skills != null)
				return false;
		} else if (!skills.equals(other.skills))
			return false;
		if (skillsFoundinWorkHistory == null) {
			if (other.skillsFoundinWorkHistory != null)
				return false;
		} else if (!skillsFoundinWorkHistory
				.equals(other.skillsFoundinWorkHistory))
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userResumeLink == null) {
			if (other.userResumeLink != null)
				return false;
		} else if (!userResumeLink.equals(other.userResumeLink))
			return false;
		return true;
	}

	public String getProcessedState() {
		return processedState;
	}

	public void setProcessedState(String processedState) {
		this.processedState = processedState;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getSkillsFoundinWorkHistory() {
		return skillsFoundinWorkHistory;
	}

	public void setSkillsFoundinWorkHistory(String skillsFoundinWorkHistory) {
		this.skillsFoundinWorkHistory = skillsFoundinWorkHistory;
	}

	public String getUserResumeLink() {
		return userResumeLink;
	}

	public void setUserResumeLink(String userResumeLink) {
		this.userResumeLink = userResumeLink;
	}

	public boolean isHideCtc() {
		return hideCtc;
	}

	public void setHideCtc(boolean hideCtc) {
		this.hideCtc = hideCtc;
	}

	public boolean isResumeParsed() {
		return isResumeParsed;
	}

	public void setResumeParsed(boolean isResumeParsed) {
		this.isResumeParsed = isResumeParsed;
	}

	public String isRecruiterProcessedState() {
		return recruiterProcessedState;
	}

	public void setRecruiterProcessedState(String recruiterProcessedState) {
		this.recruiterProcessedState = recruiterProcessedState;
	}

	public boolean isHasProfileChanged() {
		return hasProfileChanged;
	}

	public void setHasProfileChanged(boolean hasProfileChanged) {
		this.hasProfileChanged = hasProfileChanged;
	}

}
