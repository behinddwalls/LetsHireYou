package com.portal.job.model.engine;

/**
 * 
 * @author pandeysp This DataModel is related to Engine only.
 */
public class EngineUserEducationData {

    /*
     * Depending on the 'kind of University' 'User had been' to pursue the
     * 'educationType' we assign a value 'by looking in the static priority'
     * table of institute. This is used also to decide the 'WEIGHTAGE' .
     */
    private String instituteName;
    /*
     * This field gives the info about whether user education type is of
     * 'Master','Bachelors' , 'P.H.D' etc. This is used by engine to give the
     * 'WEIGHTAGE' to the 'User' having higher degree.
     */
    private String educationType;

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    @Override
    public String toString() {
        return "EngineUserEducationData [instituteTier=" + instituteName + ", educationType=" + educationType + "]";
    }

}
