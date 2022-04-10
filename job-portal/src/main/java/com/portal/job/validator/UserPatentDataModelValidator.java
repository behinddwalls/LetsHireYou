package com.portal.job.validator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.portal.job.constants.DateConstants;
import com.portal.job.enums.CountryCodes;
import com.portal.job.enums.PatentStatus;
import com.portal.job.model.UserPatentDataModel;

@Component
public class UserPatentDataModelValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserPatentDataModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserPatentDataModel patentDataModel = (UserPatentDataModel)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patentTitle", null, "This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patentApplicationNumber", null, "This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patentStatus", null, "This Field cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patentOffice", null, "This Field cannot be empty");
		if(!StringUtils.isEmpty(patentDataModel.getPatentUrl()) && !UrlValidator.getInstance().isValid(patentDataModel.getPatentUrl())){
			errors.rejectValue("patentUrl", null, "Please give in format: http://www.xyz.com/path");
		}
		List<String> myList = Arrays.asList(CountryCodes.values()).stream().map(office -> office.getCCValue()).filter(o -> o.compareToIgnoreCase(patentDataModel.getPatentOffice())==0).collect(Collectors.toList());
		if(myList.isEmpty()){    
			errors.rejectValue("patentOffice", null, "Please Enter Correct Patent Issueing Country");			
		}
		List<String> patentStatusList = Arrays.asList(PatentStatus.values()).stream().map(status -> status.name()).filter(p -> p.equals(patentDataModel.getPatentStatus())).collect(Collectors.toList());
		if(!StringUtils.isEmpty(patentDataModel.getPatentStatus()) && !patentStatusList.contains(patentDataModel.getPatentStatus()))
			errors.rejectValue("patentStatus", null, "Invalid Patent Status");
		
		if (!StringUtils.isEmpty(patentDataModel.getPatentFillingDate() )) {
			Date d = DateValidator.getInstance().validate(
					patentDataModel.getPatentFillingDate(),
					DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
			if (d == null)
				errors.rejectValue("patentFillingDate", null,
						"Date in invalid format, correct format: yyyy-mm-dd");
		}
		
		
		
		
		
	}
}
