package com.portal.job.services.s3;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.portal.job.aws.s3.proxy.JobPortalAwsS3RestApiProxy;
import com.portal.job.constants.AwsConstants;

@Service
public class JobPortalAwsS3UploadService {

	@Autowired
	private JobPortalAwsS3RestApiProxy awsS3RestApiProxy;
	@Resource(name = "awsS3BucketMap")
	private Map<String, String> awsS3BucketMap;

	/**
	 * If key exists then url for the key otherwise empty string.
	 * 
	 * @param profileImageUrlKey
	 * @return
	 */
	public String getProfileImageUrl(final String profileImageUrlKey) {
		return awsS3RestApiProxy.getObjectUrl(awsS3BucketMap.get(AwsConstants.S3_BUCKET_JOB_PORTAL_PROFILE_IMAGE),
				profileImageUrlKey);
	}

	/**
	 * On successful upload return key otherwise empty string.
	 * 
	 * @param file
	 * @return
	 */
	public String uploadProfileImage(final File file) {
		PutObjectResult result = awsS3RestApiProxy.putPublicObject(
				awsS3BucketMap.get(AwsConstants.S3_BUCKET_JOB_PORTAL_PROFILE_IMAGE), file.getName(), file);
		if (result != null && !StringUtils.isEmpty(result.getETag())) {
			return file.getName();
		}
		return StringUtils.EMPTY;
	}

	/*
	 * Using same bucket of profile image file for resume file upload, needs to
	 * be changed
	 */
	public String getResumeFileUrl(final String resumeFileUrlKey) {
		return awsS3RestApiProxy.getObjectUrl(awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_DOWNLOADABLE),
				resumeFileUrlKey);
	}

	public String uploadResumeFile(final File file) {
		PutObjectResult result = awsS3RestApiProxy.putPublicObject(
				awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_DOWNLOADABLE), file.getName(), file);
		if (result != null && !StringUtils.isEmpty(result.getETag())) {
			return file.getName();
		}
		return StringUtils.EMPTY;
	}

	public String getUnprocessedResumeXMLFileUrl(String resumeFileXMLKey) {
		return awsS3RestApiProxy.getObjectUrl(awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_UNPROCESSED),
				resumeFileXMLKey);
	}

	public String uploadUnprocessedResumeXMLFile(final File file) {
		PutObjectResult result = awsS3RestApiProxy
				.putPublicObject(awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_UNPROCESSED), file.getName(), file);
		if (result != null && !StringUtils.isEmpty(result.getETag())) {
			return file.getName();
		}
		return StringUtils.EMPTY;
	}
	
	public String getProcessedResumeXMLFileUrl(String resumeFileXMLKey) {
		return awsS3RestApiProxy.getObjectUrl(awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_PROCESSED),
				resumeFileXMLKey);
	}

	public String uploadProcessedResumeXMLFile(final File file) {
		PutObjectResult result = awsS3RestApiProxy
				.putPublicObject(awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_PROCESSED), file.getName(), file);
		if (result != null && !StringUtils.isEmpty(result.getETag())) {
			return file.getName();
		}
		return StringUtils.EMPTY;
	}
}
