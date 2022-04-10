package com.portal.job.validator;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserExperienceDataModelValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserExperienceDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserExperienceDataModel experienceDataModel = (UserExperienceDataModel)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName",null, "This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName",null, "This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate",null, "This Field cannot be empty");
		
		if (!StringUtils.isEmpty(experienceDataModel.getStartDate() )) {
			Date d = DateValidator.getInstance().validate(
					experienceDataModel.getStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("startDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}

		if (!StringUtils.isEmpty(experienceDataModel.getEndDate())) {			
			Date d = DateValidator.getInstance().validate(
					experienceDataModel.getEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);			
			if (d == null)
				errors.rejectValue("endDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		//if end date is there but not start date
		if(!StringUtils.isEmpty(experienceDataModel.getEndDate()) && StringUtils.isEmpty(experienceDataModel.getStartDate())){
			errors.rejectValue("startDate", null, "You cannot just enter the End Date, please add Start Date");
		}
		if(!StringUtils.isEmpty(experienceDataModel.getEndDate()) && !StringUtils.isEmpty(experienceDataModel.getStartDate()) ){
			
			try {
				Date endDate = DateUtility.getDateFromString(experienceDataModel.getEndDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				Date startDate = DateUtility.getDateFromString(experienceDataModel.getStartDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				if(startDate.after(endDate)){
					errors.rejectValue("startDate", null, "StartDate should be before EndDate");
				}
			} catch (ParseException e) {
				errors.rejectValue("startDate", null, "Date not in correct format");
			}
		}
		
		
	}

}
