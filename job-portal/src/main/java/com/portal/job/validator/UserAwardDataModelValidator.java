package com.portal.job.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserAwardDataModel;

@Component
public class UserAwardDataModelValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAwardDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserAwardDataModel awardDataModel = (UserAwardDataModel)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", null,"This Field cannot be Empty");
		
		if (!StringUtils.isEmpty(awardDataModel.getDate() )) {
			Date d = DateValidator.getInstance().validate(
					awardDataModel.getDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("date", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		
	}

}
