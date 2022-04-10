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
import com.portal.job.controller.user.response.UserCertificationDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.mapper.UserCertificationDetailMapper;
import com.portal.job.model.UserCertificationDataModel;
import com.portal.job.service.UserCertificationDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserCertificationDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/certification")
public class UserCertificationDetailController extends JobPortalBaseController {

	@Autowired
	private UserCertificationDetailsService jsCertificationDetailSrvc;
	@Autowired
	UserCertificationDetailMapper jsCertificationDetailMapper;
	@Autowired
	private UserCertificationDataModelValidator userCertificationDataValidator;
	@Autowired
    private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserCertificationDetailController.class);

	@InitBinder("certificationDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userCertificationDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getCertificationDetailsForUser(Model model) {

		Set<UserCertificationDataModel> certificationDetailsList = jsCertificationDetailSrvc
				.getCertificationDetailByJobSeekerId(getUserId());
		model.addAttribute("jsCertificationDetailList",
				certificationDetailsList);
		return new ModelAndView("jobseeker/certification/view", "command",
				model);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserCertificationDataResponse saveCertificationDetails(
			@ModelAttribute("certificationDataValidator") @Validated UserCertificationDataModel jsCertificationDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserCertificationDataResponse certificationDataResponse = new UserCertificationDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			certificationDataResponse.setErrorMap(errors);
			certificationDataResponse.setStatus(ResponseStatus.Error.name());
			certificationDataResponse.setUserCertifications(null);
			if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
			{
				candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
			}
			return certificationDataResponse;
		}
		UserCertificationDataModel certificationData = jsCertificationDetailSrvc
				.createOrUpdate(jsCertificationDetailData, getUserId());
		certificationDataResponse.setErrorMap(null);
		certificationDataResponse.setStatus(ResponseStatus.Success.name());
		certificationDataResponse.setUserCertifications(certificationData);
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
		}
		return certificationDataResponse;
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserCertificationDataModel> getAllCertificationDetails(
			Model model) {
		Set<UserCertificationDataModel> certificationDetailsSet = jsCertificationDetailSrvc
				.getCertificationDetailByJobSeekerId(getUserId());
		return new ArrayList<UserCertificationDataModel>(
				certificationDetailsSet);
	}

	@RequestMapping(value = "viewById/{userCertificationId}", method = RequestMethod.GET)
	public @ResponseBody UserCertificationDataModel getCertificationDetailsById(
			@PathVariable String userCertificationId) {
		if (StringUtils.isEmpty(userCertificationId)) {
			// do something
		}
		long certificationId = Long.parseLong(userCertificationId);
		return jsCertificationDetailSrvc
				.getCertificationDetailById(certificationId);
	}

	@RequestMapping(value = "remove/{userCertificationId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeCertificationDetailById(
			@PathVariable String userCertificationId,HttpSession httpSession) throws ParseException {
		if (StringUtils.isEmpty(userCertificationId)) {
			// do something
		}
		UserCertificationDataModel certificationData = new UserCertificationDataModel();
		certificationData.setCertificationId(userCertificationId);
		boolean response=jsCertificationDetailSrvc.delete(certificationData, getUserId());
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
		}
		return response;
	}

}
