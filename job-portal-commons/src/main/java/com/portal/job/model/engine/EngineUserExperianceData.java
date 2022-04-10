package com.portal.job.model.engine;

/**
 * 
 * @author pandeysp This DataModel is related to Engine only.
 */
public class EngineUserExperianceData {

    private String companyName;
    /*
     * This is used to specify how long 'JobSeeker' was the part of this
     * company.
     */
    private int workTenureInCompany;
    private String workDescription;
    /*
     * This is used to specify the index order in which the user has switched to
     * this company. Greatest value shows that 'this Company' is current company
     * of the Customer.
     */
    private String jobWorkTitle;
    private int switchIndexOfCompanyByUser;


    public int getWorkTenureInCompany() {
        return workTenureInCompany;
    }

    public void setWorkTenureInCompany(int workTenureInCompany) {
        this.workTenureInCompany = workTenureInCompany;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public int getSwitchIndexOfCompanyByUser() {
        return switchIndexOfCompanyByUser;
    }

    public void setSwitchIndexOfCompanyByUser(int switchIndexOfCompanyByUser) {
        this.switchIndexOfCompanyByUser = switchIndexOfCompanyByUser;
    }

    public String getJobWorkTitle() {
        return jobWorkTitle;
    }

    public void setJobWorkTitle(String jobWorkTitle) {
        this.jobWorkTitle = jobWorkTitle;
    }

    @Override
    public String toString() {
        return "EngineUserExperianceData [companyName=" + companyName + ", workTenureInCompany=" + workTenureInCompany
                + ", workDescription=" + workDescription + ", jobWorkTitle=" + jobWorkTitle
                + ", switchIndexOfCompanyByUser=" + switchIndexOfCompanyByUser + "]";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
