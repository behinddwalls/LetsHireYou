package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum JobFunctionType {

    Accounting("Accounting/Auditing"), Administrative("Administrative"), Advertising("Advertising"), Art("Art/Creative"), BusinessDevelopment(
            "Business Development"), Consulting("Consulting"), Customer("Customer Service"), Distribution(
            "Distribution"), Design("Design"), Education("Education"), Engineering("Engineering"), Finance("Finance"), General(
            "General Business"), Human("Human Resources"), Information("Information Technology"), Legal("Legal"), Management(
            "Management"), Manufacturing("Manufacturing"), Marketing("Marketing"), Purchasing("Purchasing"), Public(
            "Public Relations"), Product("Product Management"), Project("Project Management"), Quality("Quality"), Research(
            "Research"), Sales("Sales"), Science("Science"), Strategy("Strategy/Planning"), Supply("Supply Chain"), Training(
            "Training"), Writing("Writing/Editing"), Health("Health Care Provider");
    
    
    private final static Map<String, JobFunctionType> jobFunctionTypeMap = new HashMap<String, JobFunctionType>();
    // initialize it statically.
    static {

        for (JobFunctionType statueEnum : JobFunctionType.values()) {
            jobFunctionTypeMap.put(statueEnum.getEmploymentType(), statueEnum);
        }
    }
    //
    private String jobFunctionType;

    private JobFunctionType(String jobFunctionType) {
        this.jobFunctionType = jobFunctionType;
    }

    public String getEmploymentType() {
        return this.jobFunctionType;
    }

    public static JobFunctionType getJobFunctionType(final String applicationStatus) {
        if (jobFunctionTypeMap.containsKey(applicationStatus)) {
            return jobFunctionTypeMap.get(applicationStatus);
        }
        return null;
    }

}
