package com.portal.job.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.portal.job.dao.model.OrganisationDetail;
import com.portal.job.model.OrganisationDataModel;

@Component
public class UserOrganisationalDetailMapper {

	public OrganisationDetail getOrganisationalDetail(final OrganisationDataModel jobSeekerOrganisationDataModel,
			final Long jobSeekerId){
	
		OrganisationDetail organisationDetail = new OrganisationDetail();
		organisationDetail.setOrganisationName(jobSeekerOrganisationDataModel.getOrganisationName());
		organisationDetail.setOrganisationId(
				Long.valueOf(jobSeekerOrganisationDataModel.getOrganisationId()));
	//	organisationDetail.setIndustryCodeDetail(jobSeekerOrganisationDataModel.getOrganisationId());
		
		return organisationDetail;
	}
	
	public OrganisationDataModel getOrganisationDataModel
	(final OrganisationDetail organisationDetail){
	
		OrganisationDataModel jobSeekerOrganisationDataModel = 
				new OrganisationDataModel();
		jobSeekerOrganisationDataModel.setOrganisationName(
				organisationDetail.getOrganisationName());
		jobSeekerOrganisationDataModel.setOrganisationId(
				String.valueOf(organisationDetail.getOrganisationId()));
	//	organisationDetail.setIndustryCodeDetail(jobSeekerOrganisationDataModel.getOrganisationId());
		
		return jobSeekerOrganisationDataModel;
	}
	
	public List<OrganisationDataModel> getJobSeekerOrganisationDataModelList(
			final List<OrganisationDetail> organisationDetailList){
		List<OrganisationDataModel> jobSeekerOrganisationDataModelList = 
				new ArrayList<OrganisationDataModel>();		
		for(OrganisationDetail organisationDetail :  organisationDetailList){
			jobSeekerOrganisationDataModelList.add(getOrganisationDataModel(organisationDetail));
		}		
		return jobSeekerOrganisationDataModelList;
	}
}
