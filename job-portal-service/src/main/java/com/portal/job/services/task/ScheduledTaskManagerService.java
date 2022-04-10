package com.portal.job.services.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.portal.job.enums.TaskState;
import com.portal.job.service.JobDetailService;

@Service
@EnableScheduling
public class ScheduledTaskManagerService {

	@Autowired
	private JobTaskExecutorService jobTaskExecutorService;
	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private FailedTaskService failedTaskService;

	/**
	 * Cron is represented as
	 * 
	 * <pre>
	 * second, minute, hour, day of month, month, day(s) of week
	 * </pre>
	 * 
	 * Execute this task every day at 01:00 am.
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void test() {
		int pageSize = 10;
		int pageNumber = 0;
		try {
			final List<TaskState> states = Lists.newArrayList();
			states.add(TaskState.REJECTED_PERMANENTLY);
			states.add(TaskState.PROCESSED);
			long count = failedTaskService.countByStateNotIn(states);

			failedTaskService.getTasks(pageSize, pageNumber);
			jobDetailService.getJobDataModelByJobId((long) 1);

		} catch (Exception e) {

		}
	}
}
