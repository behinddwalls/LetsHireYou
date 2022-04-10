package com.portal.job.resumeparsing.xmlparsing.parser;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.dao.model.UserCertificationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserEducationDetail;
import com.portal.job.dao.model.UserExperinceDetail;
import com.portal.job.dao.model.UserPatentDetail;
import com.portal.job.dao.model.UserPublicationDetail;
import com.portal.job.dao.model.UserVolunteerDetail;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.service.UserCertificationDetailsService;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserEducationDetailsService;
import com.portal.job.service.UserExperienceDetailsService;
import com.portal.job.service.UserPatentDetailsService;
import com.portal.job.service.UserPublicationDetailsService;
import com.portal.job.service.UserVolunteerDetailsService;

/**
 * we could have done all this in the service only, i wanted to allow saving of
 * other details even if one fails an we should not catch exception in
 * 
 * @transactional, only transactional methods can call transactional in one
 *                 class, you cannot call transactional from a non transactional
 *                 method in the same class.
 * 
 * @author abbhasin
 *
 */
@Component
public class SaveResumeHandler {

	private static final String fakePass = "test123";
	@Autowired
	private UserExperienceDetailsService experienceDetailsService;
	@Autowired
	private UserEducationDetailsService educationDetailsService;
	@Autowired
	private UserCertificationDetailsService certificationDetailsService;
	@Autowired
	private UserVolunteerDetailsService volunteerDetailsService;
	@Autowired
	private UserPatentDetailsService userPatentDetailsService;
	@Autowired
	private UserPublicationDetailsService userPublicationDetailsService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private BasicAccountDetailService accountDetailService;

	private static Logger log = LoggerFactory
			.getLogger(SaveResumeHandler.class);

	public void saveResumeToDbAndCreateNewUser(SovrenResumeWrapper resume) throws Exception {
		try {
			Optional<UserDetail> userDetailOpt = resume.getUserBasicDetails();
			if (userDetailOpt.isPresent()) {
				UserDetail user = accountDetailService
						.registerNewUser(userDetailOpt.get());
				saveResumeToDB(resume, user.getUserId(), user
						.getBasicAccountDetail().getAccountId());
			} else
				throw new Exception("userDetail not present for resume");
		} catch (Exception e) {
			log.error("Unable to save User Basic Details ", e);
			throw e;
		}
	}

	public void saveResumeToDbForUser(SovrenResumeWrapper resume, Long userId,
			Long accountId) {
		try {
			Optional<UserDetail> userDetailOpt = resume.getUserBasicDetails();
			if (userDetailOpt.isPresent()) {
				UserDetail userDetail = userDetailOpt.get();
				BasicAccountDetail accountDetail = new BasicAccountDetail();
				accountDetail.setAccountId(accountId);
				userDetail.setUserId(userId);
				userDetail.setBasicAccountDetail(accountDetail);
				userDetailService.saveUserDetailsFromResumeParser(userDetail);
			}
		} catch (Exception e) {
			log.error("unable to save user detail for userId " + userId, e);
		}
		saveResumeToDB(resume, userId, accountId);
	}

	private void saveResumeToDB(SovrenResumeWrapper resume, Long userId,
			Long accountId) {
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		try {
			List<UserCertificationDetail> certificationDetails = resume
					.getUserCertificationDetails();
			certificationDetails.stream().forEach(
					c -> c.setUserDetail(userDetail));

			if (!certificationDetails.isEmpty()) {
				log.error("zzzzzz userDeetail : "
						+ certificationDetails.toString());
				certificationDetailsService
						.saveUserCertificationDetails(certificationDetails);
			}

		} catch (Exception e) {
			log.error("unable to save any certification ", e);
		}

		try {
			List<UserPatentDetail> patentDetails = resume
					.getUserPatentDetails();
			patentDetails.stream().forEach(p -> p.setUserDetail(userDetail));
			if (!patentDetails.isEmpty()) {
				log.error("zzzzzz userDeetail : " + patentDetails.toString());
				userPatentDetailsService.saveUserPatentDetails(patentDetails);
			}

		} catch (Exception e) {
			log.error("unable to save User Patents", e);
		}

		try {
			List<UserPublicationDetail> publicationDetails = resume
					.getUserPublicationDetails();
			publicationDetails.stream().forEach(
					p -> p.setUserDetail(userDetail));
			if (!publicationDetails.isEmpty()) {
				log.error("zzzzzz userDeetail : "
						+ publicationDetails.toString());
				userPublicationDetailsService
						.saveUserPublicationDetails(publicationDetails);
			}

		} catch (Exception e) {
			log.error("unable to save User Publications ", e);
		}

		try {
			List<UserVolunteerDetail> volunteerDetails = resume
					.getUserVolunteerDetails();
			volunteerDetails.stream().forEach(v -> v.setUserDetail(userDetail));
			if (!volunteerDetails.isEmpty()) {
				log.error("zzzzzz userDeetail : " + volunteerDetails.toString());
				volunteerDetailsService
						.saveUserVolunteerDetails(volunteerDetails);
			}

		} catch (Exception e) {
			log.error("unable to save any volunteer detail " + e);
		}

		try {
			List<UserExperinceDetail> experienceDetails = resume
					.getUserExperinceDetails();
			experienceDetails.stream()
					.forEach(e -> e.setUserDetail(userDetail));
			if (!experienceDetails.isEmpty()) {
				log.error("zzzzzz userDeetail : "
						+ experienceDetails.toString());
				userDetailService.saveUserExperienceMonthAndExperienceDetails(
						userId, experienceDetails);
			}

		} catch (Exception e) {
			log.error("unable to save User Experiences", e);
		}

		try {
			List<UserEducationDetail> educationDetails = resume
					.getUserEducationDetails();
			educationDetails.stream().forEach(e -> e.setUserDetail(userDetail));
			if (!educationDetails.isEmpty())
				userDetailService.saveUserInstituteTierAndEducationDetails(
						userId, educationDetails);
		} catch (Exception e) {
			log.error("unable to save User Education Details", e);
		}

	}
}
