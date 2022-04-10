package com.portal.job.enums;

import java.util.HashMap;
import java.util.Map;

public enum RecruiterType {

	FREE_LANCER("Freelancer"), START_UP("Start-up"), ENTERPRISE("Enterprise"), CONSULTANT(
			"Consultant");

	private final String typeName;
	private static final Map<RecruiterType, String> recruiterTypeToNameMap;

	static {
		recruiterTypeToNameMap = new HashMap<RecruiterType, String>();
		for (final RecruiterType type : values()) {
			recruiterTypeToNameMap.put(type, type.getTypeName());
		}
	}

	private RecruiterType(final String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public static Map<RecruiterType, String> getRecruiterTypeToNameMap() {
		return recruiterTypeToNameMap;
	}

}
