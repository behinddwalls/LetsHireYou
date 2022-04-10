package com.portal.job.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserProjectDataModel;

@Component
public class UserProjectDataModelValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserProjectDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		final UserProjectDataModel projectData = (UserProjectDataModel)obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "projectName", null, "Project Name cannot be empty");

		if (!StringUtils.isEmpty(projectData.getProjectDate() )) {
			Date d = DateValidator.getInstance().validate(
					projectData.getProjectDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("projectDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		//not empty and invalid
		if(!StringUtils.isEmpty(projectData.getProjectURL()) && !UrlValidator.getInstance().isValid(projectData.getProjectURL())){
			errors.rejectValue("projectURL", null, "Please enter a valid URL: http://www.xyz.com/path");
		}
		
		
	}

}
