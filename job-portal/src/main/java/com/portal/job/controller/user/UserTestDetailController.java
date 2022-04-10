package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.portal.job.constants.SessionConstants;
import com.portal.job.context.RequestContextContainer;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserTestsDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.mapper.UserTestDetailMapper;
import com.portal.job.model.UserTestDataModel;
import com.portal.job.service.UserTestDetailService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserTestDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/tests")
public class UserTestDetailController extends JobPortalBaseController {

	@Autowired
	private UserTestDetailService userTestDetailService;
	@Autowired
	private UserTestDetailMapper userTestDetailMapper;
	@Autowired
	private UserTestDataModelValidator userTestDataValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserTestDetailController.class);

	@InitBinder("testDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userTestDataValidator);
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserTestDataModel> getAllUserTestDetailsForUser(
			Model model) {
		List<UserTestDataModel> userTestDataModels = this.userTestDetailService
				.getTestDataModelForUserId(getUserId());
		userTestDataModels.forEach(x -> x
				.setTestDescription(replaceEndOfLineWithBreakLine(x
						.getTestDescription())));
		return userTestDataModels;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserTestsDataResponse addOrUpdateUserTestDataModel(
			@ModelAttribute("testDataValidator") @Validated UserTestDataModel testDataModel,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserTestsDataResponse userTestsDataResponse = new UserTestsDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			userTestsDataResponse.setErrorMap(errors);
			userTestsDataResponse.setStatus(ResponseStatus.Error.name());
			userTestsDataResponse.setUserTest(null);
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return userTestsDataResponse;
		}

		UserTestDataModel testData = this.userTestDetailService.addOrUpdate(
				testDataModel, getUserId());

		userTestsDataResponse.setErrorMap(null);
		userTestsDataResponse.setStatus(ResponseStatus.Success.name());
		userTestsDataResponse.setUserTest(testData);
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return userTestsDataResponse;
	}

	@RequestMapping(value = "remove/{userTestId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeJobSeekerTestDataModel(
			@PathVariable String userTestId, HttpSession httpSession)
			throws ParseException {
		UserTestDataModel testDataModel = new UserTestDataModel();
		testDataModel.setTestId(userTestId);
		boolean response = this.userTestDetailService.delete(testDataModel,
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

	@RequestMapping(value = "viewById/{userTestId}", method = RequestMethod.GET)
	public @ResponseBody UserTestDataModel getTestDetailsById(
			@PathVariable String userTestId) {
		if (StringUtils.isEmpty(userTestId)) {
			// do something
		}
		long testId = Long.parseLong(userTestId);
		return userTestDetailService.getTestDetailById(testId);
	}

}
