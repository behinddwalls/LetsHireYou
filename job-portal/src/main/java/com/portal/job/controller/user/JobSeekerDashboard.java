package com.portal.job.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.controller.JobPortalBaseController;

@Controller
@RequestMapping("jobseeker")
public class JobSeekerDashboard extends JobPortalBaseController {

	private static Logger log = LoggerFactory
			.getLogger(JobSeekerDashboard.class);

	@RequestMapping("dashboard")
	public ModelAndView getJobSeekerDashboard(Model model) {
		log.info("GGGGGGGG in js dashboard");
		return getModelAndView(model, "jobseeker/dashboard");
	}

}
