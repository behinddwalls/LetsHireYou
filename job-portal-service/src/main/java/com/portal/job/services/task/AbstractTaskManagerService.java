package com.portal.job.services.task;

import java.util.concurrent.ConcurrentSkipListMap;

import com.portal.job.task.Task;

/**
 * @author behinddwalls
 *
 */
public abstract class AbstractTaskManagerService {

    protected final ConcurrentSkipListMap<String, Task<?>> inProgressQueue = new ConcurrentSkipListMap<String, Task<?>>();

    protected abstract <T> void onCreate(Task<T> task);

    protected abstract <T> void onSuccess(Task<T> task);

    protected abstract <T> void onFailure(Task<T> task);

    protected abstract <T> void onMaxRetryFailure(Task<T> task);

    public <T> void doOnCreate(Task<T> task) {
        synchronized (inProgressQueue) {
            onCreate(task);
        }

    }

    public <T> void doOnSuccess(Task<T> task) {
        synchronized (inProgressQueue) {
            onSuccess(task);
        }
    }

    public <T> void doOnFailure(Task<T> task) {
        synchronized (inProgressQueue) {
            onFailure(task);
        }
    }

    public <T> void doOnMaxRetryFailure(Task<T> task) {
        synchronized (inProgressQueue) {
            onMaxRetryFailure(task);
        }
    }

    public ConcurrentSkipListMap<String, Task<?>> getInProgressQueue() {
        return inProgressQueue;
    }
}
