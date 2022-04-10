package com.portal.job.controller.recruiter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.controller.JobPortalBaseController;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("recruiter")
public class RecruiterDashboard extends JobPortalBaseController {

	@RequestMapping("dashboard")
	public ModelAndView getRecruiterDashboard(Model model) {

		return getModelAndView(model, "recruiter/dashboard");
	}

}
