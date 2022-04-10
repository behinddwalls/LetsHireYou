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
import com.portal.job.context.RequestContextContainer;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserProjectDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.mapper.UserProjectDetailMapper;
import com.portal.job.model.UserProjectDataModel;
import com.portal.job.service.UserProjectDetailService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserProjectDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/project")
public class UserProjectDetailController extends JobPortalBaseController {

	@Autowired
	private UserProjectDetailService userProjectDetailService;
	@Autowired
	private UserProjectDetailMapper userProjectDetailMapper;
	@Autowired
	private UserProjectDataModelValidator userProjectDataValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserProjectDetailController.class);

	@InitBinder("projectDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userProjectDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getProjectDataModel(Model model) {
		Set<UserProjectDataModel> jobSeekerProjectDataModelList = this.userProjectDetailService
				.getProjectDataModelForUserId(getUserId());
		model.addAttribute("jobSeekerProjectDataModelList",
				jobSeekerProjectDataModelList);
		return new ModelAndView("jobseeker/projectdetails/view", "command",
				model);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserProjectDataResponse addOrUpdateProjectDataModel(
			@ModelAttribute("projectDataValidator") @Validated UserProjectDataModel projectDataModel,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserProjectDataResponse projectDataResponse = new UserProjectDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			projectDataResponse.setErrorMap(errors);
			projectDataResponse.setStatus(ResponseStatus.Error.name());
			projectDataResponse.setProjectData(null);
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return projectDataResponse;
		}
		UserProjectDataModel jobSeekerProjectDataModel = this.userProjectDetailService
				.addOrUpdate(projectDataModel, getUserId());

		projectDataResponse.setErrorMap(null);
		projectDataResponse.setStatus(ResponseStatus.Success.name());
		projectDataResponse.setProjectData(jobSeekerProjectDataModel);
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return projectDataResponse;

	}

	@RequestMapping(value = "remove/{userProjectId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeProjectDataModel(
			@PathVariable String userProjectId, HttpSession httpSession)
			throws ParseException {
		UserProjectDataModel userProjectData = new UserProjectDataModel();
		userProjectData.setProjectId(userProjectId);
		boolean response = userProjectDetailService.delete(userProjectData,
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

	@RequestMapping(value = "viewById/{userProjectId}", method = RequestMethod.GET)
	public @ResponseBody UserProjectDataModel getUserProjectById(
			@PathVariable String userProjectId) {
		if (StringUtils.isEmpty(userProjectId)) {
			// do something
		}
		log.info("zzzz projectId = " + userProjectId);
		long expId = Long.parseLong(userProjectId);
		return userProjectDetailService.getProjectDetailById(expId);

	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserProjectDataModel> getAllProjectDetailsForUser(
			Model model) {
		Set<UserProjectDataModel> projectDataSet = userProjectDetailService
				.getProjectDataModelForUserId(getUserId());
		projectDataSet.forEach(x -> x
				.setProjectDescription(replaceEndOfLineWithBreakLine(x
						.getProjectDescription())));
		return new ArrayList<UserProjectDataModel>(projectDataSet);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping("mockSave")
	public @ResponseBody boolean mockAddOrUpdateJobSeekerProjectDataModel(
			Model model) throws NumberFormatException, ParseException {
		//
		UserProjectDataModel projectDataModel = this.userProjectDetailMapper
				.getMockJobSeekerProjectDataModel(null, "Computer vision");

		UserProjectDataModel JobSeekerProjectDataModel = this.userProjectDetailService
				.addOrUpdate(projectDataModel, Long.valueOf("1"));
		model.addAttribute("jobSeekerProjectDataModel",
				JobSeekerProjectDataModel);
		// return new ModelAndView("jobseeker/projectdetails/mockSave",
		// "command", model);
		return true;
	}

	@RequestMapping(value = "mockview")
	public @ResponseBody boolean mockGetJobSeekerProjectDataModel(Model model) {
		Set<UserProjectDataModel> jobSeekerProjectDataModelList = this.userProjectDetailService
				.getProjectDataModelForUserId(Long.valueOf("1"));
		model.addAttribute("jobSeekerProjectDataModelList",
				jobSeekerProjectDataModelList);
		// return new ModelAndView("jobseeker/projectdetails/view", "command",
		// model);
		return true;
	}

}
