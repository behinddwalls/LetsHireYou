package com.portal.job.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.portal.job.constants.JobPortalConstants;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.mongodb.model.FailedTask;
import com.portal.job.enums.ProcessingState;
import com.portal.job.enums.TaskState;
import com.portal.job.exceptions.HttpClientException;
import com.portal.job.exceptions.TaskRejectionException;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.model.ComputedJobsByUser;
import com.portal.job.model.UserDataModel;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.model.engine.EngineJobData;
import com.portal.job.model.engine.EngineRequestContext;
import com.portal.job.model.engine.EngineUserData;
import com.portal.job.model.engine.EngineUserEducationData;
import com.portal.job.model.engine.EngineUserExperianceData;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.JobsByUserService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.engine.JobPortalEngineServiceProxy;
import com.portal.job.services.search.JobseekerSearchService;
import com.portal.job.services.task.FailedTaskService;
import com.portal.job.services.task.TaskManagerService;

/**
 * 
 * @author pandeysp
 * 
 *  This class is used to show the 'RecommendedJobs' to Users by calling 
 *  'processing engine'. We pass 'JobSeekerData' and Set<JobSeeker> to engine
 *   by applying some basic filtering at out end. 
 */

/**
 * 
 * @author pandeysp Sample Input :
 * 
 *         {
 * 
 *         engineJobDataSet: [ { jobId: "24", jobTitle: "amazon",
 *         jobDescription: "Working On Prime UOP patformization 3", degree:
 *         "Masters", company_tier: 1, jobSkills: [ "JAVA", "C++", "PHP" ] }, {
 *         jobId: "25", jobTitle: "city", jobDescription:
 *         "Working On Prime UOP patformization 3", degree: "Masters",
 *         company_tier: 1, jobSkills: [ "JAVA", "C++", "PHP" ] } ],
 *         engineUserData: [ { userId: "2", userEducationList: [ {
 *         instituteTier: 1, educationType: "Bachelor" } ], userExperianceList:
 *         [ { companyTier: 1, workTenureInCompany: 12, workDescription:
 *         "Working On Prime UOP patformization 3", jobWorkTitle: "Amazon",
 *         swithIndexOfCompanyByUser: 1 }, { companyTier: 1,
 *         workTenureInCompany: 12, workDescription:
 *         "Working On Prime UOP patformization 2", jobWorkTitle: "City",
 *         swithIndexOfCompanyByUser: 2 }, { companyTier: 1,
 *         workTenureInCompany: 12, workDescription:
 *         "Working On Prime UOP patformization 1", jobWorkTitle: "Cisco",
 *         swithIndexOfCompanyByUser: 3 } ], skills: [ "JAVA", "C++" ],
 *         skillsFoundInWorkHistory: [ ] } ]
 * 
 *         }
 * 
 * 
 * 
 *         Sample Output:
 * 
 *         {
 * 
 *         2: [ { total: 21.43087101353638, jobId: "24" }, { total:
 *         25.93087101353638, jobId: "25" } ]
 * 
 *         }
 *
 */
