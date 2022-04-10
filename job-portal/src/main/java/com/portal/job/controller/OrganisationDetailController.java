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

import com.portal.job.service.OrganisationDetailService;

@Controller
@RequestMapping("organisation")
public class OrganisationDetailController extends JobPortalBaseController {

	private static final Logger log = LoggerFactory
			.getLogger(RoleDetailController.class);
	@Autowired
	private OrganisationDetailService organisationDetailService;

	@RequestMapping("search")
	public @ResponseBody Set<String> searchOrganisation(
			@RequestParam("orgName") final String orgName) {
		try {
			return organisationDetailService.serachOrganisationByName(orgName);
		} catch (Exception e) {
			log.error("Failed to fetch Organisation", e);
		}
		return new HashSet<String>();
	}

	@RequestMapping("getall")
	public @ResponseBody Set<String> getAllOrganisation() {
		try {
			return organisationDetailService.getAllOrganisation();
		} catch (Exception e) {
			log.error("Failed to fetch Organisation", e);
		}
		return new HashSet<String>();
	}
}
