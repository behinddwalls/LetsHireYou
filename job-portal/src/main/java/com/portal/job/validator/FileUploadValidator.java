package com.portal.job.validator;

import org.apache.commons.fileupload.FileUpload;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;


@Component
public class FileUploadValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		// just validate the FileUpload instances
		return MultipartFile.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

        FileUpload file = (FileUpload) target;

/*		if (file.getFile().getSize() == 0) {
			errors.rejectValue("file", "required.fileUpload");
		}*/
	}
}