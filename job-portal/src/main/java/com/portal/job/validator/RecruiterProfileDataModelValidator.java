package com.portal.job.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.model.RecruiterProfileDataModel;

@Component
public class RecruiterProfileDataModelValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return RecruiterProfileDataModel.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		final RecruiterProfileDataModel registerDataModel = (RecruiterProfileDataModel) obj;

		ValidationUtils.rejectIfEmpty(errors, "userBasicDataModel.firstName",
				null, "First Name can not be empty.");
		ValidationUtils.rejectIfEmpty(errors,
				"userBasicDataModel.profileHeadline", null,
				"Profile Headline can not be empty.");
		ValidationUtils.rejectIfEmpty(errors,
				"userBasicDataModel.mobileNumber", null,
				"Mobile Number can not be empty.");
		final String mobileNumber = registerDataModel.getUserBasicDataModel()
				.getMobileNumber();
		if (!StringUtils.isEmpty(mobileNumber) && mobileNumber.length() != 10) {
			errors.rejectValue("userBasicDataModel.mobileNumber", null,
					"Mobile number can be more than 10 digits");
		}

		// exp.

		ValidationUtils.rejectIfEmpty(errors,
				"userExperienceDataModel.companyName", null,
				"Company  can not be empty.");
		ValidationUtils.rejectIfEmpty(errors,
				"userExperienceDataModel.roleName", null,
				"Role can not be empty.");
		ValidationUtils.rejectIfEmpty(errors,
				"userExperienceDataModel.location", null,
				"Location can not be empty.");
		ValidationUtils.rejectIfEmpty(errors,
				"userExperienceDataModel.startDate", null,
				"Start date can not be empty.");

	}

}
