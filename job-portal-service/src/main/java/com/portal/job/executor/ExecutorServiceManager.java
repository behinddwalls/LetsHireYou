package com.portal.job.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * This class is responsible for creating & managing executor service. Primary
 * responsibilities are: 1) Create executor service for the configured number of
 * maximum threads & idle thread timeout 2) Install shutdown hook which will be
 * called in graceful shutdown of VM. It tries to gracefully close the running
 * tasks.
 * 
 * @author behinddwalls
 *
 */
@Component
public class ExecutorServiceManager {
	private static final Logger logger = LoggerFactory
			.getLogger(ExecutorServiceManager.class);

	// (50%). Leave enough capacity for other threads sharing the CPU and for OS
	private static final double TARGET_CPU_UTILIZATION = 0.5;

	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * Default constructor
	 * 
	 */
	public ExecutorServiceManager() {
		this(20, 60);
	}

	/**
	 * It uses {@link ThreadPoolExecutor} to create the {@link ExecutorService}.
	 * We don't use {@link Executors} method directly because we want to control
	 * the maximum number of threads and at the same time, we don't want idle
	 * threads to be always alive.
	 * 
	 * Executors static factory methods only provide two type of
	 * ExecutorService: 1) Cached thread pool: There is no limit on the maximum
	 * number of threads 2) Fixed size thread pool: Threads are always alive
	 * even when the load is less
	 * 
	 * @param waitToComputeTimeRatio
	 *            Pass wait to compute ratio. This ratio is indicator of amount
	 *            of time spent in doing I/O activity. Refer Java concurrency in
	 *            practice book to understand the important of this ratio in
	 *            calculation of thread pool size.
	 * @param idleTimeoutInSecs
	 *            If a thread is idle for the time > idleTimeout then the thread
	 *            will be closed
	 */
	public ExecutorServiceManager(final double waitToComputeTimeRatio,
			final int idleTimeoutInSecs) {

		int maxThreads = getNumberOfThreads(waitToComputeTimeRatio);
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(maxThreads);
		taskExecutor.setMaxPoolSize(maxThreads);
		taskExecutor.setKeepAliveSeconds(idleTimeoutInSecs);
		taskExecutor.setAllowCoreThreadTimeOut(true);
		// initialize
		taskExecutor.initialize();

		this.threadPoolTaskExecutor = taskExecutor;

		logger.info("ExecutorService started with number of threads:"
				+ maxThreads);

		// install shutdown hook
		Runtime.getRuntime().addShutdownHook(new ShutDownHook());
	}

	/**
	 * Get the {@link ExecutorService}
	 * 
	 * @return
	 */
	public ExecutorService get() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor();
	}

	/**
	 * Calculate the maximum number of threads required by ExecutorService
	 * 
	 * @param waitToComputeTimeRatio
	 * @return
	 */
	private int getNumberOfThreads(final double waitToComputeTimeRatio) {
		/*
		 * This formula is taken from Java concurrency in practice book. Refer
		 * Section: "Sizing Thread Pools"
		 */
		Double numThreads = Runtime.getRuntime().availableProcessors()
				* TARGET_CPU_UTILIZATION * (1 + waitToComputeTimeRatio);
		return numThreads.intValue();
	}

	public int getActiveCountOfThreads() {
		return threadPoolTaskExecutor.getActiveCount();
	}

	public <T> Future<T> submit(Callable<T> task) {
		return threadPoolTaskExecutor.submit(task);
	}

	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return threadPoolTaskExecutor.submitListenable(task);
	}

	/**
	 * Responsible for graceful shutdown of running & waiting tasks
	 * 
	 * @author preetam
	 *
	 */
	private class ShutDownHook extends Thread {
		private static final int TERMINATION_WAITING_TIME = 5; // Time is in
																// seconds.

		@Override
		public void run() {
			threadPoolTaskExecutor
					.setAwaitTerminationSeconds(TERMINATION_WAITING_TIME);
			threadPoolTaskExecutor.shutdown();
		}
	}
}