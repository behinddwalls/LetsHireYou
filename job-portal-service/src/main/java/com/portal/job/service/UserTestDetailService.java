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

import com.portal.job.dao.UserTestDetailsDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserTestDetail;
import com.portal.job.mapper.UserTestDetailMapper;
import com.portal.job.model.UserTestDataModel;

@Service
public class UserTestDetailService {

	@Autowired
	private UserTestDetailsDao userTestDetailsDao;
	@Autowired
	private UserTestDetailMapper userTestDetailMapper;

	private static Logger log = LoggerFactory
			.getLogger(UserTestDetailService.class);

	/**
	 * 
	 * @param jobseekerId
	 * @return
	 */
	@Transactional
	public List<UserTestDataModel> getTestDataModelForUserId(
			final Long userId) {
		return this.userTestDetailMapper
				.getDataModelListFromEntityList(this.userTestDetailsDao
						.getEntitiesByPropertyValue(
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
								}, UserTestDetail.class));
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional
	public List<UserTestDataModel> getAllTestDataModel(){
		return this.userTestDetailMapper.getDataModelListFromEntityList(
				this.userTestDetailsDao.findAll());
	}

	/**
	 * 
	 * @param testName
	 * @return
	 */
	@Transactional
	public List<UserTestDataModel> getTestDataModelListForTestName(
			@NotNull final String testName) {
		return this.userTestDetailMapper
				.getDataModelListFromEntityList(this.userTestDetailsDao
						.getEntitiesByPropertyValue(
								new HashMap<String, Object>() {
									private static final long serialVersionUID = 1L;

									{
										put("testName", testName);
									}
								}, UserTestDetail.class));
	}

	/**
	 * 
	 * @param jobSeekerId
	 * @return Add the project detail and associates it with the
	 *         'JobSeekerDetail'
	 * @throws ParseException 
	 */
	@Transactional
	public UserTestDataModel addOrUpdate(
			final UserTestDataModel testDataModel,
			@NotNull final Long userId) throws ParseException {
		final UserTestDetail jobseekerTestDetail = this.userTestDetailMapper
				.getEntityFromDataModel(testDataModel, userId);
		this.userTestDetailsDao.save(jobseekerTestDetail);
		return this.userTestDetailMapper
				.getDataModelFromEntity(jobseekerTestDetail);
	}

	/**
	 * 
	 * @param jobSeekerId
	 * @return remove the Particular Project Associated with the JobSeeker
	 * @throws ParseException 
	 */
	@Transactional
	public boolean delete(
			final UserTestDataModel jobSeekerTestDataModel,
			@NotNull final Long userId) throws ParseException {
		final UserTestDetail jobseekerTestDetail = this.userTestDetailMapper
				.getEntityFromDataModel(jobSeekerTestDataModel,userId);
		return this.userTestDetailsDao.remove(jobseekerTestDetail);
	}
	
	@Transactional
	public UserTestDataModel getTestDetailById(@NotNull final Long testId){
		UserTestDetail testDetail = userTestDetailsDao.find(testId);
		return userTestDetailMapper.getDataModelFromEntity(testDetail);		
	}

}
