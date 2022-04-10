package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.portal.job.context.RequestContextContainer;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserVolunteerDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.enums.VolunteerCause;
import com.portal.job.mapper.UserVolunteerDetailsMapper;
import com.portal.job.model.UserVolunteerDataModel;
import com.portal.job.service.UserVolunteerDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserVolunteerDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/volunteer")
public class UserVolunteerDetailController extends JobPortalBaseController {

	@Autowired
	private UserVolunteerDetailsService jsVolunteerDetailSrvc;
	@Autowired
	UserVolunteerDetailsMapper jsVolunteerDetailMapper;
	@Autowired
	UserVolunteerDataModelValidator userVolunteerDataValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserVolunteerDetailController.class);

	@InitBinder("volunteerDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userVolunteerDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getVolunteerDetailsForUser(Model model) {

		List<UserVolunteerDataModel> volunteerDetailsList = jsVolunteerDetailSrvc
				.getVolunteerDetailByJobSeekerId(getUserId());
		model.addAttribute("jsVolunteerDetailList", volunteerDetailsList);
		return new ModelAndView("jobseeker/volunteer/view", "command", model);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserVolunteerDataResponse saveVolunteerDetails(
			@ModelAttribute("volunteerDataValidator") @Validated UserVolunteerDataModel jsVolunteerDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		log.info("AAAAAAAAA volunteer detalis= "
				+ jsVolunteerDetailData.toString());

		UserVolunteerDataResponse volunteerDataResponse = new UserVolunteerDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			volunteerDataResponse.setErrorMap(errors);
			volunteerDataResponse.setStatus(ResponseStatus.Error.name());
			volunteerDataResponse.setVolunteerData(null);
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return volunteerDataResponse;
		}

		UserVolunteerDataModel volunteerData = jsVolunteerDetailSrvc
				.createOrUpdate(jsVolunteerDetailData, getUserId());
		volunteerDataResponse.setErrorMap(null);
		volunteerDataResponse.setStatus(ResponseStatus.Success.name());
		volunteerDataResponse.setVolunteerData(volunteerData);
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return volunteerDataResponse;
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserVolunteerDataModel> getAllVolunteerDetailsForUser(
			Model model) {
		List<UserVolunteerDataModel> volunteerDetailsList = jsVolunteerDetailSrvc
				.getVolunteerDetailByJobSeekerId(getUserId());
		volunteerDetailsList.forEach(x -> x
				.setVolunteerDescription(replaceEndOfLineWithBreakLine(x
						.getVolunteerDescription())));

		return volunteerDetailsList;
	}

	@RequestMapping(value = "causes/viewAll", method = RequestMethod.GET)
	public @ResponseBody List<String> getAllVolunteerCauses(Model model) {
		List<VolunteerCause> causeList = Arrays.asList(VolunteerCause.values());
		return causeList.stream().map(cause -> cause.getVolunteerCause())
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "remove/{userVolunteerId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeJobSeekerVolunteerDataModel(
			@PathVariable String userVolunteerId, HttpSession httpSession)
			throws ParseException {
		UserVolunteerDataModel volunteerDataModel = new UserVolunteerDataModel();
		volunteerDataModel.setVolunteerId(userVolunteerId);
		boolean response = jsVolunteerDetailSrvc.delete(volunteerDataModel,
				getUserId());
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return response;

	}

	@RequestMapping(value = "viewById/{userVolunteerId}", method = RequestMethod.GET)
	public @ResponseBody UserVolunteerDataModel getVolunteerDetailsById(
			@PathVariable String userVolunteerId) {
		if (StringUtils.isEmpty(userVolunteerId)) {
			// do something
		}
		long volunteerId = Long.parseLong(userVolunteerId);
		return jsVolunteerDetailSrvc.getVolunteerDetailById(volunteerId);
	}

}
