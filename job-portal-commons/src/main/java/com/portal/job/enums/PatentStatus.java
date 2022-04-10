package com.portal.job.enums;




public enum PatentStatus {
	Pending("Patent Pending"),
	Issued("Patent Issued");
	
	private String value;
	
	private PatentStatus(final String value) {
		this.value = value;
	}
	
	public String getPatentStatus(){
		return this.value;
	}
	
	public static PatentStatus fromValue(final String value){
		for (PatentStatus p: PatentStatus.values()) {
            if (p.value.equals(value)) {
                return p;
            }
        }
        throw new IllegalArgumentException(value);
	}

}
