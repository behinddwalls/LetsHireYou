package com.portal.job.service.aggregator;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.constants.DateConstants;
import com.portal.job.dao.mongodb.PulledJobDataModelRepository;
import com.portal.job.dao.mongodb.model.PulledJobDataModel;
import com.portal.job.enums.JobStatus;
import com.portal.job.exceptions.HttpClientException;
import com.portal.job.httpclient.JobPortalHttpClient;
import com.portal.job.httpclient.JobPortalHttpRequest;
import com.portal.job.model.JobDataModel;
import com.portal.job.utils.DateUtility;

@Service
public class MongoToMySQLAdapterCall {

	@Autowired
	private PulledJobDataModelRepository pulledJobDataModelRepository;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	@Autowired
	private JobPortalHttpClient client;
	@Resource(name = "apiCall")
	private Map<String, String> apiCall;
	@Resource(name = "mySqlMapApi")
	private Map<String, String> mySqlMapApi;
	@Resource(name = "apiCallCredentials")
	private Map<String, String> apiCallCredentials;

	private final int BATCH_COUNT = 500;

	private JobPortalHttpRequest.Builder getJobRequestHttpRequestBuilder() {
		final JobPortalHttpRequest.Builder requestBuilder = new JobPortalHttpRequest.Builder()
				.protocol(apiCall.get("protocol")).host(apiCall.get("host")).port(apiCall.get("port"))
				.basicAuth(apiCallCredentials.get("username"),apiCallCredentials.get("password"));
		return requestBuilder;
	}

