package com.portal.job.model;


public class UserTestDataModel {

	//members variable
	private String testId;
	private String testName;
	private String testScore;
	private String testDescription;
	private String testDate;
	
	
	//Getters and setters
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestScore() {
		return testScore;
	}
	public void setTestScore(String testScore) {
		this.testScore = testScore;
	}
	public String getTestDescription() {
		return testDescription;
	}
	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	
	//other methods.
	@Override
	public String toString() {
		return "JobSeekerTestDataModel [testId=" + testId + ", testName="
				+ testName + ", testScore=" + testScore + ", testDescription="
				+ testDescription + ", testDate=" + testDate + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((testDate == null) ? 0 : testDate.hashCode());
		result = prime * result
				+ ((testDescription == null) ? 0 : testDescription.hashCode());
		result = prime * result + ((testId == null) ? 0 : testId.hashCode());
		result = prime * result
				+ ((testName == null) ? 0 : testName.hashCode());
		result = prime * result
				+ ((testScore == null) ? 0 : testScore.hashCode());
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
		UserTestDataModel other = (UserTestDataModel) obj;
		if (testDate == null) {
			if (other.testDate != null)
				return false;
		} else if (!testDate.equals(other.testDate))
			return false;
		if (testDescription == null) {
			if (other.testDescription != null)
				return false;
		} else if (!testDescription.equals(other.testDescription))
			return false;
		if (testId == null) {
			if (other.testId != null)
				return false;
		} else if (!testId.equals(other.testId))
			return false;
		if (testName == null) {
			if (other.testName != null)
				return false;
		} else if (!testName.equals(other.testName))
			return false;
		if (testScore == null) {
			if (other.testScore != null)
				return false;
		} else if (!testScore.equals(other.testScore))
			return false;
		return true;
	}

	
}
