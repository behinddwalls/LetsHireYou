package com.portal.job.validator;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserVolunteerDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserVolunteerDataModelValidator implements Validator {

	private static Logger log = LoggerFactory
			.getLogger(UserVolunteerDataModelValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return UserVolunteerDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserVolunteerDataModel volunteerDataModel = (UserVolunteerDataModel) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName", null,
				"Enter Volunteer Role");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "organisationName",
				null, "Organisation cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "volunteerCause",
				null, "Enter Volunteering Cause");
		if (!StringUtils.isEmpty(volunteerDataModel.getVolunteerStartDate())) {
			Date d = DateValidator.getInstance().validate(
					volunteerDataModel.getVolunteerStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("volunteerStartDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}

		if (!StringUtils.isEmpty(volunteerDataModel.getVolunteerEndDate())) {
			Date d = DateValidator.getInstance().validate(
					volunteerDataModel.getVolunteerEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("volunteerEndDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		if(!StringUtils.isEmpty(volunteerDataModel.getVolunteerEndDate()) && !StringUtils.isEmpty(volunteerDataModel.getVolunteerStartDate()) ){
			
			try {
				Date endDate = DateUtility.getDateFromString(volunteerDataModel.getVolunteerEndDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				Date startDate = DateUtility.getDateFromString(volunteerDataModel.getVolunteerStartDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				if(startDate.after(endDate)){
					errors.rejectValue("volunteerStartDate", null, "StartDate should be before EndDate");
				}
			} catch (ParseException e) {
				errors.rejectValue("volunteerStartDate", null, "Date not in correct format");
			}
		}
		
		if(!StringUtils.isEmpty(volunteerDataModel.getVolunteerEndDate()) && StringUtils.isEmpty(volunteerDataModel.getVolunteerStartDate())){
			errors.rejectValue("volunteerStartDate", null, "You cannot just enter the End Date, please add Start Date");
		}

	}
}
