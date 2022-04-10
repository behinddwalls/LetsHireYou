package com.portal.job.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.controller.recruiter.RecruiterJobDetailController;
import com.portal.job.model.JobDataModel;

@Component
public class JobDataModelValidator implements Validator {

    private static Logger log = LoggerFactory.getLogger(RecruiterJobDetailController.class);

    public boolean supports(Class<?> clazz) {
        return JobDataModel.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jobDescription", null, "Job description can not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", null, "Job Title can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", null, "Job location can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "organisationName", null,
                "Organisation name can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "minSalary", null, "Min salary can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maxSalary", null, "Max salary can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "skills", null, "Skills can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jobFunction", null, "JobFunction can not be null or empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "industryName", null,
                "IndustryName can not be null or empty.");

        // Put some extras check about the data integrity of each member field.
        Object expvalue = errors.getFieldValue("jobExperience");
        if (expvalue != null) {
            if (!StringUtils.isNumeric(expvalue.toString())) {
                errors.rejectValue("jobExperiance", null, null, "please put a numeric number for the experiance");
            }
        }

        Object minSalary = errors.getFieldValue("minSalary");
        if (!errors.hasFieldErrors("minSalary")) {
            if (!StringUtils.isNumeric(minSalary.toString())) {
                errors.rejectValue("minSalary", null, null, "Please put a numeric number for minSalary");
            }
            if (Integer.parseInt(minSalary.toString()) <= 0) {
                errors.rejectValue("minSalary", null, null, "MinSalary value should be breater than zero.");
            }
        }

        Object maxSalary = errors.getFieldValue("maxSalary");
        if (!errors.hasFieldErrors("maxSalary")) {
            if (!StringUtils.isNumeric(maxSalary.toString())) {
                errors.rejectValue("maxSalary", null, null, "Please put a numeric number for maxSalary");
            }
            if (Integer.parseInt(maxSalary.toString()) >= 100) {
                errors.rejectValue("maxSalary", null, null,
                        "MaxSalary value should be greater than minsalry and less than 100 Lkh.");
            }
        }

        // put check for min and max salary constraint.
        if (!errors.hasFieldErrors("minSalary") && !errors.hasFieldErrors("maxSalary")) {
            if (Integer.parseInt(minSalary.toString()) > Integer.parseInt(maxSalary.toString())) {
                errors.rejectValue("minSalary", null, null, "MinSalary should be less than MaxSalary");
            }
        }

        Object applicantSettingTypes = errors.getFieldValue("applicantSettingTypes");
        if (applicantSettingTypes != null
                && JobDataModel.ApplicantSettingTypes.EXTERNALURLLINK.getType()
                        .equals(applicantSettingTypes.toString())) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "linkToExternalSite", null,
                    "Please fill the link to extrenal site.");
        }
    }
}
