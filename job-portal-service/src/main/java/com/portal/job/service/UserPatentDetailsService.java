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

import com.portal.job.dao.UserPatentDetailsDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserPatentDetail;
import com.portal.job.mapper.UserPatentDetailsMapper;
import com.portal.job.model.UserPatentDataModel;

@Service
public class UserPatentDetailsService {

	private static Logger log = LoggerFactory
			.getLogger(UserPatentDetailsService.class);

	@Autowired
	private UserPatentDetailsDao jobSeekerPatentDetailsDao;
	@Autowired
	UserPatentDetailsMapper jsPatentdetailMapper;

	
	@Transactional
	public boolean saveUserPatentDetail(UserPatentDetail patentDetail){
		return jobSeekerPatentDetailsDao.save(patentDetail);
	}
	
	@Transactional
	public boolean[] saveUserPatentDetails(List<UserPatentDetail> patentDetails){
		return jobSeekerPatentDetailsDao.save(patentDetails.toArray(new UserPatentDetail[0]));
	}
	
	@Transactional
	public UserPatentDataModel createOrUpdate(
			final UserPatentDataModel patentData, final Long jsId)
			throws ParseException {
		UserPatentDetail jsPatentDetail = jsPatentdetailMapper
				.getEntityFromDataModel(patentData, jsId);
		boolean isNew = jobSeekerPatentDetailsDao.save(jsPatentDetail);
		log.info("zzzzzz patentId " + jsPatentDetail.getPatentId());
		if (isNew) {
			log.info("New patentDetail added for JSId " + jsId);
		} else {
			log.info("Updated patentDetail added for JSId " + jsId);
		}
		UserPatentDataModel patentDataResponse = jsPatentdetailMapper
				.getDataModelFromEntity(jsPatentDetail);
		log.info(patentDataResponse.toString());
		return patentDataResponse;

	}

	@Transactional
	public boolean delete(final UserPatentDataModel patentData, final Long jsId)
			throws ParseException {
		boolean isRemoved = jobSeekerPatentDetailsDao
				.remove(jsPatentdetailMapper.getEntityFromDataModel(patentData,
						jsId));
		if (isRemoved) {
			log.info("patentData removed for jsId" + jsId);
		} else {
			log.info("unable to delete patentData");
		}
		return isRemoved;
	}

	@Transactional
	public Set<UserPatentDataModel> getAllPatentDetails() {
		Set<UserPatentDetail> jsPatentdetailList = new HashSet<UserPatentDetail>(
				jobSeekerPatentDetailsDao.findAll());
		return jsPatentdetailMapper
				.getDataModelSetFromEntitySet(jsPatentdetailList);

	}

	@Transactional
	public Set<UserPatentDataModel> getPatentDetailByJobSeekerId(
			@NotNull final Long jsId) {
		final UserDetail js = new UserDetail();
		js.setUserId(jsId);
		Set<UserPatentDetail> jsPatentDetails = new HashSet<UserPatentDetail>(
				jobSeekerPatentDetailsDao.getEntitiesByPropertyValue(
						new HashMap<String, UserDetail>() {
							private static final long serialVersionUID = 1L;

							{
								put("userDetail", js);
							}
						}, UserPatentDetail.class));

		return jsPatentdetailMapper
				.getDataModelSetFromEntitySet(jsPatentDetails);
	}

	@Transactional
	public UserPatentDataModel getPatentDetailById(@NotNull final Long patentId){
		UserPatentDetail patentDetail = jobSeekerPatentDetailsDao.find(patentId);
		return jsPatentdetailMapper.getDataModelFromEntity(patentDetail);
	}

}
