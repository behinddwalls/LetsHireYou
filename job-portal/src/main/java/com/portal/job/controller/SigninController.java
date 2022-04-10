package com.portal.job.controller;

import static com.portal.job.constants.SessionConstants.ACCOUNT;
import static com.portal.job.constants.SessionConstants.EX_SIGNED_IN;
import static com.portal.job.constants.SessionConstants.IS_RECRUITER_PROFILE_COMPLETE;
import static com.portal.job.constants.SessionConstants.JS_SIGNED_IN;
import static com.portal.job.constants.SessionConstants.RC_SIGNED_IN;
import static com.portal.job.constants.SessionConstants.USER_DATA;

import java.net.URLDecoder;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.constants.JobPortalConstants;
import com.portal.job.constants.SessionConstants;
import com.portal.job.enums.EmailType;
import com.portal.job.enums.UserType;
import com.portal.job.handler.GoogleRecaptchaHandler;
import com.portal.job.helper.AccountVerificationHelper;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.SigninDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.utils.KeyGeneratorUtility;
import com.portal.job.validator.SigninDataModelValidator;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/signin")
public class SigninController extends JobPortalBaseController {

    private static final Logger log = LoggerFactory.getLogger(SigninController.class);

    private final static String SIGN_UP_LINK = "signupLink";
    private final static String REGISTER_BASE_PATH = "/register/";
    private final static String GOOGLE_RECAPTCHA_RESPONSE = "g-recaptcha-response";
    private final static String VERIFY = "verify";
    private final static String ACCOUNT_ID = "accountId";
    private final static String KEY = "key";

    @Autowired
    private BasicAccountDetailService accountDetailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private GoogleRecaptchaHandler googleRecaptchaHandler;

    @Autowired
    private SigninDataModelValidator signinDataModelValidator;

