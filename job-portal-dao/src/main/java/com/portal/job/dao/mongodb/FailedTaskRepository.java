package com.portal.job.dao.mongodb;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portal.job.dao.mongodb.model.FailedTask;
import com.portal.job.enums.TaskState;

@Repository
public interface FailedTaskRepository extends
		MongoRepository<FailedTask, Serializable> {

	public FailedTask findByTaskId(final String taskId);

	public Long countByStateNotIn(final Collection<TaskState> state);

}
