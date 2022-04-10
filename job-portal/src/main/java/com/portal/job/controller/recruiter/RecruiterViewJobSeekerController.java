package com.portal.job.controller.recruiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.user.UserEducationDetailController;
import com.portal.job.helper.UserProfileHelper;
import com.portal.job.model.UserAwardDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserCertificationDataModel;
import com.portal.job.model.UserContactDataModel;
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.model.UserPatentDataModel;
import com.portal.job.model.UserProjectDataModel;
import com.portal.job.model.UserPublicationDataModel;
import com.portal.job.model.UserTestDataModel;
import com.portal.job.model.UserVolunteerDataModel;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.service.UserAwardDetailService;
import com.portal.job.service.UserCertificationDetailsService;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserEducationDetailsService;
import com.portal.job.service.UserExperienceDetailsService;
import com.portal.job.service.UserPatentDetailsService;
import com.portal.job.service.UserProjectDetailService;
import com.portal.job.service.UserPublicationDetailsService;
import com.portal.job.service.UserTestDetailService;
import com.portal.job.service.UserVolunteerDetailsService;
import com.portal.job.services.search.JobseekerSearchService;

@Controller
@RequestMapping("/user/getJobSeeker")
public class RecruiterViewJobSeekerController extends JobPortalBaseController {
	private static Logger log = LoggerFactory
			.getLogger(UserEducationDetailController.class);

	@Autowired
	private BasicAccountDetailService basicAccountDetailService;
	@Autowired
	private JobseekerSearchService jobseekerSearchService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private UserExperienceDetailsService jsExperienceDetailSrvc;
	@Autowired
	private UserEducationDetailsService jsEducationDetailSrvc;
	@Autowired
	private UserProjectDetailService userProjectDetailService;
	@Autowired
	private UserPublicationDetailsService userPublicationDetailsService;
	@Autowired
	private UserTestDetailService userTestDetailService;
	@Autowired
	private UserVolunteerDetailsService jsVolunteerDetailSrvc;
	@Autowired
	private UserCertificationDetailsService jsCertificationDetailSrvc;
	@Autowired
	private UserAwardDetailService awardDetailService;
	@Autowired
	private UserPatentDetailsService jsPatentDetailSrvc;

	private boolean isSearchForUserAllowed(String jobId, String userId) {
		try {
			// if(jobseekerSearchService.getUser(Long.parseLong(jobId),
			// Long.parseLong(userId))==null){
			// log.error("search for user is not allowed "+userId+"  jobId  "+jobId);
			// return false;
			// }
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@RequestMapping(value = "basicdetail/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody UserBasicDataModel getBasicUserDetailsForUserId(
			String jobseekerId, String jobId) {
		try {

			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			final UserBasicDataModel userBasicDataModel = userDetailService
					.getUserBasicDataModel(Long.valueOf(jobseekerId));
			userBasicDataModel
					.setSummary(replaceEndOfLineWithBreakLine(userBasicDataModel
							.getSummary()));
			return userBasicDataModel;

		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "contactdetail/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody UserContactDataModel getContactDetailsForUserId(
			String jobseekerId, String jobId) {
		try {

			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			return basicAccountDetailService.getUserContactDataModel(Long
					.valueOf(jobseekerId));
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "experience/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserExperienceDataModel> getAllExperienceDetailsForUserId(
			String jobseekerId, String jobId) {
		try {

			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserExperienceDataModel> experienceDetailsSet = jsExperienceDetailSrvc
					.getExperienceDetailByUserId(Long.parseLong(jobseekerId));

			List<UserExperienceDataModel> responseList = Lists
					.newArrayList(experienceDetailsSet);

			if (!responseList.isEmpty()) {
				UserProfileHelper.sortExperienceDetails(responseList);
			}

			return responseList;

		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "education/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserEducationDataModel> getAllEducationDetailsForUserId(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserEducationDataModel> educationDetailsList = jsEducationDetailSrvc
					.getEducationDetailByJobSeekerId(Long
							.parseLong(jobseekerId));
			final List<UserEducationDataModel> responseList = new ArrayList<UserEducationDataModel>(
					educationDetailsList);
			UserProfileHelper.sortEducationDetails(responseList);
			return responseList;
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "project/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserProjectDataModel> getAllProjectDetailsForUserId(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserProjectDataModel> projectDataSet = userProjectDetailService
					.getProjectDataModelForUserId(Long.parseLong(jobseekerId));
			return new ArrayList<UserProjectDataModel>(projectDataSet);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "publication/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserPublicationDataModel> getAllUserPublicationDataModelForUserId(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserPublicationDataModel> userPublicationDataModelList = this.userPublicationDetailsService
					.getUserPublicationDataModelByUserId(Long
							.parseLong(jobseekerId));
			return new ArrayList<UserPublicationDataModel>(
					userPublicationDataModelList);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "skill/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody String geetUserSkillDetailsJson(String jobseekerId,
			String jobId) {
		// Fetch Skills which is appends the all skills
		// by ',' and returns it.
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			return userDetailService.getSkillsForUser(Long
					.parseLong(jobseekerId));
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "test/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserTestDataModel> getAllUserTestDetailsForUser(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			return this.userTestDetailService.getTestDataModelForUserId(Long
					.parseLong(jobseekerId));
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "volunteer/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserVolunteerDataModel> getAllVolunteerDetailsForUser(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			List<UserVolunteerDataModel> volunteerDetailsList = jsVolunteerDetailSrvc
					.getVolunteerDetailByJobSeekerId(Long
							.parseLong(jobseekerId));
			return volunteerDetailsList;
		} catch (Exception e) {
			return null;
		}

	}

	@RequestMapping(value = "certification/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserCertificationDataModel> getAllCertificationDetails(
			String jobseekerId, String jobId) {
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserCertificationDataModel> certificationDetailsSet = jsCertificationDetailSrvc
					.getCertificationDetailByJobSeekerId(Long
							.parseLong(jobseekerId));
			return new ArrayList<UserCertificationDataModel>(
					certificationDetailsSet);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "award/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserAwardDataModel> getAllAwardDetails(
			String jobseekerId, String jobId) {
		log.info("zzzzzz in award details,jobseekerId & jobId are "
				+ jobseekerId + "  " + jobId);
		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserAwardDataModel> awardDetailsSet = awardDetailService
					.getAwardsByJobseekerId(Long.parseLong(jobseekerId));
			log.info("zzzzz award details = " + awardDetailsSet.toString());
			return new ArrayList<UserAwardDataModel>(awardDetailsSet);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "patent/viewByJsId", method = RequestMethod.GET)
	public @ResponseBody List<UserPatentDataModel> getAllPatentDetailsForUser(
			String jobseekerId, String jobId) {

		try {
			if (!isSearchForUserAllowed(jobId, jobseekerId))
				return null;
			Set<UserPatentDataModel> patentDetailsList = jsPatentDetailSrvc
					.getPatentDetailByJobSeekerId(Long.parseLong(jobseekerId));
			return new ArrayList<UserPatentDataModel>(patentDetailsList);
		} catch (Exception e) {
			return null;
		}
	}
}
