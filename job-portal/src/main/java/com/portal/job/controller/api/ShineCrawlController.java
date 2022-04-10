package com.portal.job.controller.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.aggregator.shine.ShineCrawl;

@Controller
@RequestMapping("/api")
public class ShineCrawlController {

	@Autowired
	private ShineCrawl shineCrawl;

	@RequestMapping("shine")
	public @ResponseBody String getShineCrawl() throws JsonProcessingException {
		shineCrawl.getJobs();
		return StringUtils.EMPTY;
	}

}
