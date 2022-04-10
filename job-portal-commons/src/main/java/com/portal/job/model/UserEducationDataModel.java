package com.portal.job.model;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portal.job.constants.DateConstants;
import com.portal.job.utils.DateUtility;

public class UserEducationDataModel {
	private String educationId;
	private String degreeValue;
	private String organisationName;
	private Integer organisationTier;
	private String startDate;
	private String endDate;
	private String majorSubject;
	private String description;
	private String degreeType;

	public Integer getOrganisationTier() {
		return organisationTier;
	}

	public void setOrganisationTier(Integer organisationTier) {
		this.organisationTier = organisationTier;
	}

	public String getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(String degreeType) {
		this.degreeType = degreeType;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getMajorSubject() {
		return majorSubject;
	}

	public void setMajorSubject(String majorSubject) {
		this.majorSubject = majorSubject;
	}

	public UserEducationDataModel(String educationId, String degreeValue,
			String organisationName, String startDate, String endDate,
			String majorSubject, String description, String degreeType) {
		super();
		this.educationId = educationId;
		this.degreeValue = degreeValue;
		this.organisationName = organisationName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.majorSubject = majorSubject;
		this.description = description;
		this.degreeType = degreeType;
	}

	@Override
	public String toString() {
		return "UserEducationDataModel [educationId=" + educationId
				+ ", degreeValue=" + degreeValue + ", organisationName="
				+ organisationName + ", organisationTier=" + organisationTier
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", majorSubject=" + majorSubject + ", description="
				+ description + ", degreeType=" + degreeType + "]";
	}

	public UserEducationDataModel() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEducationId() {
		return educationId;
	}

	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}

	public String getDegreeValue() {
		return degreeValue;
	}

	public void setDegreeValue(String degreeValue) {
		this.degreeValue = degreeValue;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startTimePeriod) {
		this.startDate = startTimePeriod;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endTimePeriod) {
		this.endDate = endTimePeriod;
	}

	// provide the Sorting Criteria.
	// Have Block about some more idea: Read more:
	// http://java67.blogspot.com/2012/10/how-to-sort-object-in-java-comparator-comparable-example.html#ixzz3issAtSyk
	/**
	 * 
	 * @author pandeysp This Sorts the UserEducationDataModel by the 'job start
	 *         data'
	 */
	public static class OrderByLatestEducation implements
			Comparator<UserEducationDataModel> {
		private static final Logger log = LoggerFactory.getLogger(OrderByLatestEducation.class);

		@Override
		public int compare(UserEducationDataModel o1, UserEducationDataModel o2) {
			try {
				if (!StringUtils.isEmpty(o1.getStartDate())
						&& !StringUtils.isEmpty(o2.getStartDate())) {
					final Date d1 = DateUtility.getDateFromString(
							o1.getStartDate(),
							DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
					final Date d2 = DateUtility.getDateFromString(
							o2.getStartDate(),
							DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
					return d1.compareTo(d2);
				}
			} catch (Exception e) {
				log.error("Failed to parse compare ", e);
				return -1;
			}

			return 0;
		}
	}

}
