package com.portal.job.aws.s3.proxy;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.portal.job.aws.s3.client.JobPortalAwsS3Client;

@Component
public class JobPortalAwsS3RestApiProxy {
	private static Logger log = LoggerFactory
			.getLogger(JobPortalAwsS3RestApiProxy.class);
	@Autowired
	private JobPortalAwsS3Client awsS3Client;
	
	//For testing Creating the Client Object manually.
	public JobPortalAwsS3RestApiProxy(){
		//Just pass null value for 'AmazonS3Client'.
		awsS3Client = new JobPortalAwsS3Client(null);
	}
	

	private AmazonS3 getClient() {
		return awsS3Client.getS3Client();
	}

	public List<Bucket> getAllBuckets() {
		return getClient().listBuckets();
	}
	
	public ObjectListing getAllObjectsInBucket(final String bucketName){
		log.info("zzzzzzzz getAllObjectsInBuckets called, bucketName" +bucketName);
		return getClient().listObjects(bucketName);				
	}

	public PutObjectResult putPublicObject(final String bucketName,
			final String key, final File file) {
		PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
		request.withCannedAcl(CannedAccessControlList.PublicRead);
		return getClient().putObject(request);
	}

	public S3Object getObject(final String bucketName, final String key) {
		GetObjectRequest request = new GetObjectRequest(bucketName, key);
		return getClient().getObject(request);
	}

	public CopyObjectResult copyObject(final String fromBucketName,final String key, final String toBucketName){
		CopyObjectRequest copyRequest = new CopyObjectRequest(fromBucketName, key, toBucketName, key);
		return getClient().copyObject(copyRequest);
	}
	
	public void deleteUnversionedObject(final String bucketName, final String key){
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
		getClient().deleteObject(deleteObjectRequest);
	}
	public String getObjectUrl(final String bucketName, final String key) {
		return "http://" + bucketName + ".s3.amazonaws.com/" + key;
	}

	public String getSecuredObjectUrl(final String bucketName, final String key) {
		return "https://" + bucketName + ".s3.amazonaws.com/" + key;
	}
}
