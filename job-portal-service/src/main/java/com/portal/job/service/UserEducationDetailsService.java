package com.portal.job.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.UserEducationDetailsDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserEducationDetail;
import com.portal.job.enums.Degree;
import com.portal.job.enums.FieldOfStudy;
import com.portal.job.enums.Tier;
import com.portal.job.mapper.UserEducationDetailMapper;
import com.portal.job.model.UserEducationDataModel;

@Service
public class UserEducationDetailsService {

	private static Logger log = LoggerFactory
			.getLogger(UserEducationDetailsService.class);

	@Autowired
	private UserEducationDetailsDao jobSeekerEducationDetailsDao;
	@Autowired
	UserEducationDetailMapper jsEducationdetailMapper;
	@Autowired
	OrganisationDetailService orgDetailService;
	
	private int getTopMostInstituteTier(List<UserEducationDetail> eduDetails){
		int topTier = 0; // 0 means not found in our organisation list.
		for(UserEducationDetail edu: eduDetails){			
			if(edu.getOrganisationTier()!=null && edu.getOrganisationTier()>0)
				if(topTier!=0 && topTier<edu.getOrganisationTier())
					topTier = edu.getOrganisationTier();
		}
		return topTier;		
	}

	@Transactional
	public int getTopMostInstituteForUser(Long jsId){
		List<UserEducationDetail> educationDetails = getEducationDetailsForUser(jsId);
		return getTopMostInstituteTier(educationDetails);
	}
			
	@Transactional
	public boolean[] saveUserEducationDetails(List<UserEducationDetail> educationDetails){
		for(UserEducationDetail edu : educationDetails){
			int tier = orgDetailService.getOrganisationTier(edu.getEducationalOrg());
			edu.setOrganisationTier(tier);
		}
		return jobSeekerEducationDetailsDao.save(educationDetails.toArray(new UserEducationDetail[0]));
	}
	
	
	@Transactional
	public UserEducationDataModel createOrUpdate(
			final UserEducationDataModel educationData, final Long jsId)
			throws ParseException {
		UserEducationDetail jsEducationDetail = jsEducationdetailMapper
				.getEntityFromDataModel(educationData, jsId);
		jsEducationDetail.setOrganisationTier(orgDetailService.getOrganisationTier(educationData.getOrganisationName()));
		boolean isNew = jobSeekerEducationDetailsDao.save(jsEducationDetail);
		log.info("zzzzzz educationId " + jsEducationDetail.getEducationId());
		if (isNew) {
			log.info("New educationDetail added for JSId " + jsId);
		} else {
			log.info("Updated educationDetail added for JSId " + jsId);
		}
		UserEducationDataModel educationDataResponse = jsEducationdetailMapper
				.getDataModelFromEntity(jsEducationDetail);
		log.info(educationDataResponse.toString());
		return educationDataResponse;

	}
	
	

	@Transactional
	public boolean delete(final UserEducationDataModel educationData,
			final Long jsId) throws ParseException {
		boolean isRemoved = jobSeekerEducationDetailsDao
				.remove(jsEducationdetailMapper.getEntityFromDataModel(
						educationData, jsId));
		if (isRemoved) {
			log.info("educationData removed for jsId" + jsId);
		} else {
			log.info("unable to delete educationData");
		}
		return isRemoved;
	}

	@Transactional
	public Set<UserEducationDataModel> getAllEducationDetails() {
		Set<UserEducationDetail> jsEducationdetailList = new HashSet<UserEducationDetail>(
				jobSeekerEducationDetailsDao.findAll());
		return jsEducationdetailMapper
				.getDataModelSetFromEntitySet(jsEducationdetailList);

	}
	
	@Transactional
	public UserEducationDataModel getEducationDetailById(@NotNull final Long educationId){
		UserEducationDetail educationDetail = jobSeekerEducationDetailsDao.find(educationId);
		return jsEducationdetailMapper.getDataModelFromEntity(educationDetail);
	}

	@Transactional
	public Set<UserEducationDataModel> getAllJobSeekerWithFieldOfStudy(
			@NotNull final String fieldOfStudyEnumName) {
		Set<UserEducationDetail> jsEducationDetails = new HashSet<UserEducationDetail>(
				jobSeekerEducationDetailsDao.getEntitiesByPropertyValue(
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;

							{
								put("fieldOfStudyEnum", FieldOfStudy
										.valueOf(fieldOfStudyEnumName));
							}
						}, UserEducationDetail.class));
		return jsEducationdetailMapper
				.getDataModelSetFromEntitySet(jsEducationDetails);
	}

	@Transactional
	public Set<UserEducationDataModel> getAllJobSeekerWithDegree(
			@NotNull final String degreeEnumName) {
		Set<UserEducationDetail> jsEducationDetails = new HashSet<UserEducationDetail>(
				jobSeekerEducationDetailsDao.getEntitiesByPropertyValue(
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;

							{
								put("degreeCodeEnum",
										Degree.valueOf(degreeEnumName));
							}
						}, UserEducationDetail.class));
		return jsEducationdetailMapper
				.getDataModelSetFromEntitySet(jsEducationDetails);
	}

	@Transactional
	public Set<UserEducationDataModel> getEducationDetailByJobSeekerId(
			@NotNull final Long jsId) {
		final UserDetail js = new UserDetail();
		js.setUserId(jsId);
		Set<UserEducationDetail> jsEducationDetails = new HashSet<UserEducationDetail>(
				jobSeekerEducationDetailsDao.getEntitiesByPropertyValue(
						new HashMap<String, UserDetail>() {
							private static final long serialVersionUID = 1L;

							{
								put("userDetail", js);
							}
						}, UserEducationDetail.class));

		return jsEducationdetailMapper
				.getDataModelSetFromEntitySet(jsEducationDetails);
	}
	
	private List<UserEducationDetail> getEducationDetailsForUser(Long jsId){
		final UserDetail js = new UserDetail();
		js.setUserId(jsId);
		return jobSeekerEducationDetailsDao.getEntitiesByPropertyValue(
				new HashMap<String, UserDetail>() {
					private static final long serialVersionUID = 1L;

					{
						put("userDetail", js);
					}
				}, UserEducationDetail.class);

		
	}

}
