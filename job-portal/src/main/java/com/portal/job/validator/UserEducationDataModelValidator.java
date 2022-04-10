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
import com.portal.job.model.UserEducationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserEducationDataModelValidator implements Validator {
	
	private static Logger log = LoggerFactory
			.getLogger(UserEducationDataModelValidator.class);

	public boolean supports(Class<?> clazz) {
		return UserEducationDataModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		log.info("zzzzzzz about to do validations for education");
		final UserEducationDataModel educationData = (UserEducationDataModel) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "organisationName", null,
				"School cannot be empty");
		if (!StringUtils.isEmpty(educationData.getStartDate() )) {
			Date d = DateValidator.getInstance().validate(
					educationData.getStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("startDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}

		if (!StringUtils.isEmpty(educationData.getEndDate())) {
			log.info("zzzzzz endDate = "+educationData.getEndDate());
			Date d = DateValidator.getInstance().validate(
					educationData.getEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			log.info("zzzz date d = "+d);
			if (d == null)
				errors.rejectValue("endDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		if(!StringUtils.isEmpty(educationData.getEndDate()) && !StringUtils.isEmpty(educationData.getStartDate()) ){
			
			try {
				Date endDate = DateUtility.getDateFromString(educationData.getEndDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				Date startDate = DateUtility.getDateFromString(educationData.getStartDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				if(startDate.after(endDate)){
					errors.rejectValue("startDate", null, "StartDate should be before EndDate");
				}
			} catch (ParseException e) {
				errors.rejectValue("startDate", null, "Date not in correct format");
			}			
		}
		//if end date is there but not start date
		if(!StringUtils.isEmpty(educationData.getEndDate()) && StringUtils.isEmpty(educationData.getStartDate())){
				errors.rejectValue("startDate", null, "You cannot just enter the End Date, please add Start Date");
		}

	}

}
