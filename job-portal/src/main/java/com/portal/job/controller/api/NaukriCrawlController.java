package com.portal.job.controller.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.aggregator.naukri.NaukriCrawl;
import com.portal.job.aggregator.naukri.NaukriUnprocessedCrawl;

@Controller
@RequestMapping("/api")
public class NaukriCrawlController {

	@Autowired
	private NaukriCrawl naukriCrawl;
	@Autowired
	private NaukriUnprocessedCrawl naukriUnprocessedCrawl;
	@RequestMapping("naukri")
	public @ResponseBody String getNaukriCrawl() throws JsonProcessingException {
		naukriCrawl.getJobs();
		return StringUtils.EMPTY;
	}
	
	@RequestMapping("naukriunprocessed")
	public @ResponseBody String getNaukriUnprocessedCrawl() throws JsonProcessingException {
		naukriUnprocessedCrawl.getJobs();
		return StringUtils.EMPTY;
	}

}
