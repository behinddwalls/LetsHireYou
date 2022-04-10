package com.portal.job.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserPublicationDataModel;

@Component
public class UserPublicationDataModelValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserPublicationDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		final UserPublicationDataModel publicationData = (UserPublicationDataModel)obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publicationTitle", null,"Please Enter Publication Title");
		if (!StringUtils.isEmpty(publicationData.getPublicationDate() )) {
			Date d = DateValidator.getInstance().validate(
					publicationData.getPublicationDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("startDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
	
	}

}
