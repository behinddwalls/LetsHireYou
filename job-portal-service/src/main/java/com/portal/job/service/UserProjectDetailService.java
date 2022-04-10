package com.portal.job.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.UserProjectDetailDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserProjectDetail;
import com.portal.job.mapper.UserProjectDetailMapper;
import com.portal.job.model.UserProjectDataModel;

@Service
public class UserProjectDetailService {

	@Autowired
	private UserProjectDetailDao userProjectDetailDao;
	@Autowired
	private UserProjectDetailMapper userProjectDetailMapper;

	private static Logger log = LoggerFactory
			.getLogger(UserProjectDetailService.class);

	/**
	 * 
	 * @param jobseekerId
	 * @return
	 */
	@Transactional
	public Set<UserProjectDataModel> getProjectDataModelForUserId(
			final Long userId) {
		return this.userProjectDetailMapper
				.getProjectDataModelSet(new HashSet<UserProjectDetail>(
						(this.userProjectDetailDao.getEntitiesByPropertyValue(
								new HashMap<String, UserDetail>() {
									private static final long serialVersionUID = 1L;
									{
										put("userDetail",
												new UserDetail() {
													private static final long serialVersionUID = 1L;
													{
														setUserId(userId);
													}
												});
									}
								}, UserProjectDetail.class))));
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public Set<UserProjectDataModel> getAllProjectDataModel() {
		return this.userProjectDetailMapper
				.getProjectDataModelSet(new HashSet<UserProjectDetail>(
						this.userProjectDetailDao.findAll()));
	}

	/**
	 * 
	 * @param jobSeekerId
	 * @return Add the project detail and associates it with the
	 *         'JobSeekerDetail'
	 * @throws ParseException 
	 */
	@Transactional
	public UserProjectDataModel addOrUpdate(
			final UserProjectDataModel jobSeekerProjectDataModel,
			@NotNull final Long userId) throws ParseException {
		final UserProjectDetail jobseekerProjectDetail = this.userProjectDetailMapper
				.getProjecteDetail(jobSeekerProjectDataModel, userId);
		this.userProjectDetailDao.save(jobseekerProjectDetail);
		return this.userProjectDetailMapper
				.getDataModelFromEntity(jobseekerProjectDetail);
	}

	/**
	 * 
	 * @param jobSeekerId
	 * @return remove the Particular Project Associated with the JobSeeker
	 * @throws ParseException 
	 */
	@Transactional
	public boolean delete(final UserProjectDataModel jobSeekerProjectDataModel,
			@NotNull final Long userId) throws ParseException {
		final UserProjectDetail jobseekerProjectDetail = this.userProjectDetailMapper
				.getProjecteDetail(jobSeekerProjectDataModel, userId);
		return this.userProjectDetailDao.remove(jobseekerProjectDetail);
	}
	
	@Transactional
	public UserProjectDataModel getProjectDetailById(@NotNull final Long projectId){
		UserProjectDetail projectDetail = userProjectDetailDao.find(projectId);
		return userProjectDetailMapper.getDataModelFromEntity(projectDetail);
	}

}