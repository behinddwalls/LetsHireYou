package com.portal.job.task;

import java.util.Date;
import java.util.Optional;

import com.portal.job.enums.TaskState;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.services.task.TaskManagerService;

/**
 * @author behinddwalls
 *
 * @param <V>
 */
public abstract class Task<V> {

    protected final ExecutorServiceManager executorServiceManager;
    protected String taskId;
    private Date date;
    private int retryCount = 0;
    private final int maxRetryCount;
    private final TaskState state;

    protected Task(ExecutorServiceManager executorServiceManager) {
        this(executorServiceManager, null, 2);
    }

    protected Task(ExecutorServiceManager executorServiceManager, final TaskState state) {
        this(executorServiceManager, state, 2);
    }

    protected Task(ExecutorServiceManager executorServiceManager, final TaskState state, int maxRetryCount) {
        this.executorServiceManager = executorServiceManager;
        this.maxRetryCount = maxRetryCount;
        this.state = state;
    }

    /**
     * @return
     */
    protected abstract V executeTask();

    /**
     * @return
     */
    protected abstract void executeOnCreate();

    /**
     * @return
     */
    protected abstract void handleOnMaxRetryFailure();

    /**
     * This method takes the task as an input.
     * 
     * All the tasks should provide a mechanism to identify their task in the
     * {@link TaskManagerService} queues.
     * 
     * This is important if a task which is same as the current processing one
     * is in progress or waiting queue then {@link TaskManagerService} takes
     * correct action for tasks.
     * 
     * @param task
     * @return
     */
    protected abstract String generateTaskId();

    /**
     * @return
     */
    public V doTask() {
        try {
            V output = null;
            Optional.of(generateTaskId());
            this.taskId = generateTaskId();
            executeOnCreate();
            output = executeTask();
            return output;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getTaskId() {
        return taskId;
    }

    public long getTaskCreationTime() {
        return date.getTime();
    }

    @Override
    public String toString() {
        return "Task [ taskId=" + taskId + "]";
    }

    public ExecutorServiceManager getExecutorServiceManager() {
        return executorServiceManager;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public TaskState getState() {
        return state;
    }

}
