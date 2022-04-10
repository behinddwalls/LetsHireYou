package com.portal.job.mapper.resumeparsing.xmlparsing;

import java.util.HashMap;
import java.util.Map;

import com.portal.job.enums.Gender;
import com.portal.job.enums.MaritalStatus;
import com.portal.job.enums.PatentStatus;
import com.portal.job.resumeparsing.xmlparsing.model.PatentStatusTypeTypes;
import com.portal.job.resumeparsing.xmlparsing.userArea.model.GenderEnum;
import com.portal.job.resumeparsing.xmlparsing.userArea.model.MaritalStatusEnum;

public class SovrenToJobseekerModelMapper {
	private static Map<PatentStatusTypeTypes, PatentStatus> sovrenToJobseekerPatentStatusMap = new HashMap<PatentStatusTypeTypes, PatentStatus>();
	static{
		sovrenToJobseekerPatentStatusMap.put(PatentStatusTypeTypes.PATENT_ISSUED, PatentStatus.Issued);
		sovrenToJobseekerPatentStatusMap.put(PatentStatusTypeTypes.PATENT_FILED, PatentStatus.Pending);
		sovrenToJobseekerPatentStatusMap.put(PatentStatusTypeTypes.PATENT_PENDING, PatentStatus.Pending);
	}
	private static Map<GenderEnum,Gender> sovrenGenderTypeToJobseekerGender = new HashMap<GenderEnum, Gender>();
	static{
		sovrenGenderTypeToJobseekerGender.put(GenderEnum.MALE, Gender.Male);
		sovrenGenderTypeToJobseekerGender.put(GenderEnum.FEMALE, Gender.Female);
		sovrenGenderTypeToJobseekerGender.put(GenderEnum.UNKNOWN, Gender.Unkown);
	}
	private static Map<MaritalStatusEnum,MaritalStatus> sovrenMaritalStatusToJobseekerMaritalStatus = new HashMap<MaritalStatusEnum, MaritalStatus>();
	static{
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.MARRIED, MaritalStatus.Married);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.CIVIL_UNION, MaritalStatus.Unkown);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.COHABITATING, MaritalStatus.Unkown);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.DIVORCED, MaritalStatus.Single);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.SEPARATED, MaritalStatus.Single);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.SINGLE, MaritalStatus.Single);
		sovrenMaritalStatusToJobseekerMaritalStatus.put(MaritalStatusEnum.UNKNOWN, MaritalStatus.Single);
	}
	private SovrenToJobseekerModelMapper(){				
	}
	
	public static Map<PatentStatusTypeTypes,PatentStatus> getPatentStatusMap(){		
	
		return sovrenToJobseekerPatentStatusMap;
	}
	
	public static Map<GenderEnum,Gender> getGenderMap(){
		return sovrenGenderTypeToJobseekerGender;
		
	}
	
	public static Map<MaritalStatusEnum,MaritalStatus> getMaritalStatusMap(){
		return sovrenMaritalStatusToJobseekerMaritalStatus;
	}

}