    @InitBinder("signinDataModel")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(signinDataModelValidator);
    }

    @RequestMapping(value = "jobseeker", method = RequestMethod.POST)
    public ModelAndView jobseekerSignin(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @ModelAttribute("signinDataModel") @Validated final SigninDataModel signinDataModel,
            BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {

        if (isSignedInAs(UserType.JOBSEEKER)) {
            // return getModelAndView(null, "redirect:/jobseeker/profile/view");
            return getModelAndView(null, "redirect:/jobseeker/dashboard");
        }
        return handleSignin(signinDataModel, bindingResult, model, httpServletRequest, UserType.JOBSEEKER,
                gRecaptchaResponse);
    }

    @RequestMapping(value = "recruiter", method = RequestMethod.POST)
    public ModelAndView recruiterSignin(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @ModelAttribute("signinDataModel") @Validated final SigninDataModel signinDataModel,
            BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {

        if (isSignedInAs(UserType.RECRUITER)) {
            return getModelAndView(null, "redirect:/recruiter/dashboard");
        }
        return handleSignin(signinDataModel, bindingResult, model, httpServletRequest, UserType.RECRUITER,
                gRecaptchaResponse);
    }

    @RequestMapping(value = "expert", method = RequestMethod.POST)
    public ModelAndView expertSignin(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @ModelAttribute("signinDataModel") @Validated final SigninDataModel signinDataModel,
            BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {

        if (isSignedInAs(UserType.EXPERT)) {
            return getModelAndView(null, "expert/dashboard");
        }
        return handleSignin(signinDataModel, bindingResult, model, httpServletRequest, UserType.EXPERT,
                gRecaptchaResponse);
    }

    private ModelAndView handleSignin(final SigninDataModel signinDataModel, BindingResult bindingResult, Model model,
            HttpServletRequest httpServletRequest, final UserType userType, final String gRecaptchaResponse) {

        boolean isRecaptchaValid = googleRecaptchaHandler.isRecaptchaValid(gRecaptchaResponse);
        // validate
        if (!bindingResult.hasErrors() && isRecaptchaValid) {

            final BasicAccountDataModel accountDataModel = this.accountDetailService
                    .getAccountDataModelByEmailIdAndUserType(signinDataModel.getEmailId(), userType);
            System.out.println(accountDataModel);
            if (null != accountDataModel
                    && bCryptPasswordEncoder.matches(signinDataModel.getPassword(), accountDataModel.getPasswordHash())) {

                final UserBasicDataModel userBasicDataModel = this.userDetailService
                        .getUserBasicDataModelByAccountId(accountDataModel.getAccountId());
                final HttpSession httpSession = httpServletRequest.getSession();
                httpSession.setAttribute(ACCOUNT, accountDataModel);
                httpSession.setAttribute(USER_DATA, userBasicDataModel);
                httpSession.setMaxInactiveInterval(-1);

                if (UserType.JOBSEEKER.equals(userType)) {
                    httpSession.setAttribute(JS_SIGNED_IN, true);

                    if (accountDataModel.isJobseeker()) {
                        httpSession.setAttribute(SessionConstants.JS_ACCOUNT_VERIFIED, true);
                    }
                    if (accountDataModel.isEmailIdVerified()) {
                        httpSession.setAttribute(SessionConstants.EMAIL_ID_VERIFIED, true);
                    }
                    if (isJobSeekerProfileComplete(userBasicDataModel.getUserId())) {
                        httpSession.setAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE, true);
                    }
                    return getModelAndView(null, "redirect:/jobseeker/dashboard");

                } else if (UserType.RECRUITER.equals(userType)) {
                    httpSession.setAttribute(RC_SIGNED_IN, true);
                    if (accountDataModel.isRecruiter()) {
                        httpSession.setAttribute(SessionConstants.RC_ACCOUNT_VERIFIED, true);
                    }
                    if (accountDataModel.isWorkEmailIdVerified()) {
                        httpSession.setAttribute(SessionConstants.WORK_EMAIL_ID_VERIFIED, true);
                    }

                    if (isRecruiterProfileComplete(userBasicDataModel.getUserId())) {
                        httpSession.setAttribute(IS_RECRUITER_PROFILE_COMPLETE, true);
                    }
                    return getModelAndView(null, "redirect:/recruiter/dashboard");

                } else if (UserType.EXPERT.equals(userType)) {
                    httpSession.setAttribute(EX_SIGNED_IN, true);
                    if (accountDataModel.isExpert()) {
                        httpSession.setAttribute(SessionConstants.EX_ACCOUNT_VERIFIED, true);
                    }
                    if (accountDataModel.isEmailIdVerified()) {
                        httpSession.setAttribute(SessionConstants.EMAIL_ID_VERIFIED, true);
                    }
                    return getModelAndView(null, "redirect:/expert/dashboard");
                }
            }
        }
        if (!isRecaptchaValid) {
            model.addAttribute("errorMessage", "reCaptcha Validation Failed.");
        } else {
            model.addAttribute("errorMessage", "Invalid Email/Password provided.");
        }
        model.addAttribute(bindingResult.getModel());
        model.addAttribute(SIGN_UP_LINK, REGISTER_BASE_PATH + userType.getUserTypeName());
        return getModelAndView(model, userType.getUserTypeName() + "/signin");

    }

    @RequestMapping("/jobseeker/forgot-password")
    public ModelAndView jobSeekerForgotPasswordLandingPage(final Model model) {

        if (isSignedInAs(UserType.JOBSEEKER)) {
            return getModelAndView(null, "redirect:/jobseeker/dashboard");
        }
        model.addAttribute("signinDataModel", new SigninDataModel());
        return new ModelAndView("jobseeker/forgot-password", "command", model);
    }

    @RequestMapping(value = "/jobseeker/forgot-password", method = RequestMethod.POST)
    public ModelAndView jobSeekerForgotPasswordSendMailPage(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @ModelAttribute("signinDataModel") @Validated final SigninDataModel signinDataModel,
            final BindingResult bindingResult, final Model model) {
        try {
            if (isSignedInAs(UserType.JOBSEEKER)) {
                return getModelAndView(null, "redirect:/jobseeker/dashboard");
            }
            boolean isRecaptchaValid = googleRecaptchaHandler.isRecaptchaValid(gRecaptchaResponse);

            if (null == bindingResult.getFieldError("emailId") && isRecaptchaValid) {
                BasicAccountDataModel accountDataModel = accountDetailService.getAccountDataModelByEmailIdAndUserType(
                        signinDataModel.getEmailId(), UserType.JOBSEEKER);
                if (null != accountDataModel && accountDataModel.getEmailId().equals(signinDataModel.getEmailId())) {
                    accountDataModel.setForgotPasswordKey(KeyGeneratorUtility.generateKey());
                    // update account with forgot key
                    accountDataModel = accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    // send mail for password reset
                    accountDetailService.sendForgotPasswordResetLink(accountDataModel.getEmailId(), UserType.JOBSEEKER,
                            accountDataModel.getAccountId(), accountDataModel.getForgotPasswordKey());

                    model.addAttribute("successMessage", "Please check your mail inbox");
                } else {
                    model.addAttribute("errorMessage", "Entered Email is not correct.");
                }
            }
            if (!isRecaptchaValid) {
                model.addAttribute("errorMessage", "Please verify reCaptcha.");

            }
            model.addAttribute("signinDataModel", signinDataModel);
            model.addAttribute(bindingResult.getModel());

        } catch (Exception e) {
            log.error("Failed: ", e);
            model.addAttribute("errorMessage", "Something went wrong while processing your request.");
        }
        return new ModelAndView("jobseeker/forgot-password", "command", model);
    }

    @RequestMapping(value = "forgot-password/reset")
    public ModelAndView verifyJobseekerForgotPasswordResetKeyLandingPage(
            @RequestParam(value = VERIFY) final Integer verify, @RequestParam(value = ACCOUNT_ID) final Long accountId,
            @RequestParam(value = KEY) final String key, final Model model) {
        try {

            final String decodedKey = URLDecoder.decode(key, JobPortalConstants.UTF_8);

            BasicAccountDataModel basicAccountDataModel = accountDetailService
                    .getAccountDataModelByAccountIdAndForgotPasswordKeyInTransaction(accountId, decodedKey);
            if (null == basicAccountDataModel || basicAccountDataModel.getAccountId() == null) {
                model.addAttribute("errorMessage", "Failed to verify the key.");
            }
            model.addAttribute(KEY, key);
            model.addAttribute(VERIFY, verify);
            model.addAttribute(ACCOUNT_ID, accountId);
        } catch (Exception e) {
            log.error("verifyJobseekerForgotPasswordResetKeyLandingPage Failed: ", e);
            model.addAttribute("errorMessage", "Something went wrong while processing your request.");
        }
        model.addAttribute("signinDataModel", new SigninDataModel());
        return new ModelAndView("user/reset-password", "command", model);
    }

    @RequestMapping(value = "forgot-password/reset", method = RequestMethod.POST)
    public ModelAndView verifyJobseekerForgotPasswordResetKeyPage(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @RequestParam(value = VERIFY) final Integer verify, @RequestParam(value = ACCOUNT_ID) final Long accountId,
            @RequestParam(value = KEY) final String key,
            @ModelAttribute("signinDataModel") final SigninDataModel signinDataModel,
            final BindingResult bindingResult, final Model model) {
        try {
            boolean isRecaptchaValid = googleRecaptchaHandler.isRecaptchaValid(gRecaptchaResponse);

            if (null == bindingResult.getFieldError("password") && isRecaptchaValid) {

                final String decodedKey = URLDecoder.decode(key, JobPortalConstants.UTF_8);

                BasicAccountDataModel basicAccountDataModel = accountDetailService
                        .getAccountDataModelByAccountIdAndForgotPasswordKeyInTransaction(accountId, decodedKey);
                if (null == basicAccountDataModel || basicAccountDataModel.getAccountId() == null) {
                    model.addAttribute("errorMessage", "Failed to verify the key.");
                } else {
                    basicAccountDataModel.setPasswordHash(bCryptPasswordEncoder.encode(signinDataModel.getPassword()));
                    accountDetailService.addOrUpdateAccountDetail(basicAccountDataModel);
                    model.addAttribute("successMessage", "Reset password completed successfully.");
                }
            }
            if (!isRecaptchaValid) {
                model.addAttribute("errorMessage", "Please verify reCaptcha.");
            }
        } catch (Exception e) {
            log.error("verifyJobseekerForgotPasswordResetKeyPage Failed: ", e);
            model.addAttribute("errorMessage", "Something went wrong while processing your request.");
        }

        model.addAllAttributes(bindingResult.getModel());
        return new ModelAndView("user/reset-password", "command", model);
    }

    @RequestMapping("/recruiter/forgot-password")
    public ModelAndView recruiterForgotPasswordLandingPage(final Model model) {

        if (isSignedInAs(UserType.RECRUITER)) {
            return getModelAndView(null, "redirect:/recruiter/dashboard");
        }
        model.addAttribute("signinDataModel", new SigninDataModel());
        return new ModelAndView("recruiter/forgot-password", "command", model);
    }

    @RequestMapping(value = "/recruiter/forgot-password", method = RequestMethod.POST)
    public ModelAndView recruiterForgotPasswordSendMailPage(
            @RequestParam(value = GOOGLE_RECAPTCHA_RESPONSE, required = false) final String gRecaptchaResponse,
            @ModelAttribute("signinDataModel") @Validated final SigninDataModel signinDataModel,
            final BindingResult bindingResult, final Model model) {
        try {
            if (isSignedInAs(UserType.RECRUITER)) {
                return getModelAndView(null, "redirect:/recruiter/dashboard");
            }
            boolean isRecaptchaValid = googleRecaptchaHandler.isRecaptchaValid(gRecaptchaResponse);

            if (null == bindingResult.getFieldError("emailId") && isRecaptchaValid) {
                BasicAccountDataModel accountDataModel = accountDetailService.getAccountDataModelByEmailIdAndUserType(
                        signinDataModel.getEmailId(), UserType.RECRUITER);
                if (null != accountDataModel && accountDataModel.getWorkEmailId().equals(signinDataModel.getEmailId())) {
                    accountDataModel.setForgotPasswordKey(KeyGeneratorUtility.generateKey());
                    // update account with forgot key
                    accountDataModel = accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    // send mail for password reset
                    accountDetailService.sendForgotPasswordResetLink(accountDataModel.getWorkEmailId(),
                            UserType.RECRUITER, accountDataModel.getAccountId(),
                            accountDataModel.getForgotPasswordKey());

                    model.addAttribute("successMessage", "Please check your mail inbox");
                } else {
                    model.addAttribute("errorMessage", "Entered Email is not correct.");
                }
            }
            if (!isRecaptchaValid) {
                model.addAttribute("errorMessage", "Please verify reCaptcha.");

            }
            model.addAttribute("signinDataModel", signinDataModel);
            model.addAttribute(bindingResult.getModel());
            System.out.println(bindingResult.getModel());
        } catch (Exception e) {
            log.error("Failed: ", e);
            model.addAttribute("errorMessage", "Something went wrong while processing your request.");
        }
        return new ModelAndView("recruiter/forgot-password", "command", model);
    }

    @RequestMapping(value = "email/verify")
    public ModelAndView verifyRecruiterNewEmailId(@RequestParam final String emailType,
            @RequestParam final String verificationKey, Model model, final HttpServletRequest httpRequest) {
        try {
            if (Optional.ofNullable(verificationKey).isPresent() && Optional.ofNullable(emailType).isPresent()) {
                BasicAccountDataModel accountDataModel = AccountVerificationHelper
                        .getBasicAccountDataModelForMailVerification(getAccountId(), verificationKey,
                                EmailType.valueOf(emailType), accountDetailService);

                if (Optional.ofNullable(accountDataModel).isPresent()) {
                    if (EmailType.EMAIL.name().equals(emailType)) {
                        accountDataModel.setEmailIdVerified(true);
                        accountDataModel.setEmailIdVerificationKey(null);
                        accountDataModel.setEmailId(accountDataModel.getNewUnverifiedEmailId());
                        accountDataModel.setNewUnverifiedEmailId(null);
                    } else if (EmailType.WORK_EMAIL.name().equals(emailType)) {
                        accountDataModel.setWorkEmailIdVerified(true);
                        accountDataModel.setWorkEmailIdVerificationKey(null);
                        accountDataModel.setWorkEmailId(accountDataModel.getNewUnverfiedWorkEmailId());
                        accountDataModel.setNewUnverfiedWorkEmailId(null);
                    } else {
                        throw new Exception("Malformed email Type");
                    }
                    accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    final HttpSession session = httpRequest.getSession(false);
                    if (null != session) {
                        session.invalidate();
                    }
                    model.addAttribute("successMessage",
                            "Email Verified Successfully. Please signin again to use new email.");
                } else {
                    model.addAttribute("errorMessage", "Malformed or invalid verification key: " + verificationKey);
                }

            } else {
                model.addAttribute("verifyMessage", "Please verify your email. Please check your email (inbox/spam).");
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            model.addAttribute("errorMessage", "Malformed or invalid verification key");
        }
        return new ModelAndView("user/verify-email", "command", model);
    }

    @RequestMapping(value = "account/verify/recruiter")
    public ModelAndView verifyRecruiterAccount(@RequestParam(required = false) final Long accountId,
            @RequestParam(required = false) final String verificationKey, Model model,
            final HttpServletRequest httpRequest) {
        try {
            if (Optional.ofNullable(verificationKey).isPresent() && Optional.ofNullable(accountId).isPresent()) {
                BasicAccountDataModel accountDataModel = AccountVerificationHelper
                        .getBasicAccountDataModelForAccountVerification(accountId, verificationKey,
                                accountDetailService, UserType.RECRUITER);
                System.out.println("Record " + accountDataModel);
                if (Optional.ofNullable(accountDataModel).isPresent()) {
                    accountDataModel.setRecruiterVerificationKey(null);
                    accountDataModel.setRecruiter(true);
                    accountDataModel.setWorkEmailIdVerified(true);
                    accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    final HttpSession session = httpRequest.getSession();
                    session.invalidate();
                    model.addAttribute("successMessage",
                            "Account Verified Successfully. Please go to Recruiter sign in page to start using EvenRank.");
                } else {
                    model.addAttribute("errorMessage", "Malformed or invalid verification key: " + verificationKey);
                }

            } else {
                model.addAttribute("verifyMessage", "Please verify your account. Please check your email (inbox/spam).");
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            model.addAttribute("errorMessage", "Malformed or invalid verification key");
        }
        return new ModelAndView("user/verify-account", "command", model);
    }

    @RequestMapping(value = "account/verify/jobseeker")
    public ModelAndView verifyJobSeekerAccount(@RequestParam(required = false) final Long accountId,
            @RequestParam(required = false) final String verificationKey, Model model,
            final HttpServletRequest httpRequest) {
        try {
            if (Optional.ofNullable(verificationKey).isPresent() && Optional.ofNullable(accountId).isPresent()) {
                BasicAccountDataModel accountDataModel = AccountVerificationHelper
                        .getBasicAccountDataModelForAccountVerification(accountId, verificationKey,
                                accountDetailService, UserType.JOBSEEKER);

                if (Optional.ofNullable(accountDataModel).isPresent()) {
                    accountDataModel.setJobseekerVerificationKey(null);
                    accountDataModel.setJobseeker(true);
                    accountDataModel.setEmailIdVerified(true);
                    accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    final HttpSession session = httpRequest.getSession();
                    session.invalidate();
                    model.addAttribute("successMessage",
                            "Account Verified Successfully. Please go to Jobseeker sign in page to start using EvenRank.");
                } else {
                    model.addAttribute("errorMessage", "Malformed or invalid verification key: " + verificationKey);
                }

            } else {
                model.addAttribute("verifyMessage", "Please verify your account. Please check your email (inbox/spam).");
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            model.addAttribute("errorMessage", "Malformed or invalid verification key");
        }
        return new ModelAndView("user/verify-account", "command", model);
    }

    @RequestMapping(value = { "{signInAs}" })
    public ModelAndView signinPage(@PathVariable final String signInAs, Model model,
            HttpServletRequest httpServletRequest) {

        if (!isUserAuthorized()) {
            final HttpSession httpSession = httpServletRequest.getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }

            model.addAttribute("signinDataModel", new SigninDataModel());
            if (UserType.EXPERT.getUserTypeName().equals(signInAs)) {

                model.addAttribute(SIGN_UP_LINK, REGISTER_BASE_PATH + UserType.EXPERT.getUserTypeName());
                return getModelAndView(model, "expert/signin");

            } else if (UserType.RECRUITER.getUserTypeName().equals(signInAs)) {

                model.addAttribute(SIGN_UP_LINK, REGISTER_BASE_PATH + UserType.RECRUITER.getUserTypeName());
                return getModelAndView(model, "recruiter/signin");

            } else {

                model.addAttribute(SIGN_UP_LINK, REGISTER_BASE_PATH + UserType.JOBSEEKER.getUserTypeName());
                return getModelAndView(model, "jobseeker/signin");
            }
        } else {
            return redirectUserIfAuthorized(model);
        }
    }

}
