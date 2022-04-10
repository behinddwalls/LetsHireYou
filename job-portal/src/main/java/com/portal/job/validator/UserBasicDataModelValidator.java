package com.portal.job.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.model.UserBasicDataModel;

@Component
public class UserBasicDataModelValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return UserBasicDataModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", null,
				"First Name can not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", null,
				"Gender can not be null or empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", null,
				"Date of birth can not be null or empty.");

	}
}
