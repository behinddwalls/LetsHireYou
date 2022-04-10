package com.portal.job.services.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.portal.job.dao.mongodb.FailedTaskRepository;
import com.portal.job.dao.mongodb.model.FailedTask;
import com.portal.job.enums.TaskState;

@Service
public class FailedTaskService {

	@Autowired
	private FailedTaskRepository failedTaskRepository;

	public FailedTask saveTask(final FailedTask failedTask) {
		return failedTaskRepository.save(failedTask);
	}

	public FailedTask getTaskByTaskId(final String taskId) {
		return failedTaskRepository.findByTaskId(taskId);
	}

	public List<FailedTask> getTasks(final int pageSize, final int pageNumber) {
		Page<FailedTask> failedTasks = failedTaskRepository
				.findAll(new PageRequest(pageNumber, pageSize));
		return failedTasks.getContent();
	}

	public Long countByStateNotIn(final List<TaskState> states) {
		return failedTaskRepository.countByStateNotIn(states);
	}

}
