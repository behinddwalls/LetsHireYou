package com.portal.job.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author behinddwalls
 *
 */ 
@Controller
@RequestMapping("/error")
public class ErrorController extends JobPortalBaseController {

	@RequestMapping("500")
	public ModelAndView send500InternalServerError(final Model model,
			final HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		return getModelAndView(model, "error/500");
	}

	@RequestMapping("401")
	public ModelAndView send401UnAuthorizedError(final Model model,
			final HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
		return getModelAndView(model, "error/401");
	}

	@RequestMapping("403")
	public ModelAndView send403AccessDeniedError(final Model model,
			final HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpStatus.SC_FORBIDDEN);
		return getModelAndView(model, "error/403");
	}

	@RequestMapping("404")
	public ModelAndView send404PageNotFoundError(final Model model,
			final HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpStatus.SC_NOT_FOUND);
		return getModelAndView(model, "error/404");
	}
}
