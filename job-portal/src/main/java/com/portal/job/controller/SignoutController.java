package com.portal.job.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author behinddwalls
 *
 */
@RequestMapping("/signout")
@Controller
public class SignoutController extends JobPortalBaseController {

	@RequestMapping("") 
	public ModelAndView signoutUser(Model model,
			HttpServletRequest httpServletRequest) {

		final HttpSession httpSession = httpServletRequest.getSession(false);
		if (null != httpSession) {
			httpSession.invalidate();
		}
		return getModelAndView(null, "redirect:/home?logout=true");
	}

}
