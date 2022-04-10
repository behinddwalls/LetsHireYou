package com.portal.job.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portal.job.enums.EmailType;
import com.portal.job.exceptions.NonEqualValueException;
import com.portal.job.exceptions.RequestValueNotChangedException;
import com.portal.job.helper.AccountVerificationHelper;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.BasicAccountSettingsDataModel;
import com.portal.job.service.BasicAccountDetailService;

@Controller
@RequestMapping("/account")
public class AccountController extends JobPortalBaseController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private BasicAccountDetailService basicAccountDetailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = { "/basic", "/updateEmail", "/updatePassword", "" })
    public ModelAndView getAccount(final Model model) {

        final BasicAccountDataModel accountDataModel = basicAccountDetailService
                .getAccountDataModelByAccountId(getAccountId());

        model.addAttribute("basicAccountSettingsDataModel", getBasicAccountSettingsDataModel(accountDataModel));
        return getModelAndView(model, "account/basic-settings");
    }

    @RequestMapping(value = { "/updateEmail" }, method = RequestMethod.POST)
    public ModelAndView updateEmailId(
            @ModelAttribute("basicAccountSettingsDataModel") final BasicAccountSettingsDataModel basicAccountSettingsDataModel,
            final Model model) {

        try {

            final BasicAccountDataModel accountDataModel = basicAccountDetailService
                    .getAccountDataModelByAccountId(getAccountId());

            if (basicAccountSettingsDataModel.getWorkEmailId().equals(accountDataModel.getWorkEmailId())
                    && basicAccountSettingsDataModel.getEmailId().equals(accountDataModel.getEmailId())) {
                throw new RequestValueNotChangedException("Email values are same as previus once.");
            }

            if ((StringUtils.isEmpty(accountDataModel.getEmailId()) && !StringUtils
                    .isEmpty(basicAccountSettingsDataModel.getEmailId()))
                    || (!StringUtils.isEmpty(accountDataModel.getEmailId())
                            && !StringUtils.isEmpty(basicAccountSettingsDataModel.getEmailId()) && !accountDataModel
                            .getEmailId().equals(basicAccountSettingsDataModel.getEmailId()))) {

                accountDataModel.setEmailIdVerified(false);
                accountDataModel.setEmailIdVerificationKey(bCryptPasswordEncoder.encode(basicAccountSettingsDataModel
                        .getEmailId() + new Date().getTime()));
                accountDataModel.setNewUnverifiedEmailId(basicAccountSettingsDataModel.getEmailId());
                basicAccountDetailService.sendVerifyEmailLink(basicAccountSettingsDataModel.getEmailId(),
                        EmailType.EMAIL, accountDataModel.getEmailIdVerificationKey());
            }

            if ((StringUtils.isEmpty(accountDataModel.getWorkEmailId()) && !StringUtils
                    .isEmpty(basicAccountSettingsDataModel.getWorkEmailId()))
                    || (!StringUtils.isEmpty(accountDataModel.getWorkEmailId())
                            && !StringUtils.isEmpty(basicAccountSettingsDataModel.getWorkEmailId()) && !accountDataModel
                            .getWorkEmailId().equals(basicAccountSettingsDataModel.getWorkEmailId()))) {

                accountDataModel.setWorkEmailIdVerified(false);
                accountDataModel.setWorkEmailIdVerificationKey(bCryptPasswordEncoder
                        .encode(basicAccountSettingsDataModel.getWorkEmailId() + new Date().getTime()));
                accountDataModel.setNewUnverfiedWorkEmailId(basicAccountSettingsDataModel.getWorkEmailId());
                basicAccountDetailService.sendVerifyEmailLink(basicAccountSettingsDataModel.getWorkEmailId(),
                        EmailType.WORK_EMAIL, accountDataModel.getWorkEmailIdVerificationKey());
            }

            basicAccountDetailService.addOrUpdateAccountDetail(accountDataModel);
            model.addAttribute("successMessage", "Email updated successfully");
            model.addAttribute("basicAccountSettingsDataModel", getBasicAccountSettingsDataModel(accountDataModel));

        } catch (RequestValueNotChangedException rvnme) {
            log.error("Value not changed", rvnme);
            model.addAttribute("errorMessage", "Email values are same as previus once.");

        } catch (Exception e) {
            log.error("Email Update failed", e);
            model.addAttribute("errorMessage", "Email Update failed.");
        }

        return getModelAndView(model, "account/basic-settings");
    }

    @RequestMapping(value = { "/updatePassword" }, method = RequestMethod.POST)
    public ModelAndView updatePassword(
            @ModelAttribute() final BasicAccountSettingsDataModel basicAccountSettingsDataModel, final Model model) {

        try {
            final BasicAccountDataModel accountDataModel = basicAccountDetailService

            .getAccountDataModelByAccountId(getAccountId());
            try {

                final String passwordHash = bCryptPasswordEncoder.encode(basicAccountSettingsDataModel.getPassword());
                final String verifyPasswordHash = bCryptPasswordEncoder.encode(basicAccountSettingsDataModel
                        .getVerifyPassword());
                System.out.println(basicAccountSettingsDataModel.getPassword());
                System.out.println(basicAccountSettingsDataModel.getVerifyPassword());
                if (bCryptPasswordEncoder.matches(basicAccountSettingsDataModel.getPassword(),
                        accountDataModel.getPasswordHash())) {

                    throw new RequestValueNotChangedException("Password is same as previous password.");

                } else if (!basicAccountSettingsDataModel.getPassword().equals(
                        basicAccountSettingsDataModel.getVerifyPassword())) {
                    throw new NonEqualValueException("Password and confirm password doesn't match.");

                } else {
                    accountDataModel.setPasswordHash(passwordHash);
                    basicAccountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    model.addAttribute("successMessage", "Password updated successfully");
                }

            } catch (RequestValueNotChangedException rvnce) {
                log.error(rvnce.getMessage(), rvnce);
                model.addAttribute("errorMessage", rvnce.getMessage());
            } catch (NonEqualValueException neve) {
                log.error(neve.getMessage(), neve);
                model.addAttribute("errorMessage", neve.getMessage());
            } catch (Exception e) {
                log.error("Failed to update the password.", e);
                model.addAttribute("errorMessage", "Failed to update the password.");
            }
            model.addAttribute("basicAccountSettingsDataModel", getBasicAccountSettingsDataModel(accountDataModel));
        } catch (Exception e) {
            log.error("Fatal error", e);
            getModelAndView(null, "500");
        }

        return getModelAndView(model, "account/basic-settings");
    }

    @RequestMapping(value = "email/verify")
    public ModelAndView verifyRecruiterNewEmailId(@RequestParam final String emailType,
            @RequestParam final String verificationKey, Model model, final HttpServletRequest httpRequest) {
        try {
            if (Optional.ofNullable(verificationKey).isPresent() && Optional.ofNullable(emailType).isPresent()) {
                BasicAccountDataModel accountDataModel = AccountVerificationHelper
                        .getBasicAccountDataModelForMailVerification(getAccountId(), verificationKey,
                                EmailType.valueOf(emailType), basicAccountDetailService);

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
                    basicAccountDetailService.addOrUpdateAccountDetail(accountDataModel);
                    final HttpSession session = httpRequest.getSession();
                    session.invalidate();
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

    private BasicAccountSettingsDataModel getBasicAccountSettingsDataModel(final BasicAccountDataModel accountDataModel) {
        BasicAccountSettingsDataModel basicAccountSettingsDataModel = new BasicAccountSettingsDataModel();
        basicAccountSettingsDataModel.setEmailId(accountDataModel.getEmailId());
        basicAccountSettingsDataModel.setWorkEmailId(accountDataModel.getWorkEmailId());
        basicAccountSettingsDataModel.setNewUnverfiedEmailId(accountDataModel.getNewUnverifiedEmailId());
        basicAccountSettingsDataModel.setNewUnverfiedWorkEmailId(accountDataModel.getNewUnverfiedWorkEmailId());
        return basicAccountSettingsDataModel;
    }
}
