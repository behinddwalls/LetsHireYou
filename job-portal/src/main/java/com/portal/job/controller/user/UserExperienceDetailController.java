package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.constants.SessionConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserExperienceDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.helper.UserProfileHelper;
import com.portal.job.mapper.UserExperienceDetailsMapper;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserExperienceDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserExperienceDataModelValidator;

@RequestMapping(value = "/jobseeker/experience")
@Controller
public class UserExperienceDetailController extends JobPortalBaseController {

	@Autowired
	private UserExperienceDetailsService jsExperienceDetailSrvc;
	@Autowired
	UserExperienceDetailsMapper jsExperienceDetailMapper;
	@Autowired
	UserExperienceDataModelValidator userExperienceDataValidator;
	@Autowired
	UserDetailService userDetailService;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserExperienceDetailController.class);

	@InitBinder("experienceDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userExperienceDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getExperienceDetailsForUser(Model model) {

		Set<UserExperienceDataModel> experienceDetailsList = jsExperienceDetailSrvc
				.getExperienceDetailByUserId(getUserId());
		model.addAttribute("jsExperienceDetailList", experienceDetailsList);
		return new ModelAndView("jobseeker/experience/view", "command", model);

	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserExperienceDataModel> getAllExperienceDetailsForUser(
			Model model) {

		final Set<UserExperienceDataModel> experienceDetailsSet = jsExperienceDetailSrvc
				.getExperienceDetailByUserId(getUserId());

		experienceDetailsSet.forEach(x -> x
				.setDescription(replaceEndOfLineWithBreakLine(x
						.getDescription())));

		final List<UserExperienceDataModel> responseList = new ArrayList<UserExperienceDataModel>(
				experienceDetailsSet);
		UserProfileHelper.sortExperienceDetails(responseList);

		return responseList;

	}

	@RequestMapping(value = "viewById/{jobExpId}", method = RequestMethod.GET)
	public @ResponseBody UserExperienceDataModel getJobExperienceById(
			@PathVariable String jobExpId) {
		if (StringUtils.isEmpty(jobExpId)) {
			// do something
		}
		log.info("zzzz jobExpId = " + jobExpId);
		long expId = Long.parseLong(jobExpId);
		return jsExperienceDetailSrvc.getExperienceDetailByExpId(expId);

	}

	@RequestMapping(value = "remove/{jobExpId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeJobExperienceById(
			@PathVariable String jobExpId, HttpSession httpSession)
			throws Exception {
		log.info("zzzzzz delete job exp method called " + jobExpId);

		UserExperienceDataModel jsExperienceDetailData = new UserExperienceDataModel();
		jsExperienceDetailData.setExperienceId(jobExpId);
		boolean response = userDetailService
				.deleteExperienceAndUpdateUserDetail(getUserId(),
						jsExperienceDetailData);
		if (isJobSeekerProfileComplete(getUserId())) {
			httpSession.setAttribute(
					SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, true);
		} else {
			httpSession.setAttribute(
					SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, false);
		}
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return response;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserExperienceDataResponse saveExperienceDetails(
			@ModelAttribute("experienceDataValidator") @Validated UserExperienceDataModel jsExperienceDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {

		log.info("zzzzzz adding or updating job exp  "
				+ jsExperienceDetailData.getExperienceId());
		UserExperienceDataResponse experienceDataResponse = new UserExperienceDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			experienceDataResponse.setErrorMap(errors);
			experienceDataResponse.setStatus(ResponseStatus.Error.name());
			experienceDataResponse.setUserExperience(null);
			if (isJobSeekerProfileComplete(getUserId())) {
				httpSession.setAttribute(
						SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, true);
			} else {
				httpSession.setAttribute(
						SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, false);
			}
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return experienceDataResponse;
		}

		if (jsExperienceDetailData.getStartDate() == null) {
			experienceDataResponse.setUserExperience(jsExperienceDetailSrvc
					.createOrUpdateInTransaction(jsExperienceDetailData,
							getUserId()));
		} else {

			try {
				experienceDataResponse.setUserExperience(userDetailService
						.saveUserExperienceMonthAndExperienceDetail(
								getUserId(), jsExperienceDetailData));
			} catch (Exception e) {
				log.error("unable to save user experience "
						+ jsExperienceDetailData, e);
			}
			experienceDataResponse.setErrorMap(null);
			experienceDataResponse.setStatus(ResponseStatus.Success.name());
		}
		if (isJobSeekerProfileComplete(getUserId())) {
			httpSession.setAttribute(
					SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, true);
		} else {
			httpSession.setAttribute(
					SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, false);
		}
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return experienceDataResponse;
	}

}
