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

import com.portal.job.dao.UserPulicationDetailDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserPublicationDetail;
import com.portal.job.mapper.UserPublicationDetailsMapper;
import com.portal.job.model.UserPublicationDataModel;

@Service
public class UserPublicationDetailsService {

	private static Logger log = LoggerFactory
			.getLogger(UserPublicationDetailsService.class);

	@Autowired
	private UserPulicationDetailDao userPulicationDetailDao;
	@Autowired
	private UserPublicationDetailsMapper userPublicationDetailsMapper;

	@Transactional
	public boolean saveUserPublicationDetail(UserPublicationDetail publicationDetail){
		return userPulicationDetailDao.save(publicationDetail);
	}
	
	@Transactional
	public boolean[] saveUserPublicationDetails(List<UserPublicationDetail> publicationDetails){
		return userPulicationDetailDao.save(publicationDetails.toArray(new UserPublicationDetail[0]));
	}
	@Transactional
	public UserPublicationDataModel addOrUpdate(
			final UserPublicationDataModel publicationDataModel,
			final Long userId) throws ParseException {
		UserPublicationDetail userPublicationDetail = this.userPublicationDetailsMapper
				.getEntityFromDataModel(publicationDataModel, userId);
		this.userPulicationDetailDao.save(userPublicationDetail);
		return this.userPublicationDetailsMapper
				.getDataModelFromEntity(userPublicationDetail);
	}

	@Transactional
	public boolean delete(final UserPublicationDataModel publicationDataModel,
			final Long userId) throws ParseException{
		return this.userPulicationDetailDao
				.remove(this.userPublicationDetailsMapper
						.getEntityFromDataModel(publicationDataModel, userId));
	}

	@Transactional
	public Set<UserPublicationDataModel> getAllPublicationDataModels() {
		Set<UserPublicationDetail> userPublicationDetailList = new HashSet<UserPublicationDetail>(
				this.userPulicationDetailDao.findAll());
		return this.userPublicationDetailsMapper
				.getDataModelSetFromEntitySet(userPublicationDetailList);
	}

	@Transactional
	public Set<UserPublicationDataModel> getUserPublicationDataModelByUserId(
			@NotNull final Long userId) {
		final UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		Set<UserPublicationDetail> publicationDetailList = new HashSet<UserPublicationDetail>(
				this.userPulicationDetailDao.getEntitiesByPropertyValue(
						new HashMap<String, UserDetail>() {
							private static final long serialVersionUID = 1L;

							{
								put("userDetail", userDetail);
							}
						}, UserPublicationDetail.class));

		return this.userPublicationDetailsMapper
				.getDataModelSetFromEntitySet(publicationDetailList);
	}
	
	@Transactional
	public UserPublicationDataModel getPublicationDetailById(@NotNull final Long publicationId){
		UserPublicationDetail publicationDetail = userPulicationDetailDao.find(publicationId);
		return userPublicationDetailsMapper.getDataModelFromEntity(publicationDetail);
	}

}
