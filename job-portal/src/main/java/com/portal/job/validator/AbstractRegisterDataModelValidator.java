package com.portal.job.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.collect.Lists;
import com.portal.job.enums.RecruiterType;
import com.portal.job.model.BaseRegisterDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;

public abstract class AbstractRegisterDataModelValidator implements Validator {

    private List<String> emailServiceProviders = Lists.newArrayList(new String[] { "gmail", "outlook", "rocketmail",
            "yahoo", "live", "ymail" });

    @Autowired
    protected EmailValidator emailValidator;

    public boolean supports(Class<?> clazz) {
        return BaseRegisterDataModel.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        final BaseRegisterDataModel registerDataModel = (BaseRegisterDataModel) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", null, "FirstName can not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", null, "lastName can not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", null, "Password is not valid.");

        if (StringUtils.isEmpty(registerDataModel.getVerifyPassword())
                || !registerDataModel.getPassword().equals(registerDataModel.getVerifyPassword())) {
            errors.rejectValue("verifyPassword", null, "Password doesn't match.");
        }

        if ((obj instanceof JobseekerRegisterDataModel)
                && (StringUtils.isEmpty(registerDataModel.getEmailId()) || !emailValidator.isValid(registerDataModel
                        .getEmailId()))) {
            errors.rejectValue("emailId", null, "Email is not valid");
        } else if ((obj instanceof RecruiterRegisterDataModel)
                && (StringUtils.isEmpty(registerDataModel.getWorkEmailId())
                        || !emailValidator.isValid(registerDataModel.getWorkEmailId()) || (!isEmailEligibleForWorkEmailId(registerDataModel
                        .getWorkEmailId()) && !((RecruiterRegisterDataModel) registerDataModel).getRecruiterType()
                        .equals(RecruiterType.FREE_LANCER)))) {
            errors.rejectValue("workEmailId", null, "Email is not valid");
        }
    }

    private boolean isEmailEligibleForWorkEmailId(final String emailId) {
        String splitEmailid[] = emailId.split("[@._]");
        System.out.println(splitEmailid[1]);
        return !emailServiceProviders.contains(splitEmailid[1]);
    }
}
