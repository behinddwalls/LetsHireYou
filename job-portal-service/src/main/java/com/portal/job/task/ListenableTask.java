package com.portal.job.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.portal.job.enums.TaskState;
import com.portal.job.exceptions.TaskRejectionException;
import com.portal.job.executor.ExecutorServiceManager;

/**
 * @author behinddwalls
 *
 * @param <V>
 */
public abstract class ListenableTask<V> extends Task<ListenableFuture<V>>
		implements Callable<V> {
	private static Logger log = LoggerFactory.getLogger(ListenableTask.class);

	protected ListenableTask(ExecutorServiceManager executorServiceManager) {
		super(executorServiceManager);
	}

	protected ListenableTask(ExecutorServiceManager executorServiceManager,
			TaskState state) {
		super(executorServiceManager, state);
	}

	protected abstract void executeOnSuccess(final V output);

	protected abstract void executeOnFailure(final Throwable t);

	protected abstract void handleOnTaskRejection(final TaskRejectionException e);

	@Override
	public ListenableFuture<V> executeTask() {
		try {
			final ListenableFuture<V> listenableFuture = executorServiceManager
					.submitListenable(this);
			listenableFuture.addCallback(new ListenableFutureCallback<V>() {

				@Override
				public void onSuccess(V result) {
					executeOnSuccess(result);
					System.out.println("SUCCESS");
				}

				@Override
				public void onFailure(Throwable t) {
					executeOnFailure(t);
					System.out.println("FAILED");

				}
			});
			return listenableFuture;
		} catch (TaskRejectedException e) {
			TaskRejectionException ex = new TaskRejectionException(e);
			handleOnTaskRejection(ex);
			return null;
		}

	}

}
