package com.portal.job.services.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.portal.job.dao.mongodb.UsersByJobRepository;
import com.portal.job.dao.mongodb.model.Job;
import com.portal.job.dao.mongodb.model.User;
import com.portal.job.dao.mongodb.model.UserByJob;
import com.portal.job.model.ComputedUsersByJob;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.UserDetailService;

/**
 * @author behinddwalls
 *
 */
@Service
public class EngineDataStoreService {

	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private UsersByJobRepository usersByJobRepository;

	public List<UserByJob> insertUsersByJob(
			final ComputedUsersByJob computedUsersByJob,
			final Set<UserDataModel> userDataModels,
			final JobDataModel jobDataModel) {

		if (null == userDataModels || userDataModels.isEmpty()
				|| null == jobDataModel
				|| StringUtils.isEmpty(jobDataModel.getJobId())) {
			return new ArrayList<UserByJob>();
		}

		final Map<Long, UserDataModel> filteredUserDataModelMap = Maps
				.newHashMap();

		// put into map
		userDataModels.stream().forEach(
				user -> {
					filteredUserDataModelMap.put(user.getUserBasicDataModel()
							.getUserId(), user);
				});

		final Map<Long, String> userIdToPercentageMap = computedUsersByJob
				.getUserIdToPercentageMap();
		final Set<UserDataModel> userSet = Sets.newHashSet();
		// filter the resultset
		filteredUserDataModelMap.forEach((key, value) -> {
			if (userIdToPercentageMap.containsKey(key)) {
				userSet.add(value);
			}
		});

		final List<UserByJob> usersByJobs = Lists.newArrayList();

		Job job = new Job();
		job.setJobId(Long.valueOf(jobDataModel.getJobId()));
		job.setTitle(jobDataModel.getTitle());
		job.setCompany(jobDataModel.getOrganisationName());
		job.setIndustry(jobDataModel.getIndustryName());
		job.setSalaryCurrency(jobDataModel.getSalaryCurrencyCode());
		job.setJobStatus(jobDataModel.getJobStatus());
		job.setMinSalary(jobDataModel.getMinSalary());
		job.setMaxSalary(jobDataModel.getMaxSalary());
		job.setLocation(jobDataModel.getLocation());

		for (final UserDataModel userDataModel : userSet) {

			User user = new User();
			user.setUserId(userDataModel.getUserBasicDataModel().getUserId());
			user.setFirstName(userDataModel.getUserBasicDataModel()
					.getFirstName());
			user.setLastName(userDataModel.getUserBasicDataModel()
					.getLastName());
			user.setProfileHeadline(userDataModel.getUserBasicDataModel()
					.getProfileHeadline());
			user.setCurrentSalary(userDataModel.getUserBasicDataModel()
					.getCtc());
			user.setExperience(String.valueOf(userDataModel
					.getUserBasicDataModel().getPastExperienceMonths()));

			if (userDataModel.getUserExperienceDataModels() != null
					&& !userDataModel.getUserExperienceDataModels().isEmpty()) {
				user.setCompany(userDataModel.getUserExperienceDataModels()
						.iterator().next().getCompanyName());
				user.setLocation(userDataModel.getUserExperienceDataModels()
						.iterator().next().getLocation());
			}

			UserByJob userByJob = usersByJobRepository
					.findOneByUser_UserIdAndJob_JobId(user.getUserId(),
							job.getJobId());
			boolean doesUserExist = true;
			if (null == userByJob) {
				userByJob = new UserByJob();
				doesUserExist = false;
			}
			userByJob.setJob(job);
			userByJob.setUser(user);
			userByJob.setPercentageMatch(Double.valueOf(computedUsersByJob
					.getUserIdToPercentageMap().get(user.getUserId())));

			if (doesUserExist) {
				usersByJobRepository.save(userByJob);
			} else {
				usersByJobRepository.insert(userByJob);
			}
			usersByJobs.add(userByJob);
		}
		return usersByJobs;
	}
}
