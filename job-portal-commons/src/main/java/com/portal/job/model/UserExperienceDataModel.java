package com.portal.job.model;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.portal.job.constants.DateConstants;
import com.portal.job.utils.DateUtility;

public class UserExperienceDataModel {

    private static final Logger log = LoggerFactory.getLogger(UserExperienceDataModel.class);

    private String experienceId;
    private String description;
    private String companyName; // the organisation name set using typeHead or a
                                // free field
    private String roleName; // the job title set using typeHead or free field
    private String location;
    private String startDate;
    private String endDate;
    private Long userId;
    private Integer totalExpMonth;
    private Integer organisationTier;
    private boolean isCurrent;

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public UserExperienceDataModel(String experienceId, String description, String companyName, String roleName,
            String location, String startDate, String endDate, Long userId, boolean isCurrent) {
        super();
        this.experienceId = experienceId;
        this.description = description;
        this.companyName = companyName;
        this.roleName = roleName;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.isCurrent = isCurrent;
    }

    public Integer getOrganisationTier() {
        return organisationTier;
    }

    public void setOrganisationTier(Integer organisationTier) {
        this.organisationTier = organisationTier;
    }

    public Integer getTotalExpMonth() {
        return totalExpMonth;
    }

    public void setTotalExpMonth(Integer totalExpMonth) {
        this.totalExpMonth = totalExpMonth;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isDescriptionPresent() {
        return !isEmpty(this.description);
    }

    public boolean isCompanyNamePresent() {
        return !isEmpty(this.companyName);
    }

    public boolean isLocationPresent() {
        return !isEmpty(this.location);
    }

    public boolean isStartDatePresent() {
        return !isEmpty(this.startDate);
    }

    public boolean isEndDatePresent() {
        return !isEmpty(this.endDate);
    }

    private boolean isEmpty(Object o) {
        return o == null;
    }

    public UserExperienceDataModel() {

    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Other methods
    @Override
    public String toString() {
        return "UserExperienceDataModel [experienceId=" + experienceId + ", description=" + description
                + ", companyName=" + companyName + ", roleName=" + roleName + ", location=" + location + ", startDate="
                + startDate + ", endDate=" + endDate + ", userId=" + userId + ", totalExpMonth=" + totalExpMonth
                + ", organisationTier=" + organisationTier + ", isCurrent=" + isCurrent + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((experienceId == null) ? 0 : experienceId.hashCode());
        result = prime * result + (isCurrent ? 1231 : 1237);
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((organisationTier == null) ? 0 : organisationTier.hashCode());
        result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((totalExpMonth == null) ? 0 : totalExpMonth.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        UserExperienceDataModel other = (UserExperienceDataModel) obj;
        if (companyName == null) {
            if (other.companyName != null)
                return false;
        } else if (!companyName.equals(other.companyName))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (experienceId == null) {
            if (other.experienceId != null)
                return false;
        } else if (!experienceId.equals(other.experienceId))
            return false;
        if (isCurrent != other.isCurrent)
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (organisationTier == null) {
            if (other.organisationTier != null)
                return false;
        } else if (!organisationTier.equals(other.organisationTier))
            return false;
        if (roleName == null) {
            if (other.roleName != null)
                return false;
        } else if (!roleName.equals(other.roleName))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (totalExpMonth == null) {
            if (other.totalExpMonth != null)
                return false;
        } else if (!totalExpMonth.equals(other.totalExpMonth))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    // provide the Sorting Criteria.
    // Have Block about some more idea: Read more:
    // http://java67.blogspot.com/2012/10/how-to-sort-object-in-java-comparator-comparable-example.html#ixzz3issAtSyk
    /**
     * 
     * @author pandeysp This Sorts the UserEducationDataModel by the 'job start
     *         data'
     */
    @JsonIgnoreType
    public static class OrderByLatestCompany implements Comparator<UserExperienceDataModel> {

        @Override
        public int compare(UserExperienceDataModel o1, UserExperienceDataModel o2) {
            try {
                if (!StringUtils.isEmpty(o1.getStartDate()) && !StringUtils.isEmpty(o2.getStartDate())) {
                    final Date d1 = DateUtility.getDateFromString(o1.getStartDate(),
                            DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
                    final Date d2 = DateUtility.getDateFromString(o2.getStartDate(),
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
