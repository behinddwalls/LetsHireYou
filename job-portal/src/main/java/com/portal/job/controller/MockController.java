package com.portal.job.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.job.context.RequestContext;
import com.portal.job.context.RequestContextContainer;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.UserType;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.resumeparsing.xmlparsing.parser.JAXBParser;
import com.portal.job.resumeparsing.xmlparsing.parser.SaveResumeHandler;
import com.portal.job.resumeparsing.xmlparsing.parser.SovrenResumeWrapper;
import com.portal.job.service.BasicAccountDetailService;

@Controller
public class MockController extends JobPortalBaseController {

	private static Logger log = LoggerFactory.getLogger(MockController.class);
	@Autowired
	private SaveResumeHandler saveResumeHandler;
	@Autowired
	private JAXBParser resumeParser;
	@Autowired
	BasicAccountDetailService accountDetailService;

	private JobseekerRegisterDataModel getMockJobseekerDataModel() {
		JobseekerRegisterDataModel data = new JobseekerRegisterDataModel();
		data.setEmailId(UUID.randomUUID().toString() + "@gmail.com");
		data.setFirstName(UUID.randomUUID().toString());
		data.setLastName(UUID.randomUUID().toString());
		data.setPassword(UUID.randomUUID().toString());
		data.setUserType(UserType.JOBSEEKER.getUserTypeName());
		data.setVerifyPassword(UUID.randomUUID().toString());
		return data;
	}

	private RequestContext getRequestContext(UserDetail user) {
		RequestContext context = new RequestContext.Builder()
				.signedInAs(UserType.JOBSEEKER)
				.accountId(user.getBasicAccountDetail().getAccountId())
				.userId(user.getUserId()).build();
		return context;

	}

	private static List<String> skills = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("java");
			add("django");
			add("perl");
			add("python");
			add("ruby");
			add("scala");
			add("c");
			add("c++");
		}

	};

	@RequestMapping("/jobseeker/mockCreateUsers")
	public void CreateJobseekers() throws Exception {
		RequestContext originalRequestContext = RequestContextContainer
				.getRequestContext();
		for (int i = 1; i < 40; i++) {
			UserDetail user = accountDetailService
					.register(getMockJobseekerDataModel());

			RequestContextContainer.setRequestContext(getRequestContext(user));
			log.error("zzzzzzzzz userDEtail " + user + " \n requestContext : "
					+ RequestContextContainer.getRequestContext().toString());

			Thread.sleep(100);
			SovrenResumeWrapper resumeWrapper = new SovrenResumeWrapper(
					resumeParser.parse("xmlFiles/" + i + ".xml"));
			saveResumeHandler.saveResumeToDbForUser(resumeWrapper, user
					.getUserId(), user.getBasicAccountDetail().getAccountId());
			Thread.sleep(100);
		}
		RequestContextContainer.setRequestContext(originalRequestContext);
	}

}
