package com.portal.job.resumeparsing.xmlparsing.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.job.controller.api.S3ResumeProcessingController;
import com.portal.job.resumeparsing.xmlparsing.parser.JAXBParser;
import com.portal.job.resumeparsing.xmlparsing.parser.ResumeParsingException;
import com.portal.job.resumeparsing.xmlparsing.parser.SaveResumeHandler;
import com.portal.job.resumeparsing.xmlparsing.parser.SovrenResumeWrapper;
import com.portal.job.service.resumeparsing.ParsingServiceClient;
import com.portal.job.services.s3.JobPortalAwsS3UploadService;

/**
 * 
 * @author Kai
 * 
 */

@Component
public class ProcessCurrentResume {

	@Autowired
	private ParsingServiceClient parsingServiceClient;
	@Autowired
	private JAXBParser resumeParser;
	@Autowired
    private SaveResumeHandler saveResumeHandler;
	@Autowired
	private JobPortalAwsS3UploadService uploadService;
	
	
	private static Logger log = LoggerFactory.getLogger(S3ResumeProcessingController.class);
	
	public void processResume(File serverFile) throws Exception
	{
		try
		{			
			String xml = parsingServiceClient.parseResume(serverFile);
			serverFile = saveXMLResponse(serverFile, xml);
			InputStream inputStream=new FileInputStream(serverFile);
			try
			{
				SovrenResumeWrapper parsedResume = resumeParser.parseResume(inputStream);
				saveResumeHandler.saveResumeToDbAndCreateNewUser(parsedResume);
				String resumeFileXMLKey=uploadService.uploadProcessedResumeXMLFile(serverFile);
				String resumeFileXMLUrl=uploadService.getProcessedResumeXMLFileUrl(resumeFileXMLKey);
				System.out.println(resumeFileXMLUrl);
			}
			catch(ResumeParsingException e)
			{
				log.error("Unable to parse the current resume!!! Adding this to UNPROCESSED bucket");
				throw e;
			}
		}
		catch(Exception e)
		{
			String resumeFileXMLKey=uploadService.uploadUnprocessedResumeXMLFile(serverFile);
			String resumeFileXMLUrl=uploadService.getUnprocessedResumeXMLFileUrl(resumeFileXMLKey);
			System.out.println(resumeFileXMLUrl);
			throw e;
		}
	}
	
	// private save XML response.
	private File saveXMLResponse(final File file, final String xmlStr) throws IOException {
		//
		/*
		 * final String xmlStr =
		 * "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
		 * "<Emp id=\"1\"><name>Pankaj</name><age>25</age>\n" +
		 * "<role>Developer</role><gen>Male</gen></Emp>";
		 */

		// Create the file on save XML file
		File serverFile = new File(file.getParent() + File.separator + "SOVERN.xml");
		// if file doesn't exists, then create it
		if (!serverFile.exists()) {
			serverFile.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(serverFile);
		// get the content in bytes
		byte[] contentInBytes = xmlStr.getBytes();
		fop.write(contentInBytes);
		fop.flush();
		fop.close();
		return serverFile;
	}
}
