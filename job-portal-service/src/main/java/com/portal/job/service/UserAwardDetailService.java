package com.portal.job.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.UserAwardDetailDao;
import com.portal.job.dao.model.UserAwardDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.mapper.UserAwardDetailMapper;
import com.portal.job.model.UserAwardDataModel;

@Service
public class UserAwardDetailService {

	@Autowired
	private UserAwardDetailDao awardDetailDao;
	@Autowired
	private UserAwardDetailMapper awardDetailMapper; 

	@Transactional
	public Set<UserAwardDataModel> getAwardsByJobseekerId(final Long jobseekerId) {
		Set<UserAwardDetail> awardDetails = new HashSet<UserAwardDetail>( awardDetailDao.getEntitiesByPropertyValue(
				new HashMap<String, UserDetail>() {
					private static final long serialVersionUID = 1L;
					{
						put("userDetail", new UserDetail() {
							private static final long serialVersionUID = 1L;
							{
								setUserId(jobseekerId);
							}
						});
					}
				}, UserAwardDetail.class));
		
		return awardDetailMapper.getDataModelSetFromEntitySet(awardDetails);
	}

	@Transactional
	public UserAwardDataModel getAwardsByAwardId(final Long awardId) {
		UserAwardDetail awardDetail = awardDetailDao.find(awardId);		
		return awardDetailMapper.getDataModelFromEntity(awardDetail);
	}
	
	@Transactional
	public boolean delete(final UserAwardDataModel awardDataModel,Long jsId) throws ParseException{
		UserAwardDetail awardDetail = awardDetailMapper.getEntityFromDataModel(awardDataModel, jsId);
		return awardDetailDao.remove(awardDetail);
		
	}
	
	@Transactional
	public UserAwardDataModel createOrUpdate(final UserAwardDataModel awardDataModel,final Long jsId) throws ParseException{
		UserAwardDetail awardDetail = awardDetailMapper.getEntityFromDataModel(awardDataModel, jsId);
		awardDetailDao.save(awardDetail);
		return awardDetailMapper.getDataModelFromEntity(awardDetail);
	}

}
