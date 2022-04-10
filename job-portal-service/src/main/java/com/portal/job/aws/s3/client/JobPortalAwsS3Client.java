package com.portal.job.aws.s3.client;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.portal.job.constants.AwsConstants;

@Component
public final class JobPortalAwsS3Client {

	@Resource(name = "awsCredentialConfig")
	private Map<String, String> awsCredentialConfig;

	private AmazonS3 s3Client;
	
	//For Testing I'm creating the differnt Constructor
	public JobPortalAwsS3Client(final AmazonS3 s3Client) {
		final AWSCredentials credentials = new BasicAWSCredentials("AKIAJ3H42TTVWYQTDYUA",
				"8N2d6JORisQ4YwYJLlNVEAJ7jQ/AixYoLuOQ/MSI");
		this.s3Client = new AmazonS3Client(credentials);
		Region apSouthEast1 = Region.getRegion(Regions.AP_SOUTHEAST_1);
		this.s3Client.setRegion(apSouthEast1);
	}

	public JobPortalAwsS3Client() {
		
	}
	
	@PostConstruct
	public void init() {
		final AWSCredentials credentials = new BasicAWSCredentials(
				awsCredentialConfig.get(AwsConstants.ACCESS_KEY),
				awsCredentialConfig.get(AwsConstants.SECRET_KEY));
		s3Client = new AmazonS3Client(credentials);
		Region apSouthEast1 = Region.getRegion(Regions.AP_SOUTHEAST_1);
		s3Client.setRegion(apSouthEast1);
	}

	public AmazonS3 getS3Client() {
		return s3Client;
	}

}
