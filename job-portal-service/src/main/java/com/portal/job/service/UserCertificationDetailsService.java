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

import com.portal.job.dao.UserCertificationDetailDao;
import com.portal.job.dao.model.UserCertificationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.mapper.UserCertificationDetailMapper;
import com.portal.job.model.UserCertificationDataModel;

@Service
public class UserCertificationDetailsService {

	private static Logger log = LoggerFactory
			.getLogger(UserCertificationDetailsService.class);

	@Autowired
	private UserCertificationDetailDao jobSeekerCertificationDetailsDao;
	@Autowired
	UserCertificationDetailMapper jsCertificationDetailMapper;

	@Transactional
	public boolean saveUserCertificationDetail(UserCertificationDetail certificationDetail){
		return jobSeekerCertificationDetailsDao.save(certificationDetail);
	}
	
	@Transactional
	public boolean[] saveUserCertificationDetails(List<UserCertificationDetail> certificationDetails){
		return jobSeekerCertificationDetailsDao.save(certificationDetails.toArray(new UserCertificationDetail[0]));
	}
	
	
	@Transactional
	public UserCertificationDataModel createOrUpdate(
			final UserCertificationDataModel certificationData, final Long jsId)
			throws ParseException {
		UserCertificationDetail jsCertificationDetail = jsCertificationDetailMapper
				.getEntityFromDataModel(certificationData, jsId);
		boolean isNew = jobSeekerCertificationDetailsDao
				.save(jsCertificationDetail);
		
		if (isNew) {
			log.info("New certificationDetail added for JSId " + jsId);
		} else {
			log.info("Updated certificationDetail added for JSId " + jsId);
		}
		UserCertificationDataModel certificationDataResponse = jsCertificationDetailMapper
				.getDataModelFromEntity(jsCertificationDetail);
	
		return certificationDataResponse;

	}

	@Transactional
	public boolean delete(final UserCertificationDataModel certificationData,
			final Long jsId) throws ParseException {	
		UserCertificationDetail certificationDetail = jsCertificationDetailMapper.getEntityFromDataModel(
				certificationData, jsId);	
		boolean isRemoved = jobSeekerCertificationDetailsDao
				.remove(certificationDetail);
		if (isRemoved) {
			log.info("certificationData removed for jsId" + jsId);
		} else {
			log.info("unable to delete certificationData");
		}
		return isRemoved;
	}

	@Transactional
	public Set<UserCertificationDataModel> getAllCertificationDetails() {
		Set<UserCertificationDetail> jsCertificationdetailList = new HashSet<UserCertificationDetail>(
				jobSeekerCertificationDetailsDao.findAll());
		return jsCertificationDetailMapper
				.getDataModelSetFromEntitySet(jsCertificationdetailList);
	}

	@Transactional
	public Set<UserCertificationDataModel> getCertificationDetailByJobSeekerId(
			@NotNull final Long jsId) {
		final UserDetail js = new UserDetail();
		js.setUserId(jsId);
		Set<UserCertificationDetail> jsCertificationDetails = new HashSet<UserCertificationDetail>(
				jobSeekerCertificationDetailsDao.getEntitiesByPropertyValue(
						new HashMap<String, UserDetail>() {
							private static final long serialVersionUID = 1L;

					{
						put("userDetail", js);
					}
				}, UserCertificationDetail.class));

		return jsCertificationDetailMapper
				.getDataModelSetFromEntitySet(jsCertificationDetails);
	}
	
	@Transactional
	public UserCertificationDataModel getCertificationDetailById(@NotNull final Long certificationId){
		UserCertificationDetail certificationDetail = jobSeekerCertificationDetailsDao.find(certificationId);
		return jsCertificationDetailMapper.getDataModelFromEntity(certificationDetail);
	}

}
