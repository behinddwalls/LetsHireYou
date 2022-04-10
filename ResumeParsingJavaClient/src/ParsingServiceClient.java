import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

import javax.xml.namespace.QName;

import com.resumeparsing.services.ParseJobOrderRequest;
import com.resumeparsing.services.ParseJobOrderResponse2;
import com.resumeparsing.services.ParseResumeRequest;
import com.resumeparsing.services.ParseResumeResponse2;
import com.resumeparsing.services.ParsingService;
import com.resumeparsing.services.ParsingServiceSoap;

public class ParsingServiceClient {
	
	// Replace these values with your account credentials
	private static String AccountId = "34710524";
	private static String ServiceKey = "c3d5PQz9G93dZSZH8F5/0G6/7r/98F54cdsp0gaI";

	public static void main(String[] args) 
	{
		// Parse a resume
		ParseResume();
		
		// Parse a job order
		ParseJobOrder();
	}
	
	public static void ParseResume()
	{
		try {

			// Load the resume into a byte array. These bytes could be from a
			// web page file upload, a database, or any other source. It is a
			// file in this example only for the sake of keeping this sample
			// simple. We recommend using the binary data to avoid common
			// character encoding issues when reading the file as a string.
			byte[] bytes = getBytesFromFile(new File("resume.doc"));

			// Optionally, compress the bytes to reduce network transfer time
			bytes = gzip(bytes);

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
			System.out.println(parseResult.getXml());

		} catch (Exception e) {
			System.out.println(e);
		}
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
