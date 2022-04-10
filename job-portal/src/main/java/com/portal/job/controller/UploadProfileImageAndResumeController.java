package com.portal.job.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.text.ParseException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.portal.job.model.UserBasicDataModel;
import com.portal.job.resumeparsing.xmlparsing.parser.JAXBParser;
import com.portal.job.resumeparsing.xmlparsing.parser.ProcessCurrentResume;
import com.portal.job.resumeparsing.xmlparsing.parser.SaveResumeHandler;
import com.portal.job.resumeparsing.xmlparsing.parser.SovrenResumeWrapper;
import com.portal.job.service.UserDetailService;
import com.portal.job.services.s3.JobPortalAwsS3UploadService;
import com.portal.job.validator.FileUploadValidator;
import com.portal.job.constants.FileTypeConstants;
/**
 * 
 * @author pandeysp Some file reference :
 *         http://stackoverflow.com/questions/17498426
 *         /upload-file-form-data-spring-mvc-jquery
 * @modified Kai
 */

@Controller
public class UploadProfileImageAndResumeController extends JobPortalBaseController {

	private static Logger log = LoggerFactory.getLogger(UploadProfileImageAndResumeController.class);

	@Autowired
	private JAXBParser resumeParser;
	@Autowired
	private SaveResumeHandler saveResumeHandler;
	@Autowired
	private FileUploadValidator fileUploadValidator;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private JobPortalAwsS3UploadService uploadService;
	@Autowired
	private ProcessCurrentResume processCurrentResume;
	
	final long IMAGESIZEINKB=500;
	final long RESUMESIZEINKB=2*1024;

