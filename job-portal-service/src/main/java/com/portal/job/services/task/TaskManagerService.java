package com.portal.job.services.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.portal.job.task.Task;

/**
 * This Service is used to manage the task executor queues.
 * 
 * @author behinddwalls
 *
 */
@Service
public class TaskManagerService extends AbstractTaskManagerService {

    private Logger logger = LoggerFactory.getLogger(TaskManagerService.class);

    /**
     * Queue the task for processing if there is no task with same task id in In
     * progress queue otherwise check if any task with same taskId is waiting in
     * waiting queue. If any task is in waiting queue then update that task with
     * newly task as it is an obsolete task.
     * 
     * @param <T>
     * 
     * @param task
     * @return
     */

    @Override
    public <T> void onCreate(Task<T> task) {

        final String taskId = task.getTaskId();

        if (!StringUtils.isEmpty(taskId)) {
            if (inProgressQueue.containsKey(taskId)) {
//                task.setSkipExecution(true);
            } else {
                inProgressQueue.put(taskId, task);
//                task.setSkipExecution(false);
            }
        } else {
            logger.warn("Task Id can not be null for qnqueing task");
        }
    }

    @Override
    public <T> void onSuccess(Task<T> task) {
        final String taskId = task.getTaskId();

        if (!StringUtils.isEmpty(taskId) && inProgressQueue.containsKey(taskId)) {
            inProgressQueue.remove(taskId);
        } else {
            logger.warn("Task Id can not be null for dequeing task");
        }

    }

    @Override
    public <T> void onFailure(Task<T> task) {
        final String taskId = task.getTaskId();

        if (!StringUtils.isEmpty(taskId) && inProgressQueue.containsKey(taskId)) {
            inProgressQueue.remove(taskId);
            task.doTask();

        } else {
            logger.warn("Task Id can not be null for dequeing task");
        }

    }

    @Override
    protected <T> void onMaxRetryFailure(Task<T> task) {
        if (!StringUtils.isEmpty(task.getTaskId())) {
            inProgressQueue.remove(task.getTaskId());
        }
    }
}
