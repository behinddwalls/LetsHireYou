package com.portal.job.task;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.portal.job.enums.ProcessingState;
import com.portal.job.enums.TaskState;
import com.portal.job.exceptions.TaskRejectionException;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.model.ComputedUsersByJob;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.model.engine.EngineJobData;
import com.portal.job.model.engine.EngineRequestContext;
import com.portal.job.model.engine.EngineUserData;
import com.portal.job.model.engine.EngineUserEducationData;
import com.portal.job.model.engine.EngineUserExperianceData;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.UserByJobService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.engine.JobPortalEngineServiceProxy;
import com.portal.job.services.task.FailedTaskService;
import com.portal.job.services.task.TaskManagerService;

/**
 * @author behinddwalls
 *
 */
public class OnPostOrEditJobComputeCandidatesTask extends ListenableTask<Void> {

    private static final Logger log = LoggerFactory.getLogger(OnPostOrEditJobComputeCandidatesTask.class);

    final private UserDetailService userDetailService;
    final private JobDetailService jobDetailService;
    final private JobDataModel jobDataModel;
    final private TaskManagerService taskManagerService;
    final private FailedTaskService failedTaskService;
    private final JobPortalEngineServiceProxy jobPortalEngineServiceProxy;
    private final UserByJobService userByJobService;

    private static final String TASK_ID_PREFIX = "OnPostOrEditJobComputeCandidatesTask_";

