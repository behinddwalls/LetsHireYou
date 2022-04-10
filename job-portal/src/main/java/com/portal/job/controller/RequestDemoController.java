package com.portal.job.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.dao.model.RequestDemoDetail;
import com.portal.job.handler.GoogleRecaptchaHandler;
import com.portal.job.service.RequestDemoDetailService;
import com.portal.job.validator.RequestDemoDataModelValidator;

@Controller
@RequestMapping("/register/request")
public class RequestDemoController extends JobPortalBaseController {

	private final static Logger log = LoggerFactory
			.getLogger(RequestDemoController.class);

	@Autowired
	private GoogleRecaptchaHandler googleRecaptchaHandler;

	@Autowired
	private RequestDemoDetailService requestDemoDetailService;

	@Autowired
	private RequestDemoDataModelValidator requestDemoDataModelValidator;

	@InitBinder("requestDemoDetail")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(requestDemoDataModelValidator);
	}

	private final static String GOOGLE_RECAPTCHA_RESPONSE = "g-recaptcha-response";

	@RequestMapping(value = { "demo" }, method = RequestMethod.GET)
	public ModelAndView showRequestDemoPage(final Model model) {

		model.addAttribute("requestDemoDetail", new RequestDemoDetail());
		return getModelAndView(model, "common/request-demo");
	}

	@RequestMapping(value = { "demo" }, method = RequestMethod.POST)
	public ModelAndView submitRequestDemoPage(
			@RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
			@ModelAttribute("requestDemoDetail") final RequestDemoDetail requestDemoDetail,
			final BindingResult bindingResult, final Model model) {
		try {
			boolean isRecaptchaValid = googleRecaptchaHandler
					.isRecaptchaValid(gRecaptchaResponse);

			if (!bindingResult.hasErrors() && isRecaptchaValid) {
				requestDemoDetailService.addRequestDemo(requestDemoDetail);
				model.addAttribute("success", "success");
				model.addAttribute("requestDemoDetail", new RequestDemoDetail());
				return getModelAndView(model, "common/request-demo");
			}
			if (!isRecaptchaValid) {
				model.addAttribute("error", "Recaptcha failed.");
			}
		} catch (Exception e) {
			model.addAttribute("error", "Operation failed.");
			log.error("Failed to add request demo", e);
		}
		model.addAttribute("requestDemoDetail", requestDemoDetail);
		return getModelAndView(model, "common/request-demo");
	}
}
