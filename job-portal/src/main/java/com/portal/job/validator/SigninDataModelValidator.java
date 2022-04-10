package com.portal.job.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.model.SigninDataModel;

@Component
public class SigninDataModelValidator implements Validator {

	@Autowired
	private EmailValidator emailValidator;

	public boolean supports(Class<?> clazz) {
		return SigninDataModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		final SigninDataModel signinDataModel = (SigninDataModel) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", null,
				"Password is not valid.");
		if (StringUtils.isEmpty(signinDataModel.getEmailId())
				|| !emailValidator.isValid(signinDataModel.getEmailId())) {
			errors.rejectValue("emailId", null, "email is not valid");
		}

	}
}
