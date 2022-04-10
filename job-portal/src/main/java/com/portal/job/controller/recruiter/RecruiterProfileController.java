package com.portal.job.controller.recruiter;

import java.text.ParseException;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.constants.SessionConstants;
import com.portal.job.controller.JobPortalBaseController;
import com.portal.job.controller.RegisterController;
import com.portal.job.enums.UserType;
import com.portal.job.helper.AccountVerificationHelper;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.RecruiterProfileDataModel;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.service.BasicAccountDetailService;
import com.portal.job.service.UserDetailService;
import com.portal.job.service.UserExperienceDetailsService;
import com.portal.job.validator.RecruiterProfileDataModelValidator;

/**
 * @author behinddwalls
 *
 */
@Controller
@RequestMapping("/recruiter")
public class RecruiterProfileController extends JobPortalBaseController {

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserExperienceDetailsService userExperienceDetailsService;
    @Autowired
    private BasicAccountDetailService accountDetailService;
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RecruiterProfileDataModelValidator recruiterProfileDataModelValidator;

    @InitBinder("recruiterProfileDataModel")
    private void initBinderRecruiter(WebDataBinder binder) {
        binder.setValidator(recruiterProfileDataModelValidator);
    }

    @RequestMapping(value = "profile", method = RequestMethod.POST)
    public ModelAndView updateProfile(
            @ModelAttribute("recruiterProfileDataModel") @Validated final RecruiterProfileDataModel recruiterProfileDataModel,
            final BindingResult bindingResult, final Model model, final HttpServletRequest httpServletRequest)
            throws ParseException {

        final HttpSession httpSession = httpServletRequest.getSession(false);

        if (!bindingResult.hasErrors()) {
            try {
                userDetailService.updateRecruiterProfile(recruiterProfileDataModel, getUserId(), getAccountId());
                model.addAttribute("successMessage", "Profile updated successfully");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Profile update failed.");
            }
        }
        model.addAttribute("recruiterProfileDataModel", recruiterProfileDataModel);
        model.addAllAttributes(bindingResult.getModel());

        if (isRecruiterProfileComplete(getUserId())) {
            httpSession.setAttribute(SessionConstants.IS_RECRUITER_PROFILE_COMPLETE, true);
        } else {
            httpSession.setAttribute(SessionConstants.IS_RECRUITER_PROFILE_COMPLETE, false);
        }

        return getModelAndView(model, UserType.RECRUITER.getUserTypeName() + "/edit-profile");
    }

    @RequestMapping("profile")
    public ModelAndView getProfile(@RequestParam(value = "incomplete", required = false) final Integer incomplete,
            final Model model) {
        final RecruiterProfileDataModel recruiterProfileDataModel = new RecruiterProfileDataModel();

        final UserBasicDataModel userBasicDataModel = userDetailService.getUserBasicDetailById(getUserId());

        final Set<UserExperienceDataModel> userExperienceDataModels = userExperienceDetailsService
                .getExperienceDetailByUserId(getUserId());

        if (null != userExperienceDataModels && !userExperienceDataModels.isEmpty()) {
            recruiterProfileDataModel.setUserExperienceDataModel(userExperienceDataModels.iterator().next());
        }

        recruiterProfileDataModel.setUserBasicDataModel(userBasicDataModel);
        model.addAttribute("recruiterProfileDataModel", recruiterProfileDataModel);
        if (incomplete != null && incomplete == 1) {
            model.addAttribute("incomplete", "Please complete your full recruiter profile to get started.");
        }

        return getModelAndView(model, UserType.RECRUITER.getUserTypeName() + "/edit-profile");
    }

    @Deprecated
    @RequestMapping(value = "account/verify")
    public ModelAndView verifyRecruiterAccount(@RequestParam(required = false) final Long accountId,
            @RequestParam(required = false) final String verificationKey, Model model,
            final HttpServletRequest httpRequest) {
        try {
            if (Optional.ofNullable(verificationKey).isPresent() && Optional.ofNullable(accountId).isPresent()) {
                BasicAccountDataModel accountDataModel = AccountVerificationHelper
                        .getBasicAccountDataModelForAccountVerification(accountId, verificationKey,
                                accountDetailService, signedInAs());
                System.out.println("Record " + accountDataModel);
                if (Optional.ofNullable(accountDataModel).isPresent()) {
                    accountDataModel.setRecruiterVerificationKey(null);
                    accountDataModel.setRecruiter(true);
                    accountDataModel.setWorkEmailIdVerified(true);
                    accountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    final HttpSession session = httpRequest.getSession();
                    session.invalidate();
                    model.addAttribute("successMessage",
                            "Account Verified Successfully. Please go to Recruiter sign in page to start using JobXR.");
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

}