    public OnPostOrEditJobComputeCandidatesTask(final UserDetailService userDetailService,
            final JobDetailService jobDetailService, final UserByJobService userByJobService,
            final FailedTaskService failedTaskService, final JobDataModel jobDataModel,
            final ExecutorServiceManager executorServiceManager, final TaskManagerService executorTaskManagerService,
            final TaskState state, final JobPortalEngineServiceProxy jobPortalEngineServiceProxy) {
        super(executorServiceManager, state);
        this.userDetailService = userDetailService;
        this.jobDetailService = jobDetailService;
        this.userByJobService = userByJobService;
        this.jobDataModel = jobDataModel;
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
            int pageSize = 100;
            boolean saveFirst = false;
            Set<UserDataModel> userDetailSet = Sets.newHashSet();
            final Set<Long> trackComputedUserIds = Sets.newHashSet();
            do {

                // ================== fetch the values form the MySQL,in bunch
                // of '100 records.'.
                // fetch results based on query
                userDetailSet = userDetailService.getUserDetailsForPostedJobsInTransaction(jobDataModel, pageOffset,
                        pageSize);
                if (!userDetailSet.isEmpty()) {
                    // Now print the REsponse received form the 'Engine'.
                    // call Engine for computation
                    // ================== Call the Engine.

                    /*
                     * Adding the InitialTimeOut and 'backOff factor' to decide
                     * how many times we want to retry this.
                     */
                    ComputedUsersByJob computedUsersByJob = null;
                    try {
                        computedUsersByJob = jobPortalEngineServiceProxy
                                .callEngineToGetComputedUsersByJob(getEngineRequestContext(jobDataModel, userDetailSet));
                        // loop termination criteria.
                        if (null == computedUsersByJob) {
                            throw new Exception("ERROR: Computed Users by Job Returned null");
                        }
                        computedUsersByJob.getUserIdToPercentageMap().forEach((k, v) -> trackComputedUserIds.add(k));
                    } catch (Exception ex) {
                        log.error("Error ", ex);

                        throw new RuntimeException(
                                "Some error while calling Engine to process the Userdata for given Job", ex);

                    }
                    // ================== End of Engine Call.

                    // ================== Save the Response coming from the
                    // Server
                    // to MySQL.
                    /*
                     * Need to catch the exception when we are trying to save
                     * the same entry again to 'user_By_Job table in DB' and it
                     * throws the 'Constraint violation' of duplicate entry
                     * found in DB .
                     */

                    /*
                     * Again provide the same logic of retrying as done earlier
                     * while calling 'Engine' to fetch the Data.
                     */
                    try {
                        boolean isDataSaved = this.userByJobService.saveComputedUsersByJob(computedUsersByJob);
                        // Assign the value to end the loop.
                        if (!isDataSaved) {
                            throw new Exception("Failed to save in db");
                        }
                        if (!saveFirst) {
                            setProcessingStateAndTimestamp(ProcessingState.PARTIALLYPROCESSED);
                            saveFirst=true;
                        }
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        log.error("Duplicate User Entries found while trying to save the Data", ex);
                    } catch (Exception ex) {
                        throw new RuntimeException("Some error occured while Saving Userdata in the DB", ex);
                    }
                    // End of While Loop
                }

                // ================== Now End of processed data Saving.
                // Do processing.
                // increase the offset.
                pageOffset++;
            } while (!userDetailSet.isEmpty());

            if (trackComputedUserIds.isEmpty()) {
                this.setProcessingStateAndTimestamp(ProcessingState.NORESULTS);
            } else {
                // delete the userIds which are no longer in computed results
                this.userByJobService.deleteObseleteUsersFromUserByJob(trackComputedUserIds,
                        Long.valueOf(jobDataModel.getJobId()));
            }

        } catch (ParseException e) {
            log.error("Task Parsing exception for Task:" + generateTaskId() + " \n ERROR STACKTRACE:\n", e);
            throw e;
        } catch (Exception e) {
            log.error("Task Parsing exception for Task:" + generateTaskId() + " \n ERROR STACKTRACE:\n", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected String generateTaskId() {
        return TASK_ID_PREFIX + jobDataModel.getJobId();
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
        log.error("Some problem in executing the task", t);
        setProcessingStateAndTimestamp(ProcessingState.PROCESSINGFAILED);
        // taskManagerService.doOnFailure(this);
    }

    private void setProcessingStateAndTimestamp(ProcessingState state) {
        jobDetailService.setJobProcessingStateAndTimestamp(Long.valueOf(jobDataModel.getJobId()), state);
    }

    // private methods.
    private EngineRequestContext getEngineRequestContext(final JobDataModel jobDataModel,
            final Set<UserDataModel> userDetailSet) {
        System.out.println("insiddeth EngineRequestContext for buuilding object");
        // Set the 'EngineJobData'
        Set<EngineJobData> engineJobDataSet = new HashSet<EngineJobData>();
        EngineJobData engineJobData = new EngineJobData();
        engineJobData.setJobDescription(jobDataModel.getJobDescription());
        engineJobData.setJobTitle(jobDataModel.getTitle());
        engineJobData.setJobId(jobDataModel.getJobId());
        engineJobData.setOrganisationName(jobDataModel.getOrganisationName());
        engineJobData.setJobSkills(jobDataModel.getSkillsAsList());
        // set EngineJobData to set.
        engineJobDataSet.add(engineJobData);

        // Set the Required 'User Detail fields'
        Set<EngineUserData> engineUserDataSet = Sets.newHashSet();
        for (UserDataModel userDetail : userDetailSet) {
            EngineUserData engineUserData = new EngineUserData();
            engineUserData.setUserId(String.valueOf(userDetail.getUserBasicDataModel().getUserId()));
            if (!StringUtils.isEmpty(userDetail.getUserBasicDataModel().getSkills())) {
                engineUserData.setSkills(userDetail.getUserBasicDataModel().getSkillsAsList());
            } else {
                engineUserData.setSkills(Lists.newLinkedList());
            }
            if (!StringUtils.isEmpty(userDetail.getUserBasicDataModel().getSkillsFoundinWorkHistory())) {
                engineUserData.setSkillsFoundInWorkHistory(userDetail.getUserBasicDataModel()
                        .getSkillsFoundInWorkHistoryAsList());
            } else {
                engineUserData.setSkillsFoundInWorkHistory(Lists.newLinkedList());
            }

            engineUserData.setUserEducationList(getEngineUserEducationDataList(userDetail));
            engineUserData.setUserExperianceList(getEngineUserExperianceDataList(userDetail));
            engineUserDataSet.add(engineUserData);
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
    private List<EngineUserEducationData> getEngineUserEducationDataList(final UserDataModel userDetail) {
        List<EngineUserEducationData> engineUserEducationDataList = Lists.newArrayList();
        for (UserEducationDataModel userEducationDetail : userDetail.getUserEducationDataModels()) {
            EngineUserEducationData engineUserEducationData = new EngineUserEducationData();
            engineUserEducationData.setEducationType(userEducationDetail.getDegreeType());
            engineUserEducationData.setInstituteName(userEducationDetail.getOrganisationName());
            engineUserEducationDataList.add(engineUserEducationData);
        }
        return engineUserEducationDataList;
    }

    /*
     * Get 'EngineUserExperianceData' List.
     */
    private List<EngineUserExperianceData> getEngineUserExperianceDataList(final UserDataModel userDetail) {
        List<EngineUserExperianceData> engineUserExperianceDataList = Lists.newArrayList();
        List<UserExperienceDataModel> userExperienceDataModelsList = new ArrayList<UserExperienceDataModel>(
                userDetail.getUserExperienceDataModels());
        // Sort the userExperience detail according to user latest Job.
        // Sort them in the Decreasing order of 'User's working start date'.
        Collections.sort(userExperienceDataModelsList, new UserExperienceDataModel.OrderByLatestCompany());
        //
        int index = 1;
        for (UserExperienceDataModel userExperinceDetail : userExperienceDataModelsList) {
            EngineUserExperianceData engineUserExperianceData = new EngineUserExperianceData();
            engineUserExperianceData.setCompanyName(userExperinceDetail.getCompanyName());
            engineUserExperianceData.setWorkDescription(userExperinceDetail.getDescription());
            engineUserExperianceData.setWorkTenureInCompany(userExperinceDetail.getTotalExpMonth());
            engineUserExperianceData.setJobWorkTitle(userExperinceDetail.getRoleName());
            engineUserExperianceData.setSwitchIndexOfCompanyByUser(index++);
            // add to the list
            engineUserExperianceDataList.add(engineUserExperianceData);
        }
        return engineUserExperianceDataList;
    }

    @Override
    protected void handleOnMaxRetryFailure() {
        // taskManagerService.doOnMaxRetryFailure(this);
        // TODO: Store to DB for Retrying later
        // final FailedTask failedTask = new FailedTask();
        // failedTask.setTaskId(getTaskId());
        // failedTask.setAdditionalKey(jobDataModel.getJobId());
        // failedTask.setCreatedDate(new Date());
        // failedTask.setModifiedDate(new Date());
        // if (TaskState.PROCESSED_BUT_FAILED.equals(this.getState())) {
        // failedTask.setState(TaskState.REJECTED_PERMANENTLY);
        // failedTask.setTempState(TaskState.REJECTED_PERMANENTLY);
        // }
        // failedTaskService.saveTask(failedTask);
    }

    @Override
    protected void handleOnTaskRejection(TaskRejectionException e) {
        this.handleOnMaxRetryFailure();
    }
}
