package com.portal.job.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.UserVolunteerDetailDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserVolunteerDetail;
import com.portal.job.mapper.UserVolunteerDetailsMapper;
import com.portal.job.model.UserVolunteerDataModel;

@Service
public class UserVolunteerDetailsService {
	private static Logger log = LoggerFactory
			.getLogger(UserVolunteerDetailsService.class);

	@Autowired
	private UserVolunteerDetailDao jobSeekerVolunteerDetailsDao;
	@Autowired
	UserVolunteerDetailsMapper jsVolunteerdetailMapper;
	
	@Transactional
	public boolean saveUserVolunteerDetail(UserVolunteerDetail volunteerDetail){
		return jobSeekerVolunteerDetailsDao.save(volunteerDetail);
	}
	
	@Transactional 
	public boolean[] saveUserVolunteerDetails(List<UserVolunteerDetail> volunteerDetails){
		return jobSeekerVolunteerDetailsDao.save(volunteerDetails.toArray(new UserVolunteerDetail[0]));
		
	}

	@Transactional
	public UserVolunteerDataModel createOrUpdate(
			final UserVolunteerDataModel volunteerData, final Long jsId)
			throws ParseException {
		log.info("zzzzz called to create or update with VolunteerDate "+volunteerData.toString());
		UserVolunteerDetail jsVolunteerDetail = jsVolunteerdetailMapper
				.getEntityFromDataModel(volunteerData, jsId);
		log.info("zzzzz volunteerDetail obj = "+jsVolunteerDetail.toString());
		boolean isNew = jobSeekerVolunteerDetailsDao.save(jsVolunteerDetail);
		log.info("zzzzzz volunteerId " + jsVolunteerDetail.getVolunteerId());
		if (isNew) {
			log.info("New volunteerDetail added for JSId " + jsId);
		} else {
			log.info("Updated volunteerDetail added for JSId " + jsId);
		}
		UserVolunteerDataModel volunteerDataResponse = jsVolunteerdetailMapper
				.getDataModelFromEntity(jsVolunteerDetail);
		log.info(volunteerDataResponse.toString());
		return volunteerDataResponse;

	}

	@Transactional
	public boolean delete(final UserVolunteerDataModel volunteerData,
			final Long jsId) throws ParseException {
		boolean isRemoved = jobSeekerVolunteerDetailsDao
				.remove(jsVolunteerdetailMapper.getEntityFromDataModel(
						volunteerData, jsId));
		if (isRemoved) {
			log.info("volunteerData removed for jsId" + jsId);
		} else {
			log.info("unable to delete volunteerData");
		}
		return isRemoved;
	}

	@Transactional
	public List<UserVolunteerDataModel> getAllVolunteerDetails() {
		List<UserVolunteerDetail> jsVolunteerdetailList = jobSeekerVolunteerDetailsDao
				.findAll();
		return jsVolunteerdetailMapper
				.getDataModelListFromEntityList(jsVolunteerdetailList);

	}

	@Transactional
	public List<UserVolunteerDataModel> getVolunteerDetailByJobSeekerId(
			@NotNull final Long jsId) {
		final UserDetail js = new UserDetail();
		js.setUserId(jsId);
		List<UserVolunteerDetail> jsVolunteerDetails = jobSeekerVolunteerDetailsDao
				.getEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
					private static final long serialVersionUID = 1L;

					{
						put("userDetail", js);
					}
				}, UserVolunteerDetail.class);

		return jsVolunteerdetailMapper
				.getDataModelListFromEntityList(jsVolunteerDetails);
	}

	@Transactional
	public UserVolunteerDataModel getVolunteerDetailById(
			@NotNull final Long volunteerId) {
		UserVolunteerDetail volunteerDetail = jobSeekerVolunteerDetailsDao
				.find(volunteerId);
		return jsVolunteerdetailMapper.getDataModelFromEntity(volunteerDetail);
	}

}
