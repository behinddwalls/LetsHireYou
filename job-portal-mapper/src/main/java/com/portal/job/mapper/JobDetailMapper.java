package com.portal.job.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.ProcessingState;
import com.portal.job.model.JobDataModel;
import com.portal.job.utils.DateUtility;

/**
 * maps the jobDetail to jobDataModel and vice-versa. To be used to add/fetch
 * any information related to a job posted by recruiter.
 * 
 * @author abbhasin
 *
 */
@Component
public class JobDetailMapper {

	private static Logger log = LoggerFactory.getLogger(JobDetailMapper.class);

	@Autowired
	private UserSkillDetailMapper userSkillDataMapper;

	/**
	 * 
	 * @param jobId
	 * @param recruiterId
	 * @return
	 */
	public JobDetail getEntityFromDataModel(final Long jobId,
			final Long recruiterId) {
		JobDetail jobDetail = new JobDetail();
		jobDetail.setJobId(jobId);
		// create the UserDetail
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(recruiterId);
		jobDetail.setUserDetail(userDetail);
		return jobDetail;
	}

	/*
	 * 
	 * Set the jobId,createDate, modifiedDate,expiry
	 */

	public JobDetail getEntityFromDataModel(final JobDataModel jobData,
			final Long recruiterId) throws ParseException {
		JobDetail jobDetail = new JobDetail();
		log.info(" !!!!!! :" + jobData.getJobId());
		if (!StringUtils.isEmpty(jobData.getJobId())) {
			log.info("Indie the Setting of JOBID");
			jobDetail.setJobId(Long.valueOf(jobData.getJobId()));
			jobData.setJobModifiedDate(DateUtility.getStringFromDate(
					new Date(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		} else {
			log.info("Seting the epiry date and create date of Job");
			jobData.setJobStatus("ACTIVE");
			// Set the JOB CREATE date.
			jobData.setJobCreatedDate(DateUtility.getStringFromDate(new Date(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
			// Set modified value
			jobData.setJobModifiedDate(DateUtility.getStringFromDate(
					new Date(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
			// set the Expiry date. Current put the montth perod of Three
			// Mothres
			// Need to revisit it if we want to increase it.
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 3);
			jobData.setJobExpiaryDate(DateUtility.getStringFromDate(
					calendar.getTime(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		}
		// Set Industry Name
		jobDetail.setIndustryName(jobData.getIndustryName());
		// Populate Organisation Detail
		jobDetail.setOrganisationName(jobData.getOrganisationName());
		// set Organsation tier.
		jobDetail.setOrganisationTier(jobData.getOrganisationTier());
		jobDetail.setJobDescription(jobData.getJobDescription());
		jobDetail.setJdFileLocation(jobData.getJdName());
		if (jobData.isKeepSalaryHidden()) {
			jobDetail.setKeepSalayHidden((byte) 1);
		} else {
			jobDetail.setKeepSalayHidden((byte) 0);
		}
		if (jobData.isTopTierOnly()) {
			jobDetail.setOnlyTopTierInstitute((byte) 1);
		} else {
			jobDetail.setOnlyTopTierInstitute((byte) 0);
		}
		jobDetail.setJobStatus(jobData.getJobStatus());
		jobDetail.setLocationDetail(jobData.getLocation());
		jobDetail.setMinSalary(Integer.parseInt(jobData.getMinSalary()));
		jobDetail.setMaxSalary(Integer.parseInt(jobData.getMaxSalary()));
		// selected from drop-down
		jobDetail.setSalaryCurrencyCode(jobData.getSalaryCurrencyCode());
		jobDetail.setTitle(jobData.getTitle());
		jobDetail.setJdFileLocation(jobData.getJobDescriptionFileLocation());

		jobDetail.setCreateDate(DateUtility.getDateFromString(
				jobData.getJobCreatedDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		jobDetail.setModifiedDate(DateUtility.getDateFromString(
				jobData.getJobModifiedDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		jobDetail.setExpireDate(DateUtility.getDateFromString(
				jobData.getJobExpiaryDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));

		jobDetail.setEmploymentType(jobData.getEmploymentType());
		jobDetail.setJobFunction(jobData.getJobFunction());
		jobDetail.setLinkToSite(jobData.getLinkToExternalSite());
		// Since we are dealing the JobExperaince everywhere in months
		// we need to convert this value in months.
		jobDetail.setJobExperiance(jobData.getJobExperience() * 12);
		jobDetail.setJobApplicabilityType(jobData.getApplicantSettingTypes());

		// setting the recruiterId
		if (recruiterId != null) {
			UserDetail recruiterDetail = new UserDetail();
			recruiterDetail.setUserId(recruiterId);
			jobDetail.setUserDetail(recruiterDetail);
		}
		log.info("$$ Job detai:" + jobDetail);
		jobDetail.setJobSkills(jobData.getSkills());
		jobDetail.setJobseekerProcessingState(jobData
				.getJobseekerProcessingState());
		return jobDetail;
	}

	public JobDataModel getDataModelFromEntity(final JobDetail jobDetail) {
		JobDataModel jobData = new JobDataModel();
		jobData.setJobId(String.valueOf(jobDetail.getJobId()));
		jobData.setJobDescription(jobDetail.getJobDescription());
		jobData.setIndustryName(jobDetail.getIndustryName());
		jobData.setJdName(jobDetail.getJdFileLocation());
		jobData.setJobStatus(jobDetail.getJobStatus());
		jobData.setJobDescriptionFileLocation(jobDetail.getJdFileLocation());
		jobData.setJobExpiaryDate(DateUtility.getStringFromDate(
				jobDetail.getExpireDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		jobData.setJobCreatedDate(DateUtility.getStringFromDate(
				jobDetail.getCreateDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		jobData.setJobModifiedDate(DateUtility.getStringFromDate(
				jobDetail.getModifiedDate(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		jobData.setTitle(jobDetail.getTitle());
		// change to locationObject
		jobData.setLocation(jobDetail.getLocationDetail());
		jobData.setMaxSalary(new StringBuilder().append(
				jobDetail.getMaxSalary()).toString());
		jobData.setMinSalary(new StringBuilder().append(
				jobDetail.getMinSalary()).toString());
		// Organisation Name
		jobData.setOrganisationName(jobDetail.getOrganisationName());
		// set Organisation Tier.
		jobData.setOrganisationTier(jobDetail.getOrganisationTier());
		jobData.setRecruiterId(jobDetail.getUserDetail().getUserId().toString());

		jobData.setSalaryCurrencyCode(jobDetail.getSalaryCurrencyCode());
		if (jobDetail.getKeepSalayHidden() == 1) {
			jobData.setKeepSalaryHidden(true);
		} else {
			jobData.setKeepSalaryHidden(false);
		}
		if (jobDetail.getOnlyTopTierInstitute() == 1) {
			jobData.setTopTierOnly(true);
		} else {
			jobData.setTopTierOnly(false);
		}

		jobData.setEmploymentType(jobDetail.getEmploymentType());
		jobData.setJobFunction(jobDetail.getJobFunction());
		jobData.setLinkToExternalSite(jobDetail.getLinkToSite());
		// Set Job experiance in Years, As data coming form the DB is in Months
		// and we want to show the experiance in Years to Recruiter.
		jobData.setJobExperience((Integer.valueOf(jobDetail.getJobExperiance())) / 12);
		jobData.setApplicantSettingTypes(jobDetail.getJobApplicabilityType());
		jobData.setSkills(jobDetail.getJobSkills());
		jobData.setJobProcessingState(jobDetail.getJobProcessingState());
		jobData.setLastProcessedTime(jobDetail.getLastProcessedTime());
		jobData.setJobseekerProcessingState(jobDetail
				.getJobseekerProcessingState());

		return jobData;
	}

	public List<JobDetail> getEntityListFromDataModel(
			final List<JobDataModel> jobDataModelList, final Long recruiterId)
			throws ParseException {
		List<JobDetail> jobDetailList = new ArrayList<JobDetail>();
		for (JobDataModel jobDataModel : jobDataModelList) {
			jobDetailList
					.add(getEntityFromDataModel(jobDataModel, recruiterId));
		}
		return jobDetailList;
	}

	public List<JobDataModel> getDataModelListFromEntityList(
			final List<JobDetail> jobDetailList) {
		List<JobDataModel> jobDataList = new ArrayList<JobDataModel>();
		for (JobDetail jobDetail : jobDetailList) {
			jobDataList.add(getDataModelFromEntity(jobDetail));
		}
		return jobDataList;
	}

	// /////////////////// MOCK /////////////////////////

	public JobDataModel getMockJobDataModel(String jobId, String recruiterId,
			String title, String jobDescription, String skills) {
		JobDataModel dataModel = new JobDataModel();
		if (!StringUtils.isEmpty(jobId)) {
			dataModel.setJobId(jobId);
		}
		dataModel.setJobDescription(jobDescription);
		dataModel.setRecruiterId(recruiterId);
		dataModel.setSkills(skills);
		Date after = new Date(new Date().getTime() + 100);
		dataModel.setJobExpiaryDate(DateUtility.getStringFromDate(after));
		//
		dataModel.setTitle(title);
		dataModel.setJobCreatedDate(DateUtility.getStringFromDate(new Date(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		dataModel.setJobExpiaryDate(DateUtility.getStringFromDate(new Date(),
				DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
		dataModel.setMinSalary("3");
		dataModel.setMaxSalary("7");
		dataModel.setJobStatus("ACTIVE");
		dataModel.setOrganisationName("HelloHiring");
		dataModel.setIndustryName("Software");
		dataModel.setSalaryCurrencyCode("USD");
		return dataModel;
	}

}
