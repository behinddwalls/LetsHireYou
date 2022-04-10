package com.portal.job.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.enums.RecruiterType;
import com.portal.job.handler.GoogleRecaptchaHandler;
import com.portal.job.model.ExpertRegisterDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.validator.ExpertRegisterDataModelValidator;
import com.portal.job.validator.JobseekerRegisterDataModelValidator;
import com.portal.job.validator.RecruiterRegisterDataModelValidator;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/register")
public class RegisterController extends JobPortalBaseController {
	private final static String GOOGLE_RECAPTCHA_RESPONSE = "g-recaptcha-response";
	private final static String UTF_8 = "UTF-8";
	private static final Logger log = LoggerFactory
			.getLogger(RegisterController.class);
	@Autowired
	private BasicAccountDetailService accountDetailService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private GoogleRecaptchaHandler googleRecaptchaHandler;
	@Autowired
	private RecruiterRegisterDataModelValidator recruiterRegisterDataModelValidator;

	@InitBinder("recruiterRegisterDataModel")
	private void initBinderRecruiter(WebDataBinder binder) {
		binder.setValidator(recruiterRegisterDataModelValidator);
	}

	@Autowired
	private JobseekerRegisterDataModelValidator jobseekerRegisterDataModelValidator;

	@InitBinder("jobseekerRegisterDataModel")
	private void initBinderJobseeker(WebDataBinder binder) {
		binder.setValidator(jobseekerRegisterDataModelValidator);
	}

	@Autowired
	private ExpertRegisterDataModelValidator expertRegisterDataModelValidator;

	@InitBinder("expertRegisterDataModel")
	private void initBinderexpert(WebDataBinder binder) {
		binder.setValidator(expertRegisterDataModelValidator);
	}

	@RequestMapping(value = "jobseeker", method = RequestMethod.POST)
	public ModelAndView verifySigninJobseeker(
			@RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
			@ModelAttribute("jobseekerRegisterDataModel") @Validated final JobseekerRegisterDataModel jobseekerRegisterDataModel,
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		boolean isRecaptchaValid = googleRecaptchaHandler
				.isRecaptchaValid(gRecaptchaResponse);
		// validate
		if (!bindingResult.hasErrors() && isRecaptchaValid) {
			try {
				accountDetailService
						.registerJobseeker(jobseekerRegisterDataModel);

				return new ModelAndView("redirect:/signin/jobseeker",
						"command", null);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage",
						" Something went wrong while processing your request.");
			}
		}
		if (!isRecaptchaValid) {
			model.addAttribute("errorMessage", "reCaptcha Validation Failed.");
		}
		model.addAttribute(bindingResult.getModel());
		return new ModelAndView("jobseeker/register", "command", model);
	}

	@RequestMapping(value = "recruiter", method = RequestMethod.POST)
	public ModelAndView verifySigninRecruiter(
			@RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
			@ModelAttribute("recruiterRegisterDataModel") @Validated final RecruiterRegisterDataModel recruiterRegisterDataModel,
			BindingResult bindingResult, Model model)
			throws NoSuchAlgorithmException {
		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		boolean isRecaptchaValid = googleRecaptchaHandler
				.isRecaptchaValid(gRecaptchaResponse);
		// validate
		if (!bindingResult.hasErrors() && isRecaptchaValid) {
			try {
				accountDetailService
						.registerRecruiter(recruiterRegisterDataModel);
				return new ModelAndView("redirect:/signin/recruiter",
						"command", model);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage",
						" Something went wrong while processing your request.");
			}
		}
		if (!isRecaptchaValid) {
			model.addAttribute("errorMessage", "reCaptcha Validation Failed.");
		}
		model.addAttribute(bindingResult.getModel());
		model.addAttribute("recruiterTypeMap",
				RecruiterType.getRecruiterTypeToNameMap());
		return new ModelAndView("recruiter/register", "command", model);
	}

	@RequestMapping(value = "expert", method = RequestMethod.POST)
	public ModelAndView verifySigninExpert(
			@RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
			@ModelAttribute("expertRegisterDataModel") @Validated final ExpertRegisterDataModel expertRegisterDataModel,
			BindingResult bindingResult, Model model)
			throws NoSuchAlgorithmException {
		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		boolean isRecaptchaValid = googleRecaptchaHandler
				.isRecaptchaValid(gRecaptchaResponse);
		// validate
		if (!bindingResult.hasErrors() && isRecaptchaValid) {
			accountDetailService.registerExpert(expertRegisterDataModel);
		}
		if (!isRecaptchaValid) {
			model.addAttribute("errorMessage", "reCaptcha Validation Failed.");
		}
		model.addAttribute(bindingResult.getModel());
		return new ModelAndView("expert/register", "command", model);
	}

	@RequestMapping(value = "jobseeker", method = RequestMethod.GET)
	public ModelAndView jobseekerSigninPage(Model model) {
		model.addAttribute("jobseekerRegisterDataModel",
				new JobseekerRegisterDataModel());
		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		return new ModelAndView("jobseeker/register", "command", model);
	}

	@RequestMapping(value = "recruiter", method = RequestMethod.GET)
	public ModelAndView recruiterSigninPage(Model model) {
		model.addAttribute("recruiterRegisterDataModel",
				new RecruiterRegisterDataModel());
		model.addAttribute("recruiterTypeMap",
				RecruiterType.getRecruiterTypeToNameMap());

		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		return new ModelAndView("recruiter/register", "command", model);
	}

	@RequestMapping(value = "expert", method = RequestMethod.GET)
	public ModelAndView expertSigninPage(Model model) {

		final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
		if (null != redirectUserIfAuthorized) {
			return redirectUserIfAuthorized;
		}
		return new ModelAndView("expert/register", "command", model);
	}

}
