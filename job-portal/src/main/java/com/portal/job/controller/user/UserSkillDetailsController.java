package com.portal.job.controller.user;

import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portal.job.constants.SessionConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.mapper.UserSkillDetailMapper;
import com.portal.job.service.SkillDetailService;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.task.CandidateTaskExecutorService;

//TODO -Need to check every redirection.

@Controller
@RequestMapping("/jobseeker/skill")
public class UserSkillDetailsController  extends JobPortalBaseController{
	private static final Logger log = LoggerFactory
			.getLogger(UserSkillDetailsController.class);
	
	@Autowired
	private UserSkillDetailMapper UserSkillDetailMapper;
	@Autowired
	private SkillDetailService skillDetailService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
    private CandidateTaskExecutorService candidateExecutorService;
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody String saveAllSkillDetailsForUser(
			String skills,Model model,HttpSession httpSession)
			throws ParseException {
		log.info("QQQQQQQQQ skills = "+skills);		
		userDetailService.addOrUpdateUserSkillDetails(getUserId(), skills);
		if (isJobSeekerProfileComplete(getUserId())) {
	        httpSession.setAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, true);
	    } else {
	        httpSession.setAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, false);
	    }
		if(httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE)!=null&& (Boolean)httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE))
		{
			candidateExecutorService.computeJobsForCandidate(userDetailService.getUserDataModelByIdInTransaction(getUserId()));
		}
		return skills;
	}		
	
	@RequestMapping("viewAll")
	public @ResponseBody String geetUserSkillDetailsJson(
			HttpSession httpSession, Model model)
			throws ParseException {		
		//Fetch Skills which is appends the all skills 
		//by ',' and returns it.
		return userDetailService.getSkillsForUser(getUserId());		
	}	
}
