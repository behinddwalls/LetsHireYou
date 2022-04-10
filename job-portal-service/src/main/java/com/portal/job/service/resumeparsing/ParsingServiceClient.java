package com.portal.job.service.resumeparsing;
import java.io.*;
import java.net.*;
import java.util.zip.GZIPOutputStream;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.portal.job.task.OnPostOrEditJobComputeCandidatesTask;

/**
 * 
 * @author pandeysp
 * Wiki : http://wiki.sovren.com/display/SUP/Getting+Started
 */
@Component
public class ParsingServiceClient {

	private static String AccountId = "34710524";
	private static String ServiceKey = "c3d5PQz9G93dZSZH8F5/0G6/7r/98F54cdsp0gaI";
	
	/**
	 * 
	 * @param resumeFilePath
	 * @return
	 * @throws Exception
	 * 
	 * Takes the input as FilePath and returns the expected parsed 
	 * 'XML String' for the Resume parsed by 'SOVREN' Client.
	 */
	public String parseResume(final byte[] fileBytes) throws Exception
	{
		try {

			// Optionally, compress the bytes to reduce network transfer time
			byte[] bytes = gzip(fileBytes);

			// Get a client proxy for the web service

			ParsingService service = new ParsingService(
					new URL("https://services.resumeparsing.com/ParsingService.asmx?wsdl"),
					new QName("http://services.resumeparsing.com/", "ParsingService"));
			ParsingServiceSoap soap = service.getParsingServiceSoap();

			// Required parameters

			ParseResumeRequest parseRequest = new ParseResumeRequest();
			parseRequest.setAccountId(AccountId);
			parseRequest.setServiceKey(ServiceKey);
			parseRequest.setFileBytes(bytes);

			// Optional parameters

			// parseRequest.setConfiguration(""); // Paste string from Parser Config String Builder.xls spreadsheet
			// parseRequest.setOutputHtml(true); // Convert to HTML
			// parseRequest.setOutputRtf(true); // Convert to RTF
			// parseRequest.setOutputWordXml(true); // Convert to WordXml
			// parseRequest.setRevisionDate("2011-05-15"); // Parse assuming a
			// historical date for "current"

			ParseResumeResponse2 parseResult = soap.parseResume(parseRequest);

			System.out.println("Code=" + parseResult.getCode());
			System.out.println("SubCode=" + parseResult.getSubCode());
			System.out.println("Message=" + parseResult.getMessage());
			System.out.println("TextCode=" + parseResult.getTextCode());
			System.out.println("CreditsRemaining=" + parseResult.getCreditsRemaining());
			System.out.println("-----");
			//System.out.println(parseResult.getXml());
			//System.out.println("@@@@@  Result:"+parseResult.getXml());
			//return the value parsed Resume.
			return parseResult.getXml();

		} catch (Exception ex) {
			System.out.println(ex);
			throw ex;
		}

	}
		
	/**
	 * 
	 * @param resumeFilePath
	 * @return
	 * @throws Exception
	 * 
	 * Takes the input as FilePath and returns the expected parsed 
	 * 'XML String' for the Resume parsed by 'SOVREN' Client.
	 */
	public String parseResume(final File resumeFilePath) throws Exception
	{
			System.out.println("#####  Insiode the Sovren Service client !!!!");
			// Load the resume into a byte array. These bytes could be from a
			// web page file upload, a database, or any other source. It is a
			// file in this example only for the sake of keeping this sample
			// simple. We recommend using the binary data to avoid common
			// character encoding issues when reading the file as a string.
			//byte[] bytes = getBytesFromFile(new File("resume.doc"));
			return parseResume(getBytesFromFile(resumeFilePath));

	}
	
	public static void ParseJobOrder()
	{
		try {

			// Load the job order into a byte array. These bytes could be from a
			// web page file upload, a database, or any other source. It is a
			// file in this example only for the sake of keeping this sample
			// simple. We recommend using the binary data to avoid common
			// character encoding issues when reading the file as a string.
			byte[] bytes = getBytesFromFile(new File("JobOrder.txt"));

			// Optionally, compress the bytes to reduce network transfer time
			bytes = gzip(bytes);

			// Get a client proxy for the web service

			ParsingService service = new ParsingService(
					new URL("https://services.resumeparsing.com/ParsingService.asmx?wsdl"),
					new QName("http://services.resumeparsing.com/", "ParsingService"));
			ParsingServiceSoap soap = service.getParsingServiceSoap();

			// Required parameters

			ParseJobOrderRequest parseRequest = new ParseJobOrderRequest();
			parseRequest.setAccountId(AccountId);
			parseRequest.setServiceKey(ServiceKey);
			parseRequest.setFileBytes(bytes);

			// Optional parameters
			
			// parseRequest.setOutputHtml(true); // Convert to HTML
			// parseRequest.setOutputRtf(true); // Convert to RTF
			// parseRequest.setOutputWordXml(true); // Convert to WordXml
			// parseRequest.setRevisionDate("2011-05-15"); // Parse assuming a
			// historical date for "current"

			ParseJobOrderResponse2 parseResult = soap.parseJobOrder(parseRequest);

			System.out.println("Code=" + parseResult.getCode());
			System.out.println("SubCode=" + parseResult.getSubCode());
			System.out.println("Message=" + parseResult.getMessage());
			System.out.println("TextCode=" + parseResult.getTextCode());
			System.out.println("CreditsRemaining=" + parseResult.getCreditsRemaining());
			System.out.println("-----");
			System.out.println(parseResult.getXml());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		try {

			// Get the size of the file
			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				// File is too large
			}

			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}

			// Return the file content
			return bytes;
		} finally {
			// Close the input stream
			is.close();
		}
	}

	public static byte[] gzip(byte[] bytes) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(
				bytes.length / 2);
		try {
			GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
			try {
				zipStream.write(bytes);
			} finally {
				zipStream.close();
			}
			return byteStream.toByteArray();
		} finally {
			byteStream.close();
		}
	}

}
