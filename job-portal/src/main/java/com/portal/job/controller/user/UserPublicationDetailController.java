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
import com.portal.job.controller.user.response.UserPublicationDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.mapper.UserPublicationDetailsMapper;
import com.portal.job.model.UserPublicationDataModel;
import com.portal.job.service.UserPublicationDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserPublicationDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/publication")
public class UserPublicationDetailController extends JobPortalBaseController {

	@Autowired
	private UserPublicationDetailsService userPublicationDetailsService;
	@Autowired
	private UserPublicationDetailsMapper userPublicationDetailsMapper;
	@Autowired
	private UserPublicationDataModelValidator userPublicationDataValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserPublicationDetailController.class);

	@InitBinder("publicationDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userPublicationDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getUserPublicationDataModel(Model model) {
		Set<UserPublicationDataModel> userPublicationDataModelList = this.userPublicationDetailsService
				.getUserPublicationDataModelByUserId(getUserId());
		model.addAttribute("userPublicationDataModelList",
				userPublicationDataModelList);
		return new ModelAndView("jobseeker/publicationdetails/view", "command",
				model);

	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserPublicationDataModel> getAllUserPublicationDataModelForUser(
			Model model) {
		Set<UserPublicationDataModel> userPublicationDataModelList = this.userPublicationDetailsService
				.getUserPublicationDataModelByUserId(getUserId());
		userPublicationDataModelList.forEach(x -> x
				.setPublicationDescription(replaceEndOfLineWithBreakLine(x
						.getPublicationDescription())));
		return new ArrayList<UserPublicationDataModel>(
				userPublicationDataModelList);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserPublicationDataResponse addOrUpdateJobSeekerProjectDataModel(
			@ModelAttribute("publicationDataValidator") @Validated UserPublicationDataModel publicationDataModel,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserPublicationDataResponse publicationResponse = new UserPublicationDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			publicationResponse.setErrorMap(errors);
			publicationResponse.setStatus(ResponseStatus.Error.name());
			publicationResponse.setPublicationData(null);
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return publicationResponse;
		}

		UserPublicationDataModel userPublicationDataModel = this.userPublicationDetailsService
				.addOrUpdate(publicationDataModel, getUserId());
		publicationResponse.setErrorMap(null);
		publicationResponse.setStatus(ResponseStatus.Success.name());
		publicationResponse.setPublicationData(userPublicationDataModel);
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return publicationResponse;

	}

	@RequestMapping(value = "remove/{userPublicationId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeJobSeekerPublicationDataModel(
			@PathVariable String userPublicationId, HttpSession httpSession)
			throws ParseException {
		UserPublicationDataModel publicationDataModel = new UserPublicationDataModel();
		publicationDataModel.setPublicationId(userPublicationId);
		boolean response = userPublicationDetailsService.delete(
				publicationDataModel, getUserId());
		if (httpSession
				.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
				&& (Boolean) httpSession
						.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
			candidateExecutorService.computeJobsForCandidate(userDetailService
					.getUserDataModelByIdInTransaction(getUserId()));
		}
		return response;

	}

	@RequestMapping(value = "viewById/{userPublicationId}", method = RequestMethod.GET)
	public @ResponseBody UserPublicationDataModel getPublicationDetailsById(
			@PathVariable String userPublicationId) {
		if (StringUtils.isEmpty(userPublicationId)) {
			// do something
		}
		long publicationId = Long.parseLong(userPublicationId);
		return userPublicationDetailsService
				.getPublicationDetailById(publicationId);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Mock
	// ///////////////////////////////////////////////////////////////////////////////////////

}
