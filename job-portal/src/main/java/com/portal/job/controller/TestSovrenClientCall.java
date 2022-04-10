/*package com.portal.job.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.portal.job.aws.s3.proxy.JobPortalAwsS3RestApiProxy;
import com.portal.job.service.resumeparsing.ParsingServiceClient;


 * 
 * @author pandeysp
 * 
 *         - See more at:
 *         http://java2novice.com/java-file-io-operations/file-list
 *         -from-folder/#sthash.XrrMGuVH.dpuf
 * 
 * 
 *         TODO- Need to remove this class once the testing is done.
 


public class TestSovrenClientCall {

	private final ParsingServiceClient client = new ParsingServiceClient();
	private final JobPortalAwsS3RestApiProxy awsS3Proxy = new JobPortalAwsS3RestApiProxy();
	private static final String FILE_READ_PATH_DIR = "/resume_to_parse_by_sovrev/";
	private static final String FILE_WRITE_PATH_DIR = "/resume_to_parse_by_sovrev/parsed_resume/";

	public static void main(String[] args) {
		new TestSovrenClientCall().callSovrenClient();
		//new TestSovrenClientCall().uploadSovrenParsedResumeToS3();
	}

	
	 * This Function Reads the 'FILE_READ_PATH_DIR' and fetch the all files
	 * present in that directory. After that loops through every files and call
	 * 'SOVREN Client'. Once we are getting 'XML String response' from the
	 * sovren we write that response on the 'FILE_WRITE_PATH_DIR' directory. And
	 * deletes the already parsed files from the 'FILE_READ_PATH_DIR' dir. After
	 * that we make a call to S3 for uploading all the file in the S3
	 * 'RESUME_UNPROCESSED_BUCKET' present in the 'FILE_READ_PATH_DIR' dir.
	 
	private void callSovrenClient() {
		//Calculate the Time lapsed for processing.
		//Get the start time.
		Date startingTime = Calendar.getInstance().getTime();
		
		Writer writer = null;
		File filePathToResumes = new File(FILE_READ_PATH_DIR);
		File[] files = filePathToResumes.listFiles();
		for (File file : files) {
			try {
				System.out.println(file.getName() + " Path:"
						+ file.getAbsolutePath());
				// Calling API.
				if (!file.isDirectory()) {
					String response = client.parseResume(file);
					System.out.println("!!!!! Output Response:");
					// String response =
					// "#### Put the sovren parsed in the file";
					// Crate the new 'SOVERN XML' File by appending the Name of
					// file
					// we are parsing through SOVREN..
					writer = new FileWriter(new File(FILE_WRITE_PATH_DIR
							+ FilenameUtils.removeExtension(file.getName())
							+ ".xml"));
					writer.write(response);

					// Now delete the file that has been parsed already .
					file.deleteOnExit();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Some error in calling the client ");
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Error closing the file : ");
				}
			}
		}
		//Get the end time:
		Date endTime = Calendar.getInstance().getTime();
		long elapsedTime = endTime.getTime()-startingTime.getTime();
		//convert it to min.
		System.out.println("Time elaplsed in SOVREN resume processing:"+elapsedTime/(1000*60));
		
		//Now Upload the Files in S3.
		uploadSovrenParsedResumeToS3();
		//Upload done
	}

	
	 * Uploads all the files present in the 'FILE_READ_PATH_DIR' dir to S3
	 * 'RESUME_UNPROCESSED_BUCKET' bucket.
	 
	private void uploadSovrenParsedResumeToS3() {
		//Get the start time.
		Date startingTime = Calendar.getInstance().getTime();
		
		File filePathToResumes = new File(FILE_WRITE_PATH_DIR);
		File[] files = filePathToResumes.listFiles();
		for (File file : files) {
			try {
				PutObjectResult response = awsS3Proxy.putPublicObject(
						"job-portal-resume-unprocessed",
						FilenameUtils.removeExtension(file.getName()), file);
				//Now delete the files uploaded successfully.
				//file.deleteOnExit();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out
						.println("Some problem in uploading the files To S3 bucket"
								+ ex);
			}
		}
		//Get the elapsed time.
		//Get the end time:
		Date endTime = Calendar.getInstance().getTime();
		long elapsedTime = endTime.getTime()-startingTime.getTime();
		//convert it to min.
		System.out.println("Time elaplsed In S3 upload:"+elapsedTime/(1000*60));
		
	}
}
*/