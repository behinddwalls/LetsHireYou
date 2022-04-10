package com.portal.job.controller.user;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.constants.SessionConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.enums.Degree;
import com.portal.job.helper.AccountVerificationHelper;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.ProfileHeaderDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserDataModel;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.task.CandidateTaskExecutorService;
import com.portal.job.validator.UserBasicDataModelValidator;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/jobseeker")
public class UserDetailController extends JobPortalBaseController {

	private static final Logger log = LoggerFactory
			.getLogger(UserDetailController.class);

	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private BasicAccountDetailService accountDetailService;

	@Autowired
	private UserBasicDataModelValidator jobseekerBasicDataModelValidator;
	@Autowired
	private CandidateTaskExecutorService candidateExecutorService;

	@InitBinder("profile/userBasicDataModel")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(jobseekerBasicDataModelValidator);
	}

	@RequestMapping("profile/Degree/viewAll")
	public @ResponseBody List<String> getAllDegree(Model model) {
		List<Degree> degreeList = Arrays.asList(Degree.values());
		return degreeList.stream().map(d -> d.getDegreeName())
				.collect(Collectors.toList());

		// for(Degree d : Degree.values()){
		//
		// if(MyStringUtils.isSubsequence(degreeSearchKey.toLowerCase(),
		// d.name().toLowerCase())){
		// degreeSet.add(d.getDegreeName());
		// continue;
		// }
		//
		// if(MyStringUtils.isSubsequence(degreeSearchKey.toLowerCase(),
		// d.getDegreeName().toLowerCase())){
		// degreeSet.add(d.getDegreeName());
		// }
		//
		// }
		// return degreeSet;

	}

	@RequestMapping("profile/view")
	public ModelAndView getJobseekerDetail(
			@RequestParam(value = "incomplete", required = false) final Integer incomplete,
			Model model) {
		log.info("zzzzzzzz in jobseeker view profile");
		final UserDataModel userDataModel = this.userDetailService
				.getUserDataModelByIdInTransaction(getUserId());
		model.addAttribute("userData", userDataModel);
		if (incomplete != null && incomplete == 1) {
			model.addAttribute("incomplete",
					"Please complete your jobseeker profile to get started.");
		}
		return getModelAndView(model, "jobseeker/profile");
	}

	@RequestMapping("profile/profileHeader/save")
	public @ResponseBody UserBasicDataModel saveJobSeekerProfileHeader(
			ProfileHeaderDataModel profileHeader, HttpSession httpSession)
			throws ParseException {
		log.info("ProfileHeader = " + profileHeader.toString());
		UserBasicDataModel userBasicDataModel = userDetailService
				.updateUserProfileHeader(profileHeader, getUserId(),
						getAccountId());
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
		return userBasicDataModel;

	}
	
	@RequestMapping("profile/complete")
	public @ResponseBody Boolean isProfileComplete(HttpSession httpSession)
			throws ParseException {
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			return true;
		}
		return false;

	}

	@RequestMapping("profile/basic/view")
	public @ResponseBody UserBasicDataModel getUserBasicDetail() {
		final UserBasicDataModel userBasicDataModel = userDetailService
				.getUserBasicDataModel(getUserId());
		userBasicDataModel
				.setSummary(replaceEndOfLineWithBreakLine(userBasicDataModel
						.getSummary()));
		return userBasicDataModel;
	}

	@RequestMapping("profile/basic/view/edit")
	public @ResponseBody UserBasicDataModel editViewUserBasicDetail() {
		final UserBasicDataModel userBasicDataModel = userDetailService
				.getUserBasicDataModel(getUserId());
		return userBasicDataModel;
	}

	@RequestMapping(value = "profile/profileImage/view", method = RequestMethod.GET)
	public @ResponseBody byte[] getProfileImage() {
		return null;
	}

	/*
	 * @RequestMapping(value = "profile/profileImage/upload", method =
	 * RequestMethod.POST) public @ResponseBody String
	 * uploadProfileImage(FileUploadModel file) { ClassLoader classLoader =
	 * getClass().getClassLoader(); if (classLoader.getResource(imageFolderPath
	 * + "1") == null) {
	 * 
	 * }
	 * 
	 * if (!file.getFile().isEmpty()) { try { File newFile = new
	 * File(classLoader.getResource( imageFolderPath +
	 * file.getFile().getOriginalFilename()) .getFile());
	 * file.getFile().transferTo(null); } catch (Exception e) {
	 * 
	 * } }
	 * 
	 * return null; }
	 */

	private static String imageFolderPath = "Images/";

	@Deprecated
	@RequestMapping(value = "account/verify")
	public ModelAndView verifyJobSeekerAccount(
			@RequestParam(required = false) final Long accountId,
			@RequestParam(required = false) final String verificationKey,
			Model model, final HttpServletRequest httpRequest) {
		try {
			if (Optional.ofNullable(verificationKey).isPresent()
					&& Optional.ofNullable(accountId).isPresent()) {
				BasicAccountDataModel accountDataModel = AccountVerificationHelper
						.getBasicAccountDataModelForAccountVerification(
								accountId, verificationKey,
								accountDetailService, signedInAs());

				if (Optional.ofNullable(accountDataModel).isPresent()) {
					accountDataModel.setJobseekerVerificationKey(null);
					accountDataModel.setJobseeker(true);
					accountDataModel.setEmailIdVerified(true);
					accountDetailService
							.addOrUpdateAccountDetail(accountDataModel);
					final HttpSession session = httpRequest.getSession();
					session.invalidate();
					model.addAttribute(
							"successMessage",
							"Account Verified Successfully. Please go to Recruiter sign in page to start using JobXR.");
				} else {
					model.addAttribute("errorMessage",
							"Malformed or invalid verification key: "
									+ verificationKey);
				}

			} else {
				model.addAttribute("verifyMessage",
						"Please verify your account. Please check your email (inbox/spam).");
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			model.addAttribute("errorMessage",
					"Malformed or invalid verification key");
		}
		return new ModelAndView("user/verify-account", "command", model);
	}

}
