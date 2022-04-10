package com.portal.job.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.enums.RecruiterType;
import com.portal.job.model.JobDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;
import com.portal.job.model.SigninDataModel;
import com.portal.job.service.JobDetailService;
import com.portal.job.services.mail.JobPortalMailService;
import com.portal.job.services.task.JobTaskExecutorService;
import com.portal.job.utils.DateUtility;

/**
 * Handles requests for the application home page.
 */
/**
 * @author behinddwalls
 *
 */
@Controller
public class HomeController extends JobPortalBaseController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private JobTaskExecutorService taskExecutorService;
    @Autowired
    private JobDetailService jobDetailService;
    @Autowired
    private JobPortalMailService jobPortalMailService;

    /**
     * Simply selects the home view to render by returning its name.
     * 
     * Requests to http://localhost:8080/job... will be mapped here. Everytime
     * invoked, we pass list of all persons to view
     */
    @RequestMapping(value = { "/home", "/" }, method = RequestMethod.GET)
    public ModelAndView home(Locale locale, Model model) {

        final ModelAndView redirectUserIfAuthorized = redirectUserIfAuthorized(model);
        if (null != redirectUserIfAuthorized) {
            return redirectUserIfAuthorized;
        }

        log.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);
        model.addAttribute("isHome", true);
        model.addAttribute("jobseekerRegisterDataModel", new JobseekerRegisterDataModel());
        model.addAttribute("recruiterRegisterDataModel", new RecruiterRegisterDataModel());
        model.addAttribute("signinDataModel", new SigninDataModel());
        model.addAttribute("signupLink", "");
        model.addAttribute("recruiterTypeMap", RecruiterType.getRecruiterTypeToNameMap());

        return new ModelAndView("home", "command", model);
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public @ResponseBody String listAll(Model model) throws ParseException {

        long st = new Date().getTime();

        for (JobDetail job : jobDetailService.getJobsByRecruiterIdForRecruiterSearchInTransaction(getUserId())) {
            JobDataModel jobDataModel = new JobDataModel();
            jobDataModel.setJobId(Long.toString(job.getJobId()));
            jobDataModel.setTitle(job.getTitle());
            jobDataModel.setMaxSalary(Integer.toString(job.getMaxSalary()));
            jobDataModel.setMinSalary(Integer.toString(job.getMinSalary()));
            jobDataModel.setJobExperience(Integer.valueOf(job.getJobExperiance()));
            jobDataModel.setOrganisationName(job.getOrganisationName());
            jobDataModel.setIndustryName(job.getIndustryName());
            jobDataModel.setJobFunction(job.getJobFunction());
            jobDataModel.setJobStatus(job.getJobStatus());
            jobDataModel.setLocation(job.getLocationDetail());
            jobDataModel.setSkills("Spring,C,C++");
            jobDataModel.setRecruiterId(getUserId().toString());
            jobDataModel.setJobCreatedDate(DateUtility.getStringFromDate(job.getCreateDate(),
                    DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
            jobDataModel.setJobExpiaryDate(DateUtility.getStringFromDate(job.getExpireDate(),
                    DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
            jobDataModel.setSalaryCurrencyCode(job.getSalaryCurrencyCode());

            taskExecutorService.computeCandidatesForPostedJob(jobDataModel);
        }
        System.out.println("TOTAL TIME" + (new Date().getTime() - st));

        return "true";
    }

    @RequestMapping("/compute")
    public @ResponseBody String computeJobs() {
        return "";
    }

    @RequestMapping("/query")
    public @ResponseBody Iterator<Object> queryJobs() {
        return Lists.newArrayList().iterator();
    }

    @RequestMapping("/mail")
    public @ResponseBody String mailJobs() {
        // try {
        // final MandrillSendMailInput input = new MandrillSendMailInput();
        // input.setFromName("JobXR");
        // input.setFromEmailId("noreply@jobxr.com");
        // input.setSubject("Welcome to JobXR !!!");
        // input.setHtmlMessage("<h1> JobXR </h1><br/> <p> Test Mail </p>"
        // + "" + "<p> <br/>Thanks<br/>" + "behinddwalls </p>");
        // final List<String> emailid = Lists.newArrayList();
        // emailid.add("behinddwalls@gmail.com");
        // emailid.add("tanwani.shreshth@gmail.com");
        // emailid.add("thussu@gmail.com");
        // emailid.add("innocentboy1386@gmail.com");
        // emailid.add("akhilbhasin25@gmail.com");
        // emailid.add("tomarshubham24@gmail.com");
        // input.setToEmailIds(emailid);
        //
        // jobPortalMailService.sendMail(input);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return "";
    }

    @RequestMapping(value = { "about" }, method = RequestMethod.GET)
    public ModelAndView aboutUs(Model model) {
        return getModelAndView(model, "about/us");
    }

    @RequestMapping(value = { "about/engine" }, method = RequestMethod.GET)
    public ModelAndView aboutEngine(Model model) {
        return getModelAndView(model, "about/engine");
    }

    @RequestMapping(value = { "about/tnc" }, method = RequestMethod.GET)
    public ModelAndView aboutTnC(Model model) {
        return getModelAndView(model, "about/tnc");
    }

    @RequestMapping(value = { "about/privacy" }, method = RequestMethod.GET)
    public ModelAndView aboutPrivacy(Model model) {
        return getModelAndView(model, "about/privacy");
    }

}
