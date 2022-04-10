package com.portal.job.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.context.RequestContextContainer;
import com.portal.job.enums.UserType;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserEducationDetailsService;
import com.portal.job.service.UserExperienceDetailsService;

/**
 * Use it only when user is signed in.
 * 
 * @author behinddwalls
 *
 */
public class JobPortalBaseController {

	@Autowired
	protected UserDetailService userDetailService;
	@Autowired
	protected UserExperienceDetailsService userExperienceDetailsService;
	@Autowired
	protected UserEducationDetailsService userEducationDetailsService;

	private static final Logger log = LoggerFactory.getLogger(JobPortalBaseController.class);

	protected boolean isUserAuthorized() {
		return RequestContextContainer.getRequestContext().isUserAuthorized();
	}

	protected boolean isJobseeker() {
		return RequestContextContainer.getRequestContext().isJobseeker();
	}

	protected boolean isRecruiter() {
		return RequestContextContainer.getRequestContext().isRecruiter();
	}

	protected boolean isExpert() {
		return RequestContextContainer.getRequestContext().isExpert();
	}

	protected Long getUserId() {
		return RequestContextContainer.getRequestContext().getUserId();
	}

	protected Long getAccountId() {
		return RequestContextContainer.getRequestContext().getAccountId();
	}

	protected boolean isSignedInAs(final UserType userType) {
		return isUserAuthorized() && userType.equals(RequestContextContainer.getRequestContext().signedInAs());
	}

	protected UserType signedInAs() {
		return RequestContextContainer.getRequestContext().signedInAs();
	}

	protected ModelAndView getModelAndView(final Model model, final String view) {
		if (null != model) {
			model.addAttribute("isUserAuthorized", isUserAuthorized());
			model.addAttribute("isJobseeker", isJobseeker());
			model.addAttribute("isRecruiter", isRecruiter());
			model.addAttribute("isExpert", isExpert());
			model.addAttribute("userId", getUserId());
			model.addAttribute("accountId", getAccountId());
			model.addAttribute("signedInAs", signedInAs());
		}
		return new ModelAndView(view, "command", model);
	}

	/**
	 * @param model
	 * @return
	 */
	protected ModelAndView redirectUserIfAuthorized(final Model model) {
		if (isUserAuthorized()) {
			if (isSignedInAs(UserType.EXPERT)) {
				return getModelAndView(null, "redirect:/expert/dashboard");
			} else if (isSignedInAs(UserType.RECRUITER)) {
				return getModelAndView(null, "redirect:/recruiter/dashboard");
			} else {
				return getModelAndView(null, "redirect:/jobseeker/dashboard");
			}
		}
		return null;
	}

	protected boolean isRecruiterProfileComplete(final Long userId) {

		UserBasicDataModel userBasicDataModel = userDetailService.getUserBasicDetailById(userId);
		Set<UserExperienceDataModel> userExperienceDataModels = userExperienceDetailsService
				.getExperienceDetailByUserId(userId);
		Iterator<UserExperienceDataModel> itr = userExperienceDataModels.iterator();
		UserExperienceDataModel userExperienceDataModel = null;
		if (itr.hasNext()) {
			userExperienceDataModel = itr.next();
		}
		// check
		if (null == userBasicDataModel || StringUtils.isEmpty(userBasicDataModel.getFirstName())
				|| StringUtils.isEmpty(userBasicDataModel.getProfileHeadline())
				|| StringUtils.isEmpty(userBasicDataModel.getMobileNumber())) {
			return false;
		} else if (null == userExperienceDataModel) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean isJobSeekerProfileComplete(final Long userId) {

		UserBasicDataModel userBasicDataModel = userDetailService.getUserBasicDetailById(userId);
		Set<UserEducationDataModel> userEducationDataModels = userEducationDetailsService
				.getEducationDetailByJobSeekerId(userId);
		Iterator<UserEducationDataModel> itr = userEducationDataModels.iterator();
		UserEducationDataModel userEducationDataModel = null;
		if (itr.hasNext()) {
			userEducationDataModel = itr.next();
		}
		Set<UserExperienceDataModel> userExperienceDataModels = userExperienceDetailsService
				.getExperienceDetailByUserId(userId);
		Iterator<UserExperienceDataModel> it = userExperienceDataModels.iterator();
		UserExperienceDataModel userExperienceDataModel = null;
		if (it.hasNext()) {
			userExperienceDataModel = it.next();
		}
		// check
		if (null != userExperienceDataModel) {
			if (StringUtils.isEmpty(userBasicDataModel.getJobFunction())
					|| StringUtils.isEmpty(userBasicDataModel.getIndustryName()))
				return false;
		}
		if (null == userBasicDataModel || StringUtils.isEmpty(userBasicDataModel.getFirstName())
				|| (StringUtils.isEmpty(userBasicDataModel.getSkills())
						|| StringUtils.isEmpty(userBasicDataModel.getSkills().replaceAll(",", "")))) {
			return false;
		} else if (null == userEducationDataModel) {
			return false;
		} else {
			return true;
		}
	}

	// @ExceptionHandler(Exception.class)
	// public ModelAndView handleError(HttpServletRequest req, Exception
	// exception) {
	//
	// ModelAndView mav = new ModelAndView();
	// mav.addObject("exception", exception);
	// mav.addObject("url", req.getRequestURL());
	// mav.setViewName("error/500");
	// return mav;
	// }

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception exception) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error/500");
		log.error("Some error", exception);
		return mav;
	}

	protected String replaceEndOfLineWithBreakLine(String originalText) {

		if (StringUtils.isEmpty(originalText)) {
			return StringUtils.EMPTY;
		}
		System.out.println(originalText.contains("\n"));
		return originalText.replaceAll(System.getProperty("line.separator"), "<br/>").replaceAll("\n", "<br/>");

	}

}
