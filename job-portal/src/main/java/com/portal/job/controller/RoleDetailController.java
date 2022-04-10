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

import com.portal.job.service.RoleDetailService;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/role")
public class RoleDetailController extends JobPortalBaseController {

	@Autowired
	private RoleDetailService roleDetailService;

	private static final Logger log = LoggerFactory
			.getLogger(RoleDetailController.class);

	@RequestMapping("search")
	public @ResponseBody Set<String> searchRole(
			@RequestParam("roleName") final String roleName) {
		try {
			return roleDetailService.serachRoleByName(roleName);
		} catch (Exception e) {
			log.error("Failed to fetch role", e);
		}
		return new HashSet<String>();
	}
 
}
