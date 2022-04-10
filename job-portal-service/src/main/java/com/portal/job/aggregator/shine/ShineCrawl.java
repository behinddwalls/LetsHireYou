package com.portal.job.aggregator.shine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.portal.job.dao.mongodb.PulledJobDataModelRepository;
import com.portal.job.dao.mongodb.model.PulledJobDataModel;
import com.portal.job.service.aggregator.MongoToMySQLAdapterCall;

@Service
//@EnableScheduling
public class ShineCrawl {

	@Autowired
	private PulledJobDataModelRepository pulledJobDataModelRepository;
	@Autowired
	private MongoToMySQLAdapterCall mongoToMySQLAdapterCall;
	
	//@Scheduled(cron = "0 0 3 * * ?") //Scheduled at 3:00 AM Everyday
	public void getJobs() throws JsonProcessingException {
		/*WebDriver driver = new FirefoxDriver();
		String websiteaddress = "http://www.shine.com/job-search/simple/analytics/";
		driver.get(websiteaddress);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.xpath(".//*[@id='search_content']/div[2]/div[2]/span[2]/select/option[2]")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {

		}
		Set<String> uniqueurl = new HashSet<String>();
		String jobCreatedDate = "";

		int total = 1;
		int fl = 0;
		int pg = 1;
		int toturl = 0;

		WebDriverWait wait = new WebDriverWait(driver, 30);

		while (true) {
			try {
				int urltp = 0;
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search_listing ']")));
				List<WebElement> jobs = driver.findElements(By.xpath("//div[@class='search_listing ']"));
				for (WebElement job : jobs) {
					String url = "";
					try {
						url = job.findElement(By.xpath(".//*[@class='cls_searchresult_a']")).getAttribute("href");
					} catch (Exception e) {
						continue;
					}

					String s[] = url.split("/");
					String id = s[s.length - 2];

					if (pulledJobDataModelRepository.findOneByjobSourcePortalId(id) != null)
						continue;

					toturl++;
					urltp++;

					Map hmp = new HashMap();
					hmp.put("Jan", "01");
					hmp.put("Feb", "02");
					hmp.put("Mar", "03");
					hmp.put("Apr", "04");
					hmp.put("May", "05");
					hmp.put("Jun", "06");
					hmp.put("Jul", "07");
					hmp.put("Aug", "08");
					hmp.put("Sep", "09");
					hmp.put("Oct", "10");
					hmp.put("Nov", "11");
					hmp.put("Dec", "12");

					String dparr[] = job.findElement(By.xpath(".//*[@class='share_links']")).getText().trim()
							.split("-");
					String dd = (dparr[0].split(" "))[2];
					String mm = (String) hmp.get(dparr[1]);
					String yyyy = dparr[2];
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					jobCreatedDate = yyyy + "-" + mm + "-" + dd;
					Date dp = format.parse(jobCreatedDate);
					Date date = new Date();
					String currdt = new SimpleDateFormat("yyyy-MM-dd").format(date);
					Date ndt = format.parse(currdt);
					int daysAgo = (int) TimeUnit.MILLISECONDS.toDays((ndt.getTime() - dp.getTime()));
					if (daysAgo > 30) {
						fl = 1;
						break;
					}
					System.out.println("daysAgo=" + daysAgo);
					System.out.println(url);
					uniqueurl.add(url);
				}
				System.out.println("Total Url this page--->" + urltp + "----------------------------");
				if (fl == 1)
					break;
				pg++;
				System.out.println("Next Page:-->" + pg + "-----------------------------------------");
				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li[" + pg + "]/a")));
					driver.findElement(
							By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li[" + pg + "]/a"))
							.click();
				} catch (Exception e) {
					Thread.sleep(10000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li[" + pg + "]/a")));
					driver.findElement(
							By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li[" + pg + "]/a"))
							.click();
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		System.out.println("Total url: " + toturl + "\n");

		for (Iterator<String> it = uniqueurl.iterator(); it.hasNext();) {
			try {

				String jobLinkToJobSourcePortal = it.next();
				String s[] = jobLinkToJobSourcePortal.split("/");
				String jobSourcePortalId = s[s.length - 1];

				driver.get(jobLinkToJobSourcePortal);
				String companyName, jobTitle, jobExperience, jobDescription, jobFunction, jobIndustryName, jobSalary,
						jobSkills;
				companyName = jobTitle = jobExperience = jobDescription = jobFunction = jobIndustryName = jobSkills = jobSalary = "";
				// try
				{
					companyName = driver
							.findElement(
									By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[1]/a/span/span/h2"))
							.getText();
				}
				// catch(Exception e)
				{
				}
				// try
				{
					jobTitle = driver.findElement(By.xpath("//h1[@itemprop='title']")).getText();
				}
				// catch(Exception e)
				{
				}
				// try
				{
					jobExperience = driver.findElement(By.xpath("//span[@itemprop='experienceRequirements']"))
							.getText();
				}
				// catch(Exception e)
				{
				}
				// try
				{
					jobDescription = driver.findElement(By.xpath("//span[@itemprop='description']")).getText();
				}
				// catch(Exception e)
				{
				}
				// try
				{
					jobFunction = driver.findElement(By.xpath("//span[@itemprop='occupationalCategory']")).getText();
				}
				// catch(Exception e)
				{
				}
				// try
				{
					jobIndustryName = driver.findElement(By.xpath("//span[@itemprop='industry']")).getText();
				}
				// catch(Exception e)
				{
				}
				try {
					jobSalary = driver
							.findElement(
									By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[2]/ul/li[2]/i/span"))
							.getText();
				} catch (Exception e) {
				}
				jobSkills = "";
				try {
					jobSkills += driver
							.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[3]/ul/li/i"))
							.getText() + "\n";
				} catch (Exception e) {
				}
				try {
					jobSkills += driver
							.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[4]/ul/li/i"))
							.getText();
				} catch (Exception e) {
				}
				String location = "";
				try {
					location = driver
							.findElement(By
									.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[2]/ul/li[2]/span[2]/div/span[2]/span.text()"))
							.getText();
				} catch (Exception e) {
					location = driver.findElement(By.xpath(".//*[@itemprop='jobLocation']/span[@itemprop='name']"))
							.getText();
				}

				System.out.println("jobSourcePortalId=" + jobSourcePortalId);
				System.out.println("Title=" + jobTitle);
				System.out.println("jobCreatedDate=" + jobCreatedDate);// --
				System.out.println("experience=" + jobExperience);
				System.out.println("Location=" + location);// --
				System.out.println("jobSkills=" + jobSkills);
				System.out.println("jobDescription=" + jobDescription);
				System.out.println("jobFunction=" + jobFunction);
				System.out.println("Industry=" + jobIndustryName);
				System.out.println("jobSalary=" + jobSalary);

				final PulledJobDataModel jobDataModel = new PulledJobDataModel();

				jobDataModel.setJobSourcePortal("www.shine.com");
				jobDataModel.setJobLinkToJobSourcePortal(jobLinkToJobSourcePortal);
				jobDataModel.setJobTitle(jobTitle);
				jobDataModel.setJobSourcePortalId(jobSourcePortalId);
				jobDataModel.setJobComapnyName(companyName);
				jobDataModel.setJobDescription(jobDescription);
				jobDataModel.setJobExperience(jobExperience);
				jobDataModel.setJobFunction(jobFunction);
				jobDataModel.setJobIndustryName(jobIndustryName);
				jobDataModel.setJobSkills(jobSkills);
				jobDataModel.setLocation(location);
				jobDataModel.setJobCreatedDate(jobCreatedDate);
				jobDataModel.setJobSalary(jobSalary);
				jobDataModel.setParsedToSQL(false);
				this.pulledJobDataModelRepository.save(jobDataModel);

				System.out.println("Parsed job number:--" + total++);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				break;
			}
		}
		System.out.println("total=" + total);*/
		System.out.println("Now mapping to mysql");
		mongoToMySQLAdapterCall.call();
	}
}