	@InitBinder("fileUploadForm")
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(fileUploadValidator);
	}

	@RequestMapping("/jobseeker/mockUpload")
	public void mockUpload() throws Exception {
		SovrenResumeWrapper resumeWrapper = new SovrenResumeWrapper(resumeParser.parse("xmlFiles/4.xml"));
		saveResumeHandler.saveResumeToDbForUser(resumeWrapper, getUserId(), getAccountId());
	}

	@RequestMapping("/jobseeker/upload/resume")
	public @ResponseBody Map<String, String> uploadresume(Model model,
			@RequestParam("resumeData") MultipartFile multipartFile, BindException errors,
			final HttpServletRequest request) throws JAXBException {
		Map<String, String> responseMap = Maps.newHashMap();
		String fileType=null;
		try {
			if (multipartFile != null && !multipartFile.isEmpty()) {
				if(((multipartFile.getSize())/(1024))>RESUMESIZEINKB)
				{
					System.out.println("Resume too large");
					responseMap.put("successMessage", "Resume file too large. Select a file of less than 2 MB !!!");
				}
				else
				{
					File dir = new File(request.getSession().getServletContext().getRealPath("/tmp"));
					Long userId=getUserId();
					String fileName = userId + "-resumeFile";
					// save the profile
					File serverFile = saveFileToServer(multipartFile, dir, fileName);
					boolean fileTypeFlag=false;
					try
					{
						final Tika tika = new Tika();
						fileType=tika.detect(serverFile).split("/")[1];
						System.out.println(fileType);
						for(int i=0;i<FileTypeConstants.FILETYPES.length;i++)
						{
							if(fileType.equals(FileTypeConstants.FILETYPES[i]))
							{
								fileTypeFlag=true;
								break;
							}
						}
						if(!fileTypeFlag)
						{
							System.out.println("Filetype error!!! Please select filetype of either doc or docx or pdf or rtf");
							responseMap.put("successMessage", "Please select filetype of either doc or docx or pdf or rtf");
							model.addAttribute("errorMessage", "Please select filetype of either doc or docx or pdf or rtf");
						}
					}
					catch(IOException e)
					{
						System.out.println("Unable detect the filetype!!! Please select filetype of either doc or docx or pdf or rtf");
						responseMap.put("successMessage", "Unable detect the filetype!!! Please select filetype of either doc or docx or pdf or rtf");
						model.addAttribute("errorMessage", "Unable detect the filetype!!! Please select filetype of either doc or docx or pdf or rtf");
					}
					
					//--------------------------Resume Parsing Logic Starts-----------------------------------
					if((fileTypeFlag)&&checkResumeConstraints(serverFile,fileType))
					{
						String uploadedResumeFileKey = uploadService.uploadResumeFile(serverFile);
						String resumeFileUrl = uploadService.getResumeFileUrl(uploadedResumeFileKey);
						saveUserResumeFileUrl(resumeFileUrl);
						System.out.println("RESUME   " + resumeFileUrl);
						responseMap.put("successMessage", "Resume uploaded successfully!!!");
						/*responseMap.put("Resume Url", resumeFileUrl);
						model.addAttribute("successMessage", "Resume is uploaded successfully !!!");
						if(!(userDetailService.isResumeParsed(userId)))
						{
							try
							{
								processCurrentResume.processResume(serverFile);
								responseMap.put("successMessage", "Resume is parsed successfully !!!");
								model.addAttribute("successMessage", "Resume is parsed successfully !!!");
								userDetailService.resumeParsed(userId);
							}
							catch(Exception e)
							{
								log.error("Error in ProcessCurrentResumeController");
								model.addAttribute("successMessage", "Resume parsing failed !!!");
								responseMap.put("successMessage", "Resume parsing failed !!!");
							}
						}*/
					}
					else if(fileTypeFlag)
					{
						model.addAttribute("Resume cannot be uploaded due to poor quality!!");
						responseMap.put("successMessage", "Resume cannot be uploaded due to poor quality!!");
					}
					//---------------------------Resume Parsing Logic Ends-------------------------------------
					serverFile.delete();
	
					/*
					 * File dir = new File("/Users" + File.separator +
					 * "uploadResume" + File.separator +
					 * String.valueOf(getUserId())); File serverFile =
					 * saveFileToServer(multipartFile, dir);
					 * 
					 * // call sovren to parse this Resume. String xml =
					 * parsingServiceClient.parseResume(serverFile); // save the
					 * string 'xml' response in a file. serverFile =
					 * saveXMLResponse(serverFile, xml); // Now Parse the response.
					 * 
					 * // Parse the Uploaded resume.
					 * 
					 * SovrenResumeWrapper resumeWrapper = new SovrenResumeWrapper(
					 * resumeParser.parse(serverFile));
					 * saveResumeHandler.saveResumeToDB(resumeWrapper);
					 * 
					 * 
					 * // set the value to return the response.
					 */ 
				} 
			}
			else {
				responseMap.put("successMessage", "Please select the resume to upload !!!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errorMessage", "Failed to upload the resume !!!");
			responseMap.put("successMessage", "Failed to upload the resume !!!");
		}
		return responseMap;
	}

	@RequestMapping("/jobseeker/upload/profileImage")
	public @ResponseBody Map<String, Object> uploadProfileImage(Model model,
			@RequestParam("profileImageData") MultipartFile multipartFile, BindException errors,
			final HttpServletRequest request) throws JAXBException {
		Map<String, Object> responseMap = Maps.newHashMap();
		try {

			if (multipartFile != null && !multipartFile.isEmpty()) {
				if((multipartFile.getSize())/(1024)>IMAGESIZEINKB)
				{
					responseMap.put("successMessage", "Image too large. Select a file of less than 500 KB !!!");
				}
				else
				{
					File dir = new File(request.getSession().getServletContext().getRealPath("/tmp"));
					String fileName = getUserId() + "-profileImage";
					// save the profile
					try (InputStream input = multipartFile.getInputStream()) {
						try {
							ImageIO.read(input).toString();
							System.out.println("It's an image (only BMP, GIF, JPG and PNG are recognized)");
							File serverFile = saveFileToServer(multipartFile, dir, fileName);
							String uploadedProfileImageKey = uploadService.uploadProfileImage(serverFile);
							String profileImageUrl = uploadService.getProfileImageUrl(uploadedProfileImageKey);
	
							System.out.println("PROFILE   " + profileImageUrl);
	
							// Save the profile Image to User table.
							saveUserProfileImage(profileImageUrl);
							// delete file from tmp
							serverFile.delete();
							// set the value to return the response.
							responseMap.put("successMessage", "Profile Image is uploaded successfully !!!");
							responseMap.put("imageUrl", profileImageUrl);
						} catch (Exception e) {
							responseMap.put("successMessage", "Selected profile image is not the required type of file!!!\nPlease select a file type of BMP or GIF or JPG or PNG");
							System.out.println("It's not an image.");
						}
					}
				}
			}
			else {
				responseMap.put("successMessage", "Please select the profile image to upload !!!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			responseMap.put("successMessage", "Failed to upload the profile image !!!");
		}
		if(responseMap.isEmpty())
			System.out.println("empty");
		return responseMap;
	}

	// private method
	/*
	 * Save the path relative from the servlet context path (/portal) After
	 * saving the userImage profile to physical location to server it takes only
	 * the path 'relative to' servlet context and saves it to DB.
	 */
	private void saveUserProfileImage(final String profileImageContextPath) throws ParseException {
		// now save the file path attached to User table.
		// fetch the Detail for User and then update the detail for him.s
		UserBasicDataModel userDataModel = this.userDetailService.getUserBasicDataModel(getUserId());
		// set profileImage
		userDataModel.setProfileImageUrl(profileImageContextPath);
		// fill the profilePath data and update the value back.
		this.userDetailService.addOrUpdateUserBasicDetail(userDataModel, getUserId(), getAccountId());
	}

	private void saveUserResumeFileUrl(final String resumeFileContextPath) throws ParseException {
		// now save the file path attached to User table.
		// fetch the Detail for User and then update the detail for him.s
		UserBasicDataModel userDataModel = this.userDetailService.getUserBasicDataModel(getUserId());
		// set profileImage
		userDataModel.setUserResumeLink(resumeFileContextPath);
		// fill the profilePath data and update the value back.
		this.userDetailService.addOrUpdateUserBasicDetail(userDataModel, getUserId(), getAccountId());
	}

	// save the file to Server.
	/*
	 * Path is specifically for getting for careting the Directory which we want
	 * to store the file specific to use case.
	 */
	private File saveFileToServer(final MultipartFile multipartFile, final File rootDir) throws IOException {
		String fileName = "";
		fileName = multipartFile.getOriginalFilename();
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		// First delete all the files from the Base directory before creating
		// new file
		deleteFilesInDirectory(rootDir);
		//
		// Create the file on server
		File serverFile = new File(rootDir.getAbsolutePath() + File.separator + fileName);
		if (!serverFile.exists()) {
			serverFile.createNewFile();
		}
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(multipartFile.getBytes());
		stream.close();
		return serverFile;
	}

	private File saveFileToServer(final MultipartFile multipartFile, final File rootDir, final String fileName)
			throws IOException {

		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		String fileExtension = Files.getFileExtension(multipartFile.getOriginalFilename());
		// Create the file on server
		File serverFile = new File(rootDir.getAbsolutePath() + File.separator + fileName + "." + fileExtension);
		if (!serverFile.exists()) {
			serverFile.createNewFile();
		}
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(multipartFile.getBytes());
		stream.close();
		return serverFile;
	}

	// // delete the files present in the directory.
	private void deleteFilesInDirectory(File file) throws IOException {

		if (file.isDirectory()) {

			// We want to delete the files in directory not director
			if (file.list().length != 0) {
				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteFilesInDirectory(fileDelete);
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}

//	// private save XML response.
//	private File saveXMLResponse(final File file, final String xmlStr) throws IOException {
//		//
//		/*
//		 * final String xmlStr =
//		 * "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
//		 * "<Emp id=\"1\"><name>Pankaj</name><age>25</age>\n" +
//		 * "<role>Developer</role><gen>Male</gen></Emp>";
//		 */
//
//		// Create the file on save XML file
//		File serverFile = new File(file.getParent() + File.separator + "SOVERN.xml");
//		// if file doesn't exists, then create it
//		if (!serverFile.exists()) {
//			serverFile.createNewFile();
//		}
//		FileOutputStream fop = new FileOutputStream(serverFile);
//		// get the content in bytes
//		byte[] contentInBytes = xmlStr.getBytes();
//		fop.write(contentInBytes);
//		fop.flush();
//		fop.close();
//		return serverFile;
//	}
	
	private boolean checkResumeConstraints(File file,String fileType) {
		try
		{
			/*if(fileType.equals(FileTypeConstants.PDF))
			{
				PDDocument pdd= PDDocument.load(file);
				PDFTextStripper reader=new PDFTextStripper();
				String text=reader.getText(pdd);
				return checkTextForCOnstraints(text);
			}
			else if(fileType.equals(FileTypeConstants.RTF))
			{
				FileInputStream fis=new FileInputStream(file);
				RTFEditor
			}
			else if(fileType.equals(FileTypeConstants.DOC))
			{
				
			}
			else if(fileType.equals(FileTypeConstants.DOCX))
			{
				
			}*/
			return true;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

}
