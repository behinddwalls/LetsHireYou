package com.portal.job.aggregator.times;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.dao.mongodb.PulledJobDataModelRepository;
import com.portal.job.dao.mongodb.model.PulledJobDataModel;
import com.portal.job.service.aggregator.MongoToMySQLAdapterCall;

@Service
public class TimesCrawl {
	@Autowired
	private PulledJobDataModelRepository pulledJobDataModelRepository;
	@Autowired
	private MongoToMySQLAdapterCall mongoToMySQLAdapterCall;

	public void main(String[] args) throws JsonProcessingException {
		// TODO Auto-generated method stub
		getJobs();
	}

	public void getJobs() throws JsonProcessingException  {
		System.out.println("new11");
		WebDriver driver = new FirefoxDriver();
		String websiteaddress = "http://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=analytics&txtLocation=";
		driver.get(websiteaddress);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		LinkedHashSet<String> uniqueurl = new LinkedHashSet<String>();
		Map hmp = new HashMap();
		hmp.put("Jan,", "01");
		hmp.put("Feb,", "02");
		hmp.put("Mar,", "03");
		hmp.put("Apr,", "04");
		hmp.put("May,", "05");
		hmp.put("Jun,", "06");
		hmp.put("Jul,", "07");
		hmp.put("Aug,", "08");
		hmp.put("Sep,", "09");
		hmp.put("Oct,", "10");
		hmp.put("Nov,", "11");
		hmp.put("Dec,", "12");
		int page = 1;
		Properties properties = new Properties();
		driver.findElement(By.xpath("//*[@id='closeId']")).click();
		if (uniqueurl.size() == 0) {
			while (true) {
				int fl = 0;
				try {
					List<WebElement> jobs = driver.findElements(By
							.xpath("//*[@class='clearfix joblistli']"));
					for (WebElement job : jobs) {
						uniqueurl.add(job.findElement(By.xpath(".//*[@class='job-action clearfix']/span/a")).getAttribute("href"));
						System.out.println(job.findElement(By.xpath(".//*[@class='job-action clearfix']/span/a")).getAttribute("href"));
						fl=1;
					}
					if (page == 200)
						break;
					page++;
					//if(page%10!=1)
					driver.findElement(By.xpath("//*[@id='searchResultData']/div[2]/div/em["+page+"]/a")).click();
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
					break;
				}}}
			int cn = 0;
			for (Iterator<String> it = uniqueurl.iterator(); it.hasNext();) {
				properties.put(String.valueOf(cn), it.next());
				System.out.println(properties.get(String.valueOf(cn)));
				cn++;
			}
			try {
				properties.store(new FileOutputStream("timesURL.properties"),
						null);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		int skipped = 0, total = 0, already = 0;
		for (Iterator<String> it = uniqueurl.iterator(); it.hasNext();) {
			try {
				System.out.println("Skipped=" + skipped + " toatal=" + total
						+ " already=" + already);
				String nxturl = it.next();
				String s[] = nxturl.substring(0, nxturl.indexOf("&bc")).split(
						"=");
				String jobSourcePortalId = s[s.length - 1];
				System.out.println("JobSourcePortalId=" + jobSourcePortalId);
				System.out.println(nxturl);
				/*if (pulledJobDataModelRepository
						.findOneByjobSourcePortalId(jobSourcePortalId) != null) {
					already++;
					continue;
				}*/

				driver.get(nxturl);
				String companyName, jobTitle, jobExperience, jobDescription, jobFunction, jobIndustryName, jobSalary, jobSkills, jobOtherSkills, jobLocation, jobCreatedDate, jobRoleCategory;
				companyName = jobTitle = jobExperience = jobDescription = jobFunction = jobIndustryName = jobSkills = jobSalary = jobOtherSkills = jobLocation = jobCreatedDate = jobRoleCategory = "";
					companyName = driver.findElement(
							By.xpath("//*[@id='site']/div[6]/section/div[1]/section[1]/header/span")).getText();
					jobTitle = driver
							.findElement(
									By.xpath("//*[@id='site']/div[6]/section/div[1]/section[1]/header/h1"))
							.getText();
					jobExperience = driver
							.findElement(
									By.xpath("//*[@id='applyFlowHideDetails_1']/li[1]/span"))
							.getText();
					jobDescription = driver
							.findElement(
									By.xpath("//*[@id='applyFlowHideDetails_2']"))
							.getText();
					jobRoleCategory = driver
							.findElement(
									By.xpath("//*[@id='applyFlowHideDetails_1']/li[5]/span"))
							.getText();
					jobFunction = jobRoleCategory;

					jobIndustryName = driver
							.findElement(
									By.xpath(".//*[@id='applyFlowHideDetails_1']/li[6]/span"))
							.getText();
					jobSalary = driver
							.findElement(
									By.xpath(".//*[@id='applyFlowHideDetails_1']/li[2]/span"))
							.getText();
					jobSkills = driver
							.findElement(
									By.xpath(".//*[@id='applyFlowHideDetails_1']/li[4]/span"))
							.getText();
					jobLocation = driver
							.findElement(
									By.xpath(".//*[@id='applyFlowHideDetails_1']/li[3]/span"))
							.getText();
					String da = driver
							.findElement(
									By.xpath(".//*[@id='applyFlowHideDetails_6']/div/span[1]"))
							.getText();
						da = da.substring(da.indexOf("Posted on: ") + 11);
						System.out.println("da=" + da);
						String tmd[]=da.split(" ");
						String dd = tmd[0];
						String mm = (String) hmp.get(tmd[1]);
						String yyyy = tmd[2];
						jobCreatedDate = yyyy + "-" + mm + "-" + dd;

					System.out.println("Title=" + jobTitle);
					System.out.println("jobCreatedDate=" + jobCreatedDate);// --
					System.out.println("experience=" + jobExperience);
					System.out.println("Location=" + jobLocation);// --
					System.out.println("jobSkills=" + jobSkills);
					System.out.println("jobOtherSkills=" + jobOtherSkills);
					System.out.println("jobDescription=" + jobDescription);
					System.out.println("jobFunction=" + jobFunction);
					System.out.println("Industry=" + jobIndustryName);
					System.out.println("jobSalary=" + jobSalary);
					System.out
							.println("jobSourcePortalId=" + jobSourcePortalId);

					final PulledJobDataModel jobDataModel = new PulledJobDataModel();
					jobDataModel.setJobTitle(jobTitle);
					jobDataModel.setJobSourcePortalId(jobSourcePortalId);
					jobDataModel.setJobComapnyName(companyName);
					jobDataModel.setJobExperience(jobExperience);
					jobDataModel.setJobFunction(jobFunction);
					jobDataModel.setJobIndustryName(jobIndustryName);
					jobDataModel.setJobSkills(jobSkills);
					jobDataModel.setLocation(jobLocation);
					jobDataModel.setJobDescription(jobDescription);
					jobDataModel.setJobCreatedDate(jobCreatedDate);
					jobDataModel.setJobSalary(jobSalary);
					jobDataModel.setJobLinkToJobSourcePortal(nxturl);
					jobDataModel.setJobSourcePortal("times");
					jobDataModel.setJobRoleCategory(jobRoleCategory);
					jobDataModel.setParsedToSQL(false);
					this.pulledJobDataModelRepository.save(jobDataModel);
					total++;
					// System.out.println("Parsed job number:--"+total++);
				 
			} catch (Exception e) {
				System.out.println("mm="+e.getMessage());
				continue;
			}
		}
		System.out.println("Now Mapping To Mysql!!!");
		mongoToMySQLAdapterCall.call();
	}
}