	public void call() throws JsonProcessingException {
		System.out.println("inside call");
		Scanner sc;

		int cnt = 0;
		int totcnt = 0;
		LinkedList<JobDataModel> jobDataModelList = new LinkedList<JobDataModel>();
		LinkedList<PulledJobDataModel> pulledJobDataModelList = new LinkedList<PulledJobDataModel>();

		for (PulledJobDataModel pulledJobDataModel : pulledJobDataModelRepository
				.findByParsedToSQL(false)) {
			try{
				final JobDataModel jobDataModel = new JobDataModel();
				if(pulledJobDataModel.getParsedToSQL())
					break;
				String jobId = pulledJobDataModel
						.getJobSourcePortalId();
				String organisationName = pulledJobDataModel.getJobComapnyName();
				String jobDescription = pulledJobDataModel.getJobDescription();
				String jobExp = pulledJobDataModel.getJobExperience();
				String jobFunction;
				if(pulledJobDataModel.getJobRoleCategory()!=null)
					jobFunction = pulledJobDataModel.getJobRoleCategory();
				else
					jobFunction = pulledJobDataModel.getJobFunction();
				String industryName = pulledJobDataModel.getJobIndustryName();
				String jobSkills = pulledJobDataModel.getJobSkills();
				String locationDetail = pulledJobDataModel.getLocation();
				String createDt = pulledJobDataModel.getJobCreatedDate();
				String sal = pulledJobDataModel.getJobSalary();
				String linkToExternalSite = pulledJobDataModel
						.getJobLinkToJobSourcePortal();
				String jobSourcePortal = pulledJobDataModel.getJobSourcePortal();
				String title = pulledJobDataModel.getJobTitle();
	
				jobSkills = jobSkills.replaceAll("\n", ",");
				jobSkills = "," + jobSkills + ",";
	
				/*System.out.println("jobID=" + jobId);
				System.out.println("jobTitle=" + title);
				System.out.println("organisationName=" + jobId);
				System.out.println("jobExp=" + jobExp);
				System.out.println("jobFunction=" + jobFunction);
				System.out.println("industryName=" + industryName);
				System.out.println("locationDetail=" + locationDetail);
				System.out.println("jobSkills=" + jobSkills);
				System.out.println("createDt=" + createDt);
				System.out.println("sal=" + sal);
				System.out.println("linkToSite=" + linkToExternalSite);
				System.out.println("jobSourcePortal=" + jobSourcePortal);
				System.out.println("jobDescription=" + jobDescription);*/
				pulledJobDataModelList.add(pulledJobDataModel);
				int jobExperiance=0;
				try{
					if(!jobExp.equals(null))
					{
						sc = new Scanner(jobExp).useDelimiter("[^0-9]+");
						jobExperiance = sc.nextInt();
					}
				}
				catch(Exception e)
				{
					/*e.printStackTrace();
					System.out.println("Experience Exception");*/
				}
				DateFormat formatter = new SimpleDateFormat(DateConstants.YYY_MM_DD_FORMAT_WITH_DASH);
				Date createDate = null;
				Date expireDate = null;
				try {
					createDate = formatter.parse(createDt);
					Calendar c = Calendar.getInstance();
					c.setTime(createDate);
					c.add(Calendar.DATE, 90);
					expireDate = c.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
					System.out.println("Date Exception");
				}
				int minSalary = 0, maxSalary = 0;
				try
				{
					if(!sal.equals(null))
					{
						boolean fl=false;
						if(sal.contains(","))
						{
							sal=sal.replaceAll(",","");
							fl=true;
						}
						sc = new Scanner(sal).useDelimiter("[^0-9.]+");
						if(sc.hasNextDouble())
						{
							try {
							minSalary = (int)sc.nextDouble();
							if(fl)
								minSalary=(int)(minSalary/100000);
							maxSalary = (int)sc.nextDouble();
							if(fl)
								maxSalary=(int)(maxSalary/100000);
							}
							catch (Exception e) {
							}
						}
					}
				}
				catch(Exception e)
				{
					/*e.printStackTrace();
					System.out.println("Salary Exception");*/
				}
				if(minSalary>maxSalary)
				{
					int tmp=minSalary;
					minSalary=maxSalary;
					maxSalary=tmp;
				}
				String recruiterId = null;
	
				if (jobSourcePortal.equals("naukri"))
					recruiterId = "1";
				else if (jobSourcePortal.equals("shine"))
					recruiterId = "2";
				jobDataModel.setOrganisationName(organisationName);
				jobDataModel.setJobDescription(jobDescription);
				jobDataModel.setJobExperience(jobExperiance);
				//------------------------------------------------------------
				try{
					if(jobFunction.equals("Analytics & BI"))
						jobFunction="Research and Analytics";
				}
				catch(Exception e)
				{
					/*e.printStackTrace();
					System.out.println("JobFunction Exception");*/
				}
				//------------------------------------------------------------
				jobDataModel.setJobFunction(jobFunction);
				jobDataModel.setIndustryName(industryName);
				jobDataModel.setSkills(jobSkills);
				jobDataModel.setLocation(locationDetail);
				jobDataModel.setJobCreatedDate(createDt);
				jobDataModel.setJobModifiedDate(createDt);
				jobDataModel.setMinSalary(String.valueOf(minSalary));
				jobDataModel.setMaxSalary(String.valueOf(maxSalary));
				jobDataModel.setLinkToExternalSite(linkToExternalSite);
				jobDataModel.setRecruiterId(recruiterId);
				jobDataModel.setTitle(title);
	
				jobDataModel.setSalaryCurrencyCode("INR");
				jobDataModel.setKeepSalaryHidden(false);
				jobDataModel.setTopTierOnly(false);
				jobDataModel.setJobStatus(JobStatus.ACTIVE.name());
				jobDataModel.setJobExpiaryDate(DateUtility.getStringFromDate(
						expireDate, DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
	
				/*System.out.println(mappingJackson2HttpMessageConverter
						.getObjectMapper().writeValueAsString(jobDataModel));*/
	
				jobDataModelList.add(jobDataModel);
				cnt++;
				totcnt++;
				if (cnt == BATCH_COUNT) {
					try {
						JobDataModelListWrapper jobDataModelListWrapper = new JobDataModelListWrapper();
						jobDataModelListWrapper
								.setJobDataModelList(jobDataModelList);
						callToMap(jobDataModelListWrapper);
						for (PulledJobDataModel pulledJobDataModelFromList : pulledJobDataModelList) {
							pulledJobDataModelFromList.setParsedToSQL(true);
							this.pulledJobDataModelRepository
									.save(pulledJobDataModelFromList);
						}
						jobDataModelList.clear();
						pulledJobDataModelList.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out
							.println("Saved job number in current batch----------------"
									+ cnt);
					cnt = 0;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("In exception");
				continue;
			}
		}
		if (!pulledJobDataModelList.isEmpty()) {
			try {
				JobDataModelListWrapper jobDataModelListWrapper = new JobDataModelListWrapper();
				jobDataModelListWrapper.setJobDataModelList(jobDataModelList);
				callToMap(jobDataModelListWrapper);
				for (PulledJobDataModel pulledJobDataModelFromList : pulledJobDataModelList) {
					pulledJobDataModelFromList.setParsedToSQL(true);
					this.pulledJobDataModelRepository
							.save(pulledJobDataModelFromList);
				}
				jobDataModelList.clear();
				pulledJobDataModelList.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out
					.println("Saved job number in current batch----------------"
							+ cnt);
		}
		System.out.println("Total mapped jobs= " + totcnt);
	}

	private void callToMap(final JobDataModelListWrapper jobDataModelListWrapper) {
		try {
			StringEntity jobDataModelListWrapperAsStringEntity = new StringEntity(
					mappingJackson2HttpMessageConverter.getObjectMapper()
							.writeValueAsString(jobDataModelListWrapper),
					ContentType.APPLICATION_JSON);
			JobPortalHttpRequest request = getJobRequestHttpRequestBuilder()
					.requestHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.path(mySqlMapApi.get("path"))
					.httpEntity(jobDataModelListWrapperAsStringEntity).build();
			final HttpResponse response = client.executePost(request);
			if (null != response
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (response.getEntity() != null)
					System.out.println("Response="
							+ mappingJackson2HttpMessageConverter
									.getObjectMapper().readValue(
											response.getEntity().getContent(),
											String.class));
				else
					System.out.println("Null Returned");
			} else {
				System.out.println("Null in else");
			}
		} catch (UnsupportedCharsetException | HttpClientException
				| UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}
	}
}
