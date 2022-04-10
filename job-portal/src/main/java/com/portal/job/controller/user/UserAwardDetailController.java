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

import com.portal.job.constants.SessionConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.response.UserAwardDataResponse;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.model.UserAwardDataModel;
import com.portal.job.service.UserAwardDetailService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserAwardDataModelValidator;

@Controller
@RequestMapping(value = "/jobseeker/award")
public class UserAwardDetailController extends JobPortalBaseController {

	@Autowired
	private UserAwardDetailService awardDetailService;
	@Autowired
	private UserAwardDataModelValidator userAwardDataValidator;
	@Autowired
    private CandidateTaskExecutorService candidateExecutorService;
	private static Logger log = LoggerFactory
			.getLogger(UserAwardDetailController.class);

	@InitBinder("awardDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userAwardDataValidator);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserAwardDataResponse saveAwardDetails(
			@ModelAttribute("awardDataValidator") @Validated UserAwardDataModel jsAwardDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		UserAwardDataResponse awardDataResponse = new UserAwardDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			awardDataResponse.setErrorMap(errors);
			awardDataResponse.setStatus(ResponseStatus.Error.name());
			awardDataResponse.setAwardDataModel(null);
			if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
			{
				candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
			}
			return awardDataResponse;
		}
		UserAwardDataModel awardData = awardDetailService.createOrUpdate(
				jsAwardDetailData, getUserId());
		awardDataResponse.setErrorMap(null);
		awardDataResponse.setStatus(ResponseStatus.Success.name());
		awardDataResponse.setAwardDataModel(awardData);
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(this.getUserId()));
		}
		return awardDataResponse;
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserAwardDataModel> getAllAwardDetails(Model model) {
		Set<UserAwardDataModel> awardDetailsSet = awardDetailService
				.getAwardsByJobseekerId(getUserId());
		return new ArrayList<UserAwardDataModel>(awardDetailsSet);
	}

	@RequestMapping(value = "viewById/{userAwardId}", method = RequestMethod.GET)
	public @ResponseBody UserAwardDataModel getAwardDetailsById(
			@PathVariable String userAwardId) {
		if (StringUtils.isEmpty(userAwardId)) {
			// do something
		}
		long awardId = Long.parseLong(userAwardId);
		return awardDetailService.getAwardsByAwardId(awardId);
	}

	@RequestMapping(value = "remove/{userAwardId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removeAwardDetailById(
			@PathVariable String userAwardId, HttpSession httpSession) throws ParseException {
		if (StringUtils.isEmpty(userAwardId)) {
			// do something
		}
		UserAwardDataModel awardData = new UserAwardDataModel();
		awardData.setAwardId(userAwardId);
		boolean response=awardDetailService.delete(awardData, getUserId());
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
		}
		return response;
	}

}
