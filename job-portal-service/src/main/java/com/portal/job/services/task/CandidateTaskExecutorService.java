package com.portal.job.services.task;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.enums.ProcessingState;
import com.portal.job.enums.TaskState;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.service.JobsByUserService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.engine.JobPortalEngineServiceProxy;
import com.portal.job.services.search.JobseekerSearchService;
import com.portal.job.task.ComputeRecommendedJobsForCandidateTask;

@Service
public class CandidateTaskExecutorService {
	private static final Logger log = LoggerFactory
			.getLogger(CandidateTaskExecutorService.class);
	// TODO- remove after testing.
	@Autowired
	private JobseekerSearchService jobseekerSearchService;
	@Autowired
	private JobsByUserService jobsByUserService;
	@Autowired
	private ExecutorServiceManager executorServiceManager;
	@Autowired
	private TaskManagerService executorTaskManagerService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private FailedTaskService failedTaskService;
	@Autowired
	private JobPortalEngineServiceProxy jobPortalEngineServiceProxy;

	public void computeJobsForCandidate(final UserDataModel userDataModel,
			final TaskState state) {
		ComputeRecommendedJobsForCandidateTask task = new ComputeRecommendedJobsForCandidateTask(
				jobseekerSearchService, userDetailService, jobsByUserService,
				failedTaskService, userDataModel, executorServiceManager,
				executorTaskManagerService, state, jobPortalEngineServiceProxy);
		try {
			if (Optional.ofNullable(userDataModel).isPresent()) {
				task.doTask();
			}
		} catch (Exception ex) {
			log.error("Task Parsing exception for Task:" + task.getTaskId()
					+ " \n ERROR STACKTRACE:\n" + ex);
			throw new RuntimeException(ex);
		}
	}

	@Transactional
	public void computeJobsForCandidate(final UserDataModel userDataModel) {
		final UserBasicDataModel userBasicDataModel = this.userDetailService
				.getUserBasicDataModel(userDataModel.getUserBasicDataModel()
						.getUserId());
		if (userBasicDataModel != null
				&& !ProcessingState.PARTIALLYPROCESSED.name().equals(
						userBasicDataModel.getProcessedState())) {
			this.computeJobsForCandidate(userDataModel, TaskState.NEW);
		}
	}

}
