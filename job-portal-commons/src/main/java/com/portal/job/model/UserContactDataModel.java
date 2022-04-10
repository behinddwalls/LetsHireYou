package com.portal.job.model;

/**
 * @author behinddwalls
 *
 */
public class UserContactDataModel {
    private String emailId;
    private String workEmailId;
    private String mobileNumber;
    private String otherContactDetails;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtherContactDetails() {
        return otherContactDetails;
    }

    public void setOtherContactDetails(String otherContactDetails) {
        this.otherContactDetails = otherContactDetails;
    }

    public String getWorkEmailId() {
        return workEmailId;
    }

    public void setWorkEmailId(String workEmailId) {
        this.workEmailId = workEmailId;
    }
}
