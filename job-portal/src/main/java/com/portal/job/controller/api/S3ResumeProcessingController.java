package com.portal.job.controller.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.portal.job.aws.s3.proxy.JobPortalAwsS3RestApiProxy;
import com.portal.job.constants.AwsConstants;
import com.portal.job.resumeparsing.xmlparsing.parser.JAXBParser;
import com.portal.job.resumeparsing.xmlparsing.parser.ResumeParsingException;
import com.portal.job.resumeparsing.xmlparsing.parser.SaveResumeHandler;
import com.portal.job.resumeparsing.xmlparsing.parser.SovrenResumeWrapper;

@Controller
public class S3ResumeProcessingController {

    @Autowired
    JobPortalAwsS3RestApiProxy awsS3Proxy;
    @Resource(name = "awsS3BucketMap")
    private Map<String, String> awsS3BucketMap;
    @Autowired
    private JAXBParser parser;
    @Autowired
    private SaveResumeHandler saveResumeHandler;
    private static Logger log = LoggerFactory.getLogger(S3ResumeProcessingController.class);

    @RequestMapping("/api/startProcessingResumes")
    public void startProcessingResumes() {

        ObjectListing objectListing = awsS3Proxy.getAllObjectsInBucket(awsS3BucketMap
                .get(AwsConstants.JOB_PORTAL_RESUME_UNPROCESSED));
        List<S3ObjectSummary> objectSumaaries = objectListing.getObjectSummaries();
        log.info("zzzzz objectsummaries size:" + objectSumaaries.size());
        for (S3ObjectSummary objectSummary : objectSumaaries) {
            log.info("objectSummary, bucket: " + objectSummary.getBucketName() + "  key: " + objectSummary.getKey());
            S3Object object = awsS3Proxy.getObject(objectSummary.getBucketName(), objectSummary.getKey());
            try {
                SovrenResumeWrapper parsedResume = parser.parseResume(object.getObjectContent());
                saveResumeHandler.saveResumeToDbAndCreateNewUser(parsedResume);
                moveObjectToSomeOtherBucket(objectSummary.getBucketName(), objectSummary.getKey(),
                        awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_PROCESSED));
            } catch (ResumeParsingException e) {
                log.error("unable to parse resume for kye: " + objectSummary.getKey(), e);
            } catch (Exception e) {
                log.error("Error in trying to save resume, trying to move to failed bucket", e);
                moveObjectToSomeOtherBucket(objectSummary.getBucketName(), objectSummary.getKey(),
                        awsS3BucketMap.get(AwsConstants.JOB_PORTAL_RESUME_FAILED));
            }
        }

    }
    /*
     * This function moves the Object from one S3 bucket to some other different
     * S3 bucket and removes the entries form the First bucket.
     */
    private void moveObjectToSomeOtherBucket(final String fromBucketName, final String key, final String toBucketName) {
        CopyObjectResult copyResult = awsS3Proxy.copyObject(fromBucketName, key, toBucketName);
        awsS3Proxy.deleteUnversionedObject(fromBucketName, key);
    }

}
