package com.portal.job.services.task;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.job.enums.TaskState;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.UserByJobService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.engine.EngineDataStoreService;
import com.portal.job.services.engine.JobPortalEngineServiceProxy;
import com.portal.job.services.search.JobseekerSearchService;
import com.portal.job.task.ComputeRecommendedJobsForCandidateTask;
import com.portal.job.task.OnPostOrEditJobComputeCandidatesTask;

/**
 * @author behinddwalls
 *
 */
@Service
public class JobTaskExecutorService {

    private static final Logger log = LoggerFactory
            .getLogger(JobTaskExecutorService.class);
	//TODO- remove after testing.
	@Autowired
	private JobseekerSearchService jobseekerSearchService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserByJobService userByJobService;
    @Autowired
    private ExecutorServiceManager executorServiceManager;
    @Autowired
    private TaskManagerService executorTaskManagerService;
    @Autowired
    private JobDetailService jobDetailService;
    @Autowired
    private FailedTaskService failedTaskService;
    @Autowired
    private JobPortalEngineServiceProxy jobPortalEngineServiceProxy;

    public void computeCandidatesForPostedJob(final JobDataModel jobDataModel, final TaskState state) {
        OnPostOrEditJobComputeCandidatesTask task = new OnPostOrEditJobComputeCandidatesTask(
                userDetailService, jobDetailService, userByJobService, failedTaskService,
                jobDataModel, executorServiceManager, executorTaskManagerService, state,
                jobPortalEngineServiceProxy);
    	try{
    	if (Optional.ofNullable(jobDataModel).isPresent()
                && !StringUtils.isEmpty(jobDataModel.getJobId())) {
            task.doTask();
            
/*            //TODO- Need to remove this once the testing is done for this.
            //ComputeRecommendedJobsForCandidateTask
            //Create 'UserDataModel'
            UserBasicDataModel userBasicDataModel = new UserBasicDataModel();
            userBasicDataModel.setUserId(1L);
            UserDataModel.Builder builder = new UserDataModel.Builder(userBasicDataModel);
            
            ComputeRecommendedJobsForCandidateTask userTask = new ComputeRecommendedJobsForCandidateTask(
            		jobseekerSearchService, jobDetailService, userByJobService, failedTaskService,
                    builder.build(), executorServiceManager, executorTaskManagerService, state,
                    jobPortalEngineServiceProxy);
            userTask.doTask();*/
        }
       }catch(Exception ex){
			log.error("Task Parsing exception for Task:" + task.getTaskId()
					+ " \n ERROR STACKTRACE:\n" + ex);
			throw new RuntimeException(ex);
       }
    }

    public void computeCandidatesForPostedJob(final JobDataModel jobDataModel) {
        this.computeCandidatesForPostedJob(jobDataModel, TaskState.NEW);
    }
}
