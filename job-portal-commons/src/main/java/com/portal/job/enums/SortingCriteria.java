package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum SortingCriteria {

    NEWEST_JOBCREATED("newest"),
    OLDEST_JOBCREATED("oldest"), 
    NAME_A_TO_Z("nameaz"), 
    NAME_Z_TO_A("nameza"), 
    LOCAL_A_TO_Z("locaz"), 
    LOCAL_Z_TO_A("locza"), 
    OTHER("Other");

    private final static Map<String, SortingCriteria> map = new HashMap<String, SortingCriteria>();
    // initialize it statically.
    static {

        for (SortingCriteria statueEnum : SortingCriteria.values()) {
            map.put(statueEnum.getName(), statueEnum);
        }
    }

    private SortingCriteria(final String criteriaName) {
        this.criteriaName = criteriaName;
    }

    // member field
    private String criteriaName;

    public String getName() {
        return this.criteriaName;
    }

    public static SortingCriteria getSortingCriteria(final String applicationStatus) {
        if (map.containsKey(applicationStatus)) {
            return map.get(applicationStatus);
        }
        return SortingCriteria.OTHER;
    }
}
