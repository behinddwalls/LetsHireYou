package com.portal.job.validator;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.model.UserCertificationDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserCertificationDataModelValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserCertificationDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserCertificationDataModel certificationDataModel = (UserCertificationDataModel)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "certificationName", null,"This Field cannot be Empty");
		if(!StringUtils.isEmpty(certificationDataModel.getCertificationUrl()) && !UrlValidator.getInstance().isValid(certificationDataModel.getCertificationUrl())){
			errors.rejectValue("certificationUrl", null, "Please give in format: http://www.xyz.com/path");
		}
		if (!StringUtils.isEmpty(certificationDataModel.getCertificationStartDate() )) {
			Date d = DateValidator.getInstance().validate(
					certificationDataModel.getCertificationStartDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("certificationStartDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		if (!StringUtils.isEmpty(certificationDataModel.getCertificationEndDate())) {			
			Date d = DateValidator.getInstance().validate(
					certificationDataModel.getCertificationEndDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			
			if (d == null)
				errors.rejectValue("certificationEndDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		if(!StringUtils.isEmpty(certificationDataModel.getCertificationEndDate()) && !StringUtils.isEmpty(certificationDataModel.getCertificationStartDate()) ){
			
			try {
				Date endDate = DateUtility.getDateFromString(certificationDataModel.getCertificationEndDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				Date startDate = DateUtility.getDateFromString(certificationDataModel.getCertificationStartDate(), DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				if(startDate.after(endDate)){
					errors.rejectValue("certificationStartDate", null, "Start Date should be before EndDate");
				}
			} catch (ParseException e) {
				errors.rejectValue("certificationStartDate", null, "Date not in correct format");
			}
		}
		if(!StringUtils.isEmpty(certificationDataModel.getCertificationEndDate()) && StringUtils.isEmpty(certificationDataModel.getCertificationStartDate())){
			errors.rejectValue("certificationStartDate", null, "You cannot just enter the End Date, please add Start Date");
		}
		
	}

}
