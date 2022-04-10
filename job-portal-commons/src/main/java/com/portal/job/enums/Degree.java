package com.portal.job.enums;

public enum Degree {
	A("Associate's degree"),
	AA("Associate of Arts (AA)"),
	AAS("Associate of Arts and Sciences (AAS)"),
	AS("Associate of Science (AS)"),
	B("Bachelor's degree"),
	BAS("Bachelor of Applied Science (BASc)"),
	BARCH("Bachelor of Architecture (BArch)"),
	BA("Bachelor of Arts (BA)"),
	BBA("Bachelor of Business Administration (BBA)"),
	BCOM("Bachelor of Commerce (BCom)"),
	BED("Bachelor of Education (BEd)"),
	BE("Bachelor of Engineering (BE)"),
	BFA("Bachelor of Fine Arts (BFA)"),
	LLB("Bachelor of Laws (LLB)"),
	MBBS("Bachelor of Medicine, Bachelor of Surgery (MBBS)"),
	BPHARM("Bachelor of Pharmacy (BPharm)"),
	BS("Bachelor of Science (BS)"),
	BSC("Bachelor of Science (BSc)"),
	BTECH("Bachelor of Technology (BTech)"),
	EDD("Doctor of Education (EdD)"),
	JS("Doctor of Law (JD)"),
	MD("Doctor of Medicine (MD)"),
	PHARMD("Doctor of Pharmacy (PharmD)"),
	PHD("Doctor of Philosophy (PhD)"),
	E("Engineer's degree"),
	F("Foundation degree"),
	L("Licentiate degree"),
	M("Master's degree"),
	MARCH("Master of Architecture (MArch)"),
	MA("Master of Arts (MA)"),
	MBA("Master of Business Administration (MBA)"),
	MCA("Master of Computer Applications (MCA)"),
	MDIV("Master of Divinity (MDiv)"),
	MED("Master of Education (MEd)"),
	ME("Master of Engineering (ME)"),
	MFA("Master of Fine Arts (MFA)"),
	LLM("Master of Laws (LLM)"),
	MLIS("Master of Library & Information Science (MLIS)"),
	MPHIL("Master of Philosophy (MPhil)"),
	MPA("Master of Public Administration (MPA)"),
	MPH("Master of Public Health (MPH)"),
	MS("Master of Science (MS)"),
	MSC("Master of Science (MSc)"),
	MSW("Master of Social Work (MSW)"),
	MTECH("Master of Technology (MTech)"),
	RD("Research Doctorate"),
	HS("High School"),
	IM("Intermediate"),
	SSC("Senior Secondary School (SSC)"),
	HSC("Higher Secondary School (HSC)");
	
	private Degree(final String degreeName){
		this.degreeName = degreeName;
	}
	
	private String degreeName;
	
	public String getDegreeName(){
		return this.degreeName;
	}
	
	

}
