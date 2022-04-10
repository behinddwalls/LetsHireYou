package com.portal.job.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.service.aggregator.CareerJetJobAggregatorService;

@Controller
@RequestMapping("/api/jobaggregator")
public class JobAggregatorController {

	@Autowired
	private CareerJetJobAggregatorService careerJetJobAggregatorService;
	@Autowired
	private ExecutorServiceManager executorServiceManager;

//	@RequestMapping(value = { "", "/" })
//	public @ResponseBody String test() {
//		if (!this.careerJetJobAggregatorService.isProcessing()) {
//			JobAggregatorTask task = new JobAggregatorTask(
//					executorServiceManager, careerJetJobAggregatorService);
//			task.doTask();
//			return "Started Processing";
//		}
//		return "Currently Processing another job - Total Processed - "
//				+ this.careerJetJobAggregatorService.getTotalProcessed()
//				+ "<br/> " + this.careerJetJobAggregatorService.getMap();
//	}
//
//	@RequestMapping(value = { "reset", })
//	public @ResponseBody String reset() {
//		final Map<String, String> map = Maps.newHashMap();
//		map.putAll(this.careerJetJobAggregatorService.getMap());
//
//		StringBuilder strbBuilder = new StringBuilder();
//		strbBuilder.append("Total Processed - ")
//				.append(this.careerJetJobAggregatorService.getTotalProcessed())
//				.append("<br/><br/>").append("All keys -").append("<br/>")
//				.append(map);
//		this.careerJetJobAggregatorService.getMap().clear();
//		this.careerJetJobAggregatorService.setIsProcessing(false);
//		this.careerJetJobAggregatorService.setTotalFailed(0);
//		this.careerJetJobAggregatorService.setTotalProcessed(0);
//		return strbBuilder.toString();
//	}
}
