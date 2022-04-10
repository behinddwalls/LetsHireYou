package com.portal.job.controller.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.aggregator.times.TimesCrawl;

@Controller
@RequestMapping("/api")
public class TimesCrawlController {

	@Autowired
	private TimesCrawl timesCrawl;
	@RequestMapping("times")
	public @ResponseBody String getNaukriCrawl() throws JsonProcessingException {
		timesCrawl.getJobs();
		return StringUtils.EMPTY;
	}
}
