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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.constants.SessionConstants;
import com.portal.job.context.RequestContextContainer;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserEducationDataResponse;
import com.portal.job.enums.Degree;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.helper.UserProfileHelper;
import com.portal.job.mapper.UserEducationDetailMapper;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserEducationDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserEducationDataModelValidator;

@RequestMapping(value = "/jobseeker/education")
@Controller
public class UserEducationDetailController extends JobPortalBaseController {

	@Autowired
	private UserEducationDetailsService jsEducationDetailSrvc;
	@Autowired
	private UserEducationDetailMapper jsEducationdetailMapper;
	@Autowired
	private UserEducationDataModelValidator jsEducationDataValidator;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserEducationDetailController.class);

	@InitBinder("educationDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(jsEducationDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getEducationDetailsForUser(Model model) {

		Set<UserEducationDataModel> educationDetailsSet = jsEducationDetailSrvc
				.getEducationDetailByJobSeekerId(getUserId());
		model.addAttribute("educationData", educationDetailsSet);
		return new ModelAndView("jobseeker/education/view", "command", model);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserEducationDataResponse saveEducationDetails(
			@ModelAttribute("educationDataValidator") @Validated UserEducationDataModel jsEducationDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserEducationDataResponse educationResponse = new UserEducationDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			log.info("zzzzzzzz fieldErrors = " + fieldErrors);
			for (FieldError fieldError : fieldErrors) {
				log.info("zzzzz fieldError " + fieldError);
				log.info("zzzzzz fieldErrorValues field= "
						+ fieldError.getField() + "  rejectedValue=  "
						+ fieldError.getRejectedValue() + "  defaultmessage= "
						+ fieldError.getDefaultMessage());
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			System.out.println("ssssssssssssssssssss1");
			educationResponse.setErrorMap(errors);
			educationResponse.setStatus(ResponseStatus.Error.name());
			educationResponse.setEducationData(null);
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
			return educationResponse;
		}
		UserEducationDataModel educationData = userDetailService
				.saveUserInstituteTierAndEducationDetail(getUserId(),
						jsEducationDetailData);
		educationResponse.setErrorMap(null);
		educationResponse.setStatus(ResponseStatus.Success.name());
		educationResponse.setEducationData(educationData);
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
		return educationResponse;
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserEducationDataModel> getAllEducationDetailsForUser(
			Model model) {
		final Set<UserEducationDataModel> educationDetailsList = jsEducationDetailSrvc
				.getEducationDetailByJobSeekerId(getUserId());

		educationDetailsList
				.forEach(x -> {
					x.setDescription(replaceEndOfLineWithBreakLine(x
							.getDescription()));
				});

		final List<UserEducationDataModel> responseList = new ArrayList<UserEducationDataModel>(
				educationDetailsList);
		UserProfileHelper.sortEducationDetails(responseList);
		return responseList;
	}

	@RequestMapping(value = "viewByJsId/{jsId}", method = RequestMethod.GET)
	public @ResponseBody List<UserEducationDataModel> getAllEducationDetailsForUserId(
			@PathVariable String jsId) {
		log.info("zzzzz called");
		Long userId;
		try {
			userId = Long.parseLong(jsId);
		} catch (Exception e) {
			return null;
		}
		Set<UserEducationDataModel> educationDetailsList = jsEducationDetailSrvc
				.getEducationDetailByJobSeekerId(userId);
		return new ArrayList<UserEducationDataModel>(educationDetailsList);
	}

	@RequestMapping(value = "viewById/{userEducationId}", method = RequestMethod.GET)
	public @ResponseBody UserEducationDataModel getEducationDetailsById(
			@PathVariable String userEducationId) {
		if (StringUtils.isEmpty(userEducationId)) {
			// do something
		}
		long educationId = Long.parseLong(userEducationId);
		return jsEducationDetailSrvc.getEducationDetailById(educationId);
	}

	@RequestMapping(value = "remove/{userEducationId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeEducationDetailById(
			@PathVariable String userEducationId, HttpSession httpSession)
			throws ParseException {
		if (StringUtils.isEmpty(userEducationId)) {
			// do something
		}
		UserEducationDataModel educationData = new UserEducationDataModel();
		educationData.setEducationId(userEducationId);
		boolean response = userDetailService
				.deleteEducationAndUpdateInstituteTier(getUserId(),
						educationData);
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

	// change to return list
	@RequestMapping(value = "gerEduForDegree")
	public @ResponseBody UserEducationDataModel getEducationDetailsForDegree(
			@RequestParam("degree") String degree, Model model) {
		Set<UserEducationDataModel> educationDataList = jsEducationDetailSrvc
				.getAllJobSeekerWithDegree(degree);
		for (UserEducationDataModel educationDetail : educationDataList) {
			log.info(educationDetail.toString());
		}
		return educationDataList.iterator().next();
	}

	// mock
	@RequestMapping(value = "gerEduForUserMock")
	public @ResponseBody boolean getEducationDetailsForUserMock(Model model) {
		Set<UserEducationDataModel> educationDataList = jsEducationDetailSrvc
				.getEducationDetailByJobSeekerId((long) 1);
		for (UserEducationDataModel educationDetail : educationDataList) {
			log.info(educationDetail.toString());
		}
		return true;
	}

	// mock change return type to list
	@RequestMapping(value = "getEduForDegreeMock")
	public @ResponseBody boolean getEducationDetailsForDegreeMock(
			@RequestParam("degree") String degree, Model model) {
		Set<UserEducationDataModel> educationDataList = jsEducationDetailSrvc
				.getAllJobSeekerWithDegree("HighSchool");
		for (UserEducationDataModel educationDetail : educationDataList) {
			log.info(educationDetail.toString());
		}
		return true;
	}

	// mock change to return the object
	@RequestMapping(value = "createNew")
	public @ResponseBody boolean getCol(Model model, HttpSession httpSession)
			throws ParseException {
		UserEducationDataModel educationData = jsEducationDetailSrvc
				.createOrUpdate(jsEducationdetailMapper.getMockEducationData(
						"", "", Degree.B), (long) 1);
		log.info("zzzzzzz educastionData " + educationData.toString());
		return true;

	}

	// mock change to return the object
	@RequestMapping(value = "update")
	public @ResponseBody boolean saveMock(Model model, HttpSession httpSession)
			throws ParseException {
		UserEducationDataModel educationData = jsEducationDetailSrvc
				.createOrUpdate(jsEducationdetailMapper.getMockEducationData(
						"4", "whats in the name", Degree.A), (long) 1);
		log.info("zzzzzzz educastionData " + educationData.toString());
		return true;

	}

}
