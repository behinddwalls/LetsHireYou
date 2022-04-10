package com.portal.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portal.job.exceptions.TaskRejectionException;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.service.aggregator.CareerJetJobAggregatorService;

public class JobAggregatorTask extends ListenableTask<Void> {

    private static final Logger log = LoggerFactory.getLogger(JobAggregatorTask.class);

    private CareerJetJobAggregatorService careerJetJobAggregatorService;

    public JobAggregatorTask(final ExecutorServiceManager executorServiceManager,
            final CareerJetJobAggregatorService careerJetJobAggregatorService) {
        super(executorServiceManager);
        this.careerJetJobAggregatorService = careerJetJobAggregatorService;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("Hello");
        try {
            System.out.println(this.careerJetJobAggregatorService);
            this.careerJetJobAggregatorService.aggregateJobs("", "");
        } catch (Exception e) {
            System.out.println(e);
            log.error("Failed to execute aggregator task ", e);
            throw e;
        }
        return null;
    }

    @Override
    protected void executeOnSuccess(Void output) {
        // this.careerJetJobAggregatorService.setIsProcessing(false);

    }

    @Override
    protected void executeOnFailure(Throwable t) {
        // this.careerJetJobAggregatorService.setIsProcessing(false);
        t.printStackTrace();

    }

    @Override
    protected void handleOnTaskRejection(TaskRejectionException e) {
        // this.careerJetJobAggregatorService.setIsProcessing(false);
        e.printStackTrace();

    }

    @Override
    protected void executeOnCreate() {
        //this.careerJetJobAggregatorService.setIsProcessing(true);

    }

    @Override
    protected void handleOnMaxRetryFailure() {
        // this.careerJetJobAggregatorService.setIsProcessing(false);

    }

    @Override
    protected String generateTaskId() {
        return getClass().getSimpleName();
    }

}
