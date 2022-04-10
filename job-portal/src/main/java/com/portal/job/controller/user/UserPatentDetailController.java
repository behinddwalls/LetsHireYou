package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.portal.job.controller.user.response.UserPatentDataResponse;
import com.portal.job.enums.CountryCodes;
import com.portal.job.enums.ResponseStatus;
import com.portal.job.mapper.UserPatentDetailsMapper;
import com.portal.job.model.UserPatentDataModel;
import com.portal.job.service.UserPatentDetailsService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserPatentDataModelValidator;

@RequestMapping(value = "/jobseeker/patent")
@Controller
public class UserPatentDetailController extends JobPortalBaseController {

	@Autowired
	private UserPatentDetailsService jsPatentDetailSrvc;
	@Autowired
	private UserPatentDetailsMapper jsPatentDetailMapper;
	@Autowired
	private UserPatentDataModelValidator userPatentDataValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;

	private static Logger log = LoggerFactory
			.getLogger(UserPatentDetailController.class);

	@InitBinder("patentDataValidator")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userPatentDataValidator);
	}

	@RequestMapping(value = "view")
	public ModelAndView getPatentDetailsForUser(Model model) {

		Set<UserPatentDataModel> patentDetailsList = jsPatentDetailSrvc
				.getPatentDetailByJobSeekerId(getUserId());
		model.addAttribute("jsPatentDetailList", patentDetailsList);
		return new ModelAndView("jobseeker/patent/view", "command", model);

	}

	@RequestMapping(value = "patentOffice/viewAll", method = RequestMethod.GET)
	public @ResponseBody List<String> getAllPatentOffice(Model model) {
		log.info("qqqqqqqqqqqqqqqqqqqqqqqqqq");
		List<CountryCodes> officeList = Arrays.asList(CountryCodes.values());
		return officeList.stream().map(office -> office.getCCValue())
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody UserPatentDataResponse savePatentDetails(
			@ModelAttribute("patentDataValidator") @Validated UserPatentDataModel jsPatentDetailData,
			BindingResult bindingResult, HttpSession httpSession, Model model)
			throws ParseException {
		log.info("zzzzz patentDataModel = " + jsPatentDetailData.toString());
		UserPatentDataResponse patentDataResponse = new UserPatentDataResponse();
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			Map<String, String> errors = new HashMap<String, String>();
			for (FieldError fieldError : fieldErrors) {
				errors.put(fieldError.getField(),
						fieldError.getDefaultMessage());
			}
			patentDataResponse.setErrorMap(errors);
			patentDataResponse.setStatus(ResponseStatus.Error.name());
			patentDataResponse.setUserPatent(null);
			if (httpSession
					.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) != null
					&& (Boolean) httpSession
							.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)) {
				candidateExecutorService
						.computeJobsForCandidate(userDetailService
								.getUserDataModelByIdInTransaction(getUserId()));
			}
			return patentDataResponse;
		}

		UserPatentDataModel patentData = jsPatentDetailSrvc.createOrUpdate(
				jsPatentDetailData, getUserId());
		patentDataResponse.setErrorMap(null);
		patentDataResponse.setStatus(ResponseStatus.Success.name());
		patentDataResponse.setUserPatent(patentData);
		return patentDataResponse;
	}

	@RequestMapping(value = "viewAll", method = RequestMethod.GET)
	public @ResponseBody List<UserPatentDataModel> getAllPatentDetailsForUser(
			Model model) {

		Set<UserPatentDataModel> patentDetailsList = jsPatentDetailSrvc
				.getPatentDetailByJobSeekerId(getUserId());
		patentDetailsList.forEach(x -> x
				.setPatentDescription(replaceEndOfLineWithBreakLine(x
						.getPatentDescription())));
		return new ArrayList<UserPatentDataModel>(patentDetailsList);
	}

	@RequestMapping(value = "remove/{userPatentId}", method = RequestMethod.DELETE)
	public @ResponseBody boolean removePatentDataModel(
			@PathVariable String userPatentId, HttpSession httpSession)
			throws ParseException {
		UserPatentDataModel userPatentData = new UserPatentDataModel();
		userPatentData.setPatentId(userPatentId);
		boolean response = jsPatentDetailSrvc.delete(userPatentData,
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

	@RequestMapping(value = "viewById/{userPatentId}", method = RequestMethod.GET)
	public @ResponseBody UserPatentDataModel getUserPatentById(
			@PathVariable String userPatentId) {
		if (StringUtils.isEmpty(userPatentId)) {
			// do something
		}
		long expId = Long.parseLong(userPatentId);
		return jsPatentDetailSrvc.getPatentDetailById(expId);

	}
}
