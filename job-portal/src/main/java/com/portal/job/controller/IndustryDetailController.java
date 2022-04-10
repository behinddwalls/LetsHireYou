package com.portal.job.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portal.job.service.IndustryDetailService;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("industry")
public class IndustryDetailController {

	private static final Logger log = LoggerFactory
			.getLogger(IndustryDetailController.class);
	@Autowired
	private IndustryDetailService industryDetailService;

	@RequestMapping("search")
	public @ResponseBody Set<String> searchIndustry(
			@RequestParam("industryName") final String industryName) {
		try {
			return this.industryDetailService
					.serachIndustryByDescription(industryName);
		} catch (Exception e) {
			log.error("Failed to fetch role", e);
		}
		return new HashSet<String>();
	}

	@RequestMapping("getall")
	public @ResponseBody Set<String> getAllIndustry() {
		try {
			return this.industryDetailService.getAllIndustry();
		} catch (Exception e) {
			log.error("Failed to fetch role", e);
		}
		return new HashSet<String>();
	}
}
