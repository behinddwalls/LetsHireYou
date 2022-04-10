package com.portal.job.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.dao.model.RequestDemoDetail;

@Component
public class RequestDemoDataModelValidator implements Validator {

	@Autowired
	protected EmailValidator emailValidator;

	public boolean supports(Class<?> clazz) {
		return RequestDemoDetail.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		final RequestDemoDetail requestDemoDetail = (RequestDemoDetail) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null,
				"Full Name can not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName", null,
				"Comapny Name can not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", null,
				"Email is not valid.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobileNumber", null,
				"Mobile number is not valid.");

		if ((StringUtils.isEmpty(requestDemoDetail.getEmailId()) || !emailValidator
				.isValid(requestDemoDetail.getEmailId()))) {
			errors.rejectValue("emailId", null, "Email is not valid");
		}
	}

}
