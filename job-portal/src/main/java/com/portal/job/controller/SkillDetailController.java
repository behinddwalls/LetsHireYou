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

import com.portal.job.service.SkillDetailService;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("skilldetail")
public class SkillDetailController extends JobPortalBaseController{

	private static final Logger log = LoggerFactory
			.getLogger(SkillDetailController.class);

	@Autowired
	private SkillDetailService skillDetailService;

	@RequestMapping("search")
	public @ResponseBody Set<String> searchSkills(
			@RequestParam("skillName") final String skillName) {
		log.info("Skill request is coming to controller");
		try {
			return this.skillDetailService.serachSkillByName(skillName);
		} catch (Exception e) {
			log.error("Failed to fetch Skill", e);
		}
		return new HashSet<String>();
	}

} 