public class ComputeRecommendedJobsForCandidateTask extends
		ListenableTask<Void> {

	private static final Logger log = LoggerFactory
			.getLogger(ComputeRecommendedJobsForCandidateTask.class);

	final private JobseekerSearchService jobseekerSearchService;
	final private UserDetailService userDetailService;
	final private UserDataModel userDataModel;
	final private TaskManagerService taskManagerService;
	final private FailedTaskService failedTaskService;
	private final JobPortalEngineServiceProxy jobPortalEngineServiceProxy;
	private final JobsByUserService jobsByUserService;

	private static final String TASK_ID_PREFIX = "ComputeRecommendedJobsForCandidateTask_";

	public ComputeRecommendedJobsForCandidateTask(
			final JobseekerSearchService jobseekerSearchService,
			final UserDetailService userDetailService,
			final JobsByUserService jobsByUserService,
			final FailedTaskService failedTaskService,
			final UserDataModel userDataModel,
			final ExecutorServiceManager executorServiceManager,
			final TaskManagerService executorTaskManagerService,
			final TaskState state,
			final JobPortalEngineServiceProxy jobPortalEngineServiceProxy) {
		super(executorServiceManager, state);
		this.jobseekerSearchService = jobseekerSearchService;
		this.userDetailService = userDetailService;
		this.jobsByUserService = jobsByUserService;
		this.userDataModel = userDataModel;
		this.taskManagerService = executorTaskManagerService;
		this.failedTaskService = failedTaskService;
		this.jobPortalEngineServiceProxy = jobPortalEngineServiceProxy;
	}

	@Override
	public Void call() {

		try {
			// STEPS
			// 1.Split the UserModel processing in the batch size of '100'.
			// 2.Call the Engine integration API.
			// 3.Store the result back in the 'MySQL'.
			// For the time Being I'm using 'chunk size of 100' need to change
			// the value accordingly.
			int pageOffset = 0;
			int pageSize = 50;
			boolean saveFirst = false;
			// TODO- I'm using the 'JobDetail' directly here.
			// Need to decide whether it's good to use 'JobDataModel' instead of
			// this.
			Set<JobDetail> jobDataModelSet = Sets.newHashSet();
			final Set<Long> trackComputedJobIds = Sets.newHashSet();
			do {
				log.info("#### Inside the CALL to process the DATA.");
				// ================== fetch the values form the MySQL,in bunch
				// of '100 records.'.
				// fetch results based on query
				jobDataModelSet = jobseekerSearchService
						.getFilteredJobsForEngineComputation(userDataModel,
								pageOffset, pageSize);

				if (!jobDataModelSet.isEmpty()) {
					// Now print the REsponse received form the 'Engine'.
					// call Engine for computation
					// ================== Call the Engine.
					ComputedJobsByUser computedJobsByUser = jobPortalEngineServiceProxy
							.callEngineToGetComputedJobsByUser(getEngineRequestContext(
									userDataModel, jobDataModelSet));
					computedJobsByUser.getJobIdToPercentageMap().forEach((k, v) -> trackComputedJobIds.add(k));
					// ================== End of Engine Call.

					// ================== Save the Response coming from the
					// Server
					// to MySQL.

					/**
					 * Save the Data to 'UserJobDetail Table' coming from
					 * Engine.
					 */
					boolean isDataSaved = this.jobsByUserService
							.saveComputedJobsByUser(computedJobsByUser);

					if (isDataSaved) {
						log.info("UsrByJobs Data is saveed successfully!!! ");
					} else {
						log.info("Some problem in saving the Data!!!");
					}
					if (!saveFirst) {
						setProcessingStateAndTimestamp(ProcessingState.PARTIALLYPROCESSED);
						saveFirst = true;
					}
					// Now change the 'ProcessingState'

					// ================== Now End of processed data Saving.
					// Do processing.
					// increase the offset.
					pageOffset++;
				} else {
					log.info("No data is available to Process !!!");
				}
			} while (!jobDataModelSet.isEmpty());
			if (trackComputedJobIds.isEmpty()) {
                this.setProcessingStateAndTimestamp(ProcessingState.NORESULTS);
            } else {
                // delete the userIds which are no longer in computed results
                this.jobsByUserService.deleteObseleteJobsFromJobsByUser(trackComputedJobIds,
                        Long.valueOf(userDataModel.getUserBasicDataModel().getUserId()));
            }
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (HttpClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	@Override
	protected String generateTaskId() {
		return TASK_ID_PREFIX
				+ userDataModel.getUserBasicDataModel().getUserId();
	}

	@Override
	protected void executeOnCreate() {
		// taskManagerService.doOnCreate(this);
	}

	@Override
	protected void executeOnSuccess(Void output) {
		setProcessingStateAndTimestamp(ProcessingState.PROCESSED);
		// taskManagerService.doOnSuccess(this);
	}

	@Override
	protected void executeOnFailure(Throwable t) {
		log.info("Some problem in executing the task");
		t.printStackTrace();
		setProcessingStateAndTimestamp(ProcessingState.PROCESSINGFAILED);
		// taskManagerService.doOnFailure(this);
	}

	private void setProcessingStateAndTimestamp(ProcessingState state) {

		userDetailService
				.setUserProcessingStateAndTimestamp(Long.valueOf(userDataModel
						.getUserBasicDataModel().getUserId()), state);
		userDataModel.getUserBasicDataModel().setProcessedState(state.name());
	}

	// private methods.
	private EngineRequestContext getEngineRequestContext(
			final UserDataModel userDataModel,
			final Set<JobDetail> jobDataModelSet) {
		System.out
				.println("insiddeth EngineRequestContext for buuilding object");
		// Set the 'UserDataModel'
		Set<EngineUserData> engineUserDataSet = Sets.newHashSet();
		// ======= START=====================================
		// TODO- We can move this to different function for fetching
		// the EngineUserData.
		EngineUserData engineUserData = new EngineUserData();
		engineUserData.setUserId(String.valueOf(userDataModel
				.getUserBasicDataModel().getUserId()));
		if (!StringUtils.isEmpty(userDataModel.getUserBasicDataModel()
				.getSkills())) {
			engineUserData.setSkills(userDataModel.getUserBasicDataModel()
					.getSkillsAsList());
		} else {
			engineUserData.setSkills(new ArrayList<String>());
		}
		// TODO-Currently we are passing 'Empty list' for this.
		// Need to decide and change it accordingly.
		engineUserData.setSkillsFoundInWorkHistory(new ArrayList<String>());
		engineUserData
				.setUserEducationList(getEngineUserEducationDataList(userDataModel));
		engineUserData
				.setUserExperianceList(getEngineUserExperianceDataList(userDataModel));
		// Add JobSeeker to Set.
		engineUserDataSet.add(engineUserData);

		// ======= END =====================================

		// Set the Required 'EngineJobData'
		Set<EngineJobData> engineJobDataSet = Sets.newHashSet();
		for (JobDetail jobDataModel : jobDataModelSet) {
			EngineJobData engineJobData = new EngineJobData();
			engineJobData.setJobDescription(jobDataModel.getJobDescription());
			engineJobData.setOrganisationName(jobDataModel
					.getOrganisationName());
			engineJobData.setJobTitle(jobDataModel.getTitle());
			engineJobData.setJobId(String.valueOf(jobDataModel.getJobId()));
			if (jobDataModel.getJobSkills() != null) {
				engineJobData.setJobSkills(Arrays.asList(jobDataModel
						.getJobSkills().split(
								JobPortalConstants.SKILLS_DELEMITER)));
			} else {
				engineJobData.setJobSkills(new ArrayList<String>());
			}
			// add it to the Engine Job set.
			engineJobDataSet.add(engineJobData);
		}
		// First generate the 'EngineRequestContext'.
		EngineRequestContext engineRequestContext = new EngineRequestContext();
		engineRequestContext.setEngineJobDataSet(engineJobDataSet);
		engineRequestContext.setEngineUserDataSet(engineUserDataSet);
		return engineRequestContext;
	}

	/*
	 * Get 'EngineUserEducationData' List.
	 */
	private List<EngineUserEducationData> getEngineUserEducationDataList(
			final UserDataModel userDetail) {
		List<EngineUserEducationData> engineUserEducationDataList = Lists
				.newArrayList();
		if (userDetail.getUserEducationDataModels() != null
				&& !userDetail.getUserEducationDataModels().isEmpty()) {
			for (UserEducationDataModel userEducationDetail : userDetail
					.getUserEducationDataModels()) {
				EngineUserEducationData engineUserEducationData = new EngineUserEducationData();
				engineUserEducationData.setEducationType(userEducationDetail
						.getDegreeType());
				engineUserEducationData.setInstituteName(userEducationDetail
						.getOrganisationName());
				engineUserEducationDataList.add(engineUserEducationData);
			}
		}
		return engineUserEducationDataList;
	}

	/*
	 * Get 'EngineUserExperianceData' List.
	 */
	private List<EngineUserExperianceData> getEngineUserExperianceDataList(
			final UserDataModel userDetail) {
		List<EngineUserExperianceData> engineUserExperianceDataList = Lists
				.newArrayList();
		if (userDetail.getUserExperienceDataModels() != null
				&& !userDetail.getUserExperienceDataModels().isEmpty()) {
			List<UserExperienceDataModel> userExperienceDataModelsList = new ArrayList<UserExperienceDataModel>(
					userDetail.getUserExperienceDataModels());
			// Sort the userExperience detail according to user latest Job.
			// Sort them in the Decreasing order of 'User's working start date'.
			Collections.sort(userExperienceDataModelsList,
					new UserExperienceDataModel.OrderByLatestCompany());
			//
			int index = 1;
			for (UserExperienceDataModel userExperinceDetail : userExperienceDataModelsList) {
				EngineUserExperianceData engineUserExperianceData = new EngineUserExperianceData();
				engineUserExperianceData.setCompanyName(userExperinceDetail
						.getCompanyName());
				engineUserExperianceData.setWorkDescription(userExperinceDetail
						.getDescription());
				engineUserExperianceData
						.setWorkTenureInCompany(userExperinceDetail
								.getTotalExpMonth());
				engineUserExperianceData.setJobWorkTitle(userExperinceDetail
						.getCompanyName());
				engineUserExperianceData.setSwitchIndexOfCompanyByUser(index++);
				// add to the list
				engineUserExperianceDataList.add(engineUserExperianceData);
			}
		}
		return engineUserExperianceDataList;
	}

	@Override
	protected void handleOnMaxRetryFailure() {
		final FailedTask failedTask = new FailedTask();
		failedTask.setTaskId(getTaskId());
		failedTask.setAdditionalKey(String.valueOf(userDataModel
				.getUserBasicDataModel().getUserId()));
		failedTask.setCreatedDate(new Date());
		failedTask.setModifiedDate(new Date());
		if (TaskState.PROCESSED_BUT_FAILED.equals(this.getState())) {
			failedTask.setState(TaskState.REJECTED_PERMANENTLY);
			failedTask.setTempState(TaskState.REJECTED_PERMANENTLY);
		}
		failedTaskService.saveTask(failedTask);
	}

	@Override
	protected void handleOnTaskRejection(TaskRejectionException e) {
		this.handleOnMaxRetryFailure();
	}
}
