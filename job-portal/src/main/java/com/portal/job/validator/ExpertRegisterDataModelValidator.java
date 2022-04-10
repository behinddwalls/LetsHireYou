package com.portal.job.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ExpertRegisterDataModelValidator extends
		AbstractRegisterDataModelValidator {

	public void validate(Object obj, Errors errors) {
		super.validate(obj, errors);

	}
}
