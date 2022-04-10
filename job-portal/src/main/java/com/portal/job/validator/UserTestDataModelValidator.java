package com.portal.job.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserTestDataModel;

@Component
public class UserTestDataModelValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserTestDataModel.class.equals(clazz);
		
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserTestDataModel testDataModel = (UserTestDataModel)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "testName", null,"This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "testScore", null,"This Field cannot be empty");	
		if (!StringUtils.isEmpty(testDataModel.getTestDate())) {
			Date d = DateValidator.getInstance().validate(
					testDataModel.getTestDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("testDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		
	}

}
