package com.portal.job.aggregator.naukri;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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
public class NaukriCrawl {
	@Autowired
	private PulledJobDataModelRepository pulledJobDataModelRepository;
	@Autowired
	private MongoToMySQLAdapterCall mongoToMySQLAdapterCall;

	public void main(String[] args) throws JsonProcessingException {
		// TODO Auto-generated method stub
		getJobs();
	}

	public void getJobs() throws JsonProcessingException {
		System.out.println("new11");
		WebDriver driver = new FirefoxDriver();
		String websiteaddress = "http://jobsearch.naukri.com/java-jobs";
		driver.get(websiteaddress);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		LinkedHashSet<String> uniqueurl = new LinkedHashSet<String>();
		int page = 1;
		Properties properties = new Properties();
		if (uniqueurl.size() == 0) {
			Actions action = new Actions(driver);
			WebElement we = driver
					.findElement(By
							.xpath("html/body/div[4]/div/div[2]/div[1]/div[1]/div/div[2]/div"));
			action.moveToElement(we).perform();
			(driver.findElement(By
					.xpath("//*[@class='list']/li[contains(.,'Date')]")))
					.click();
			System.out.println("Clicked");
			while (true) {
				int fl = 0;
				try {
					List<WebElement> jobs = driver.findElements(By
							.xpath("//*[@type='tuple']"));
					for (WebElement job : jobs) {
						String posted = job
								.findElement(
										By.xpath(".//div/div/span[@class='date']"))
								.getText().trim();
						System.out.println(posted);
						if (posted.contains(" day")) {
							int da = Integer.parseInt(posted.split(" ")[0]);
							if (da > 30) {
								fl = 1;
								break;
							}
						}
						uniqueurl.add(job.findElement(
								By.xpath(".//*[@class='content']"))
								.getAttribute("href"));
						System.out.println(job.findElement(
								By.xpath(".//*[@class='content']"))
								.getAttribute("href"));
					}
					if (fl == 1)
						break;
					if (page == 1) {
						if (driver
								.findElement(By
										.xpath("html/body/div[4]/div/div[2]/div[1]/div[57]/a/button")) == null)
							break;
						else
							driver.findElement(
									By.xpath("html/body/div[4]/div/div[2]/div[1]/div[57]/a/button"))
									.click();
					} else {
						if (driver
								.findElement(By
										.xpath("html/body/div[4]/div/div[2]/div[1]/div[57]/a[3]/button")) == null)
							break;
						else
							driver.findElement(
									By.xpath("html/body/div[4]/div/div[2]/div[1]/div[57]/a[3]/button"))
									.click();
					}
					page++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
			}

			int cn = 0;
			for (Iterator<String> it = uniqueurl.iterator(); it.hasNext();) {
				properties.put(String.valueOf(cn), it.next());
				System.out.println(properties.get(String.valueOf(cn)));
				cn++;
			}
			try {
				properties.store(new FileOutputStream("naukriURL.properties"),
						null);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		int skipped = 0, total = 0, already = 0;
		for (Iterator<String> it = uniqueurl.iterator(); it.hasNext();) {
			try {
				System.out.println("Skipped=" + skipped + " toatal=" + total
						+ " already=" + already);
				String nxturl = it.next();
				String s[] = nxturl.substring(0, nxturl.indexOf("?src")).split(
						"-");
				String jobSourcePortalId = s[s.length - 1];
				if (pulledJobDataModelRepository
						.findOneByjobSourcePortalId(jobSourcePortalId) != null) {
					already++;
					continue;
				}

				driver.get(nxturl);
				String companyName, jobTitle, jobExperience, jobDescription, jobFunction, jobIndustryName, jobSalary, jobSkills, jobOtherSkills, jobLocation, jobCreatedDate, jobRoleCategory;
				companyName = jobTitle = jobExperience = jobDescription = jobFunction = jobIndustryName = jobSkills = jobSalary = jobOtherSkills = jobLocation = jobCreatedDate = jobRoleCategory = "";
				try {
					companyName = driver.findElement(
							By.xpath("//*[@id='jdCpName']")).getText();
					jobTitle = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[1]/div[1]/div/h1"))
							.getText();
					jobExperience = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[1]/div[1]/div/div/span"))
							.getText();
					jobDescription = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[2]/ul[1]"))
							.getText();
					jobRoleCategory = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[2]/div[2]/p[4]/span"))
							.getText();
					try {
						jobFunction = driver
								.findElement(
										By.xpath("html/body/div[2]/div/div[2]/div[2]/div[2]/p[3]/span/a"))
								.getText();
					} catch (Exception e) {
						jobFunction = driver
								.findElement(
										By.xpath("html/body/div[2]/div/div[2]/div[2]/div[2]/p[3]/span"))
								.getText();
					}

					jobIndustryName = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[2]/div[2]/p[2]/span"))
							.getText();
					jobSalary = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[1]/div[3]/span[2]"))
							.getText();
					jobSkills = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[2]/div[3]"))
							.getText();
					jobLocation = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[1]/div[1]/div/div/div/a"))
							.getText();
					String da = driver
							.findElement(
									By.xpath("html/body/div[2]/div/div[2]/div[1]/div[3]/span[3]"))
							.getText();
					if (da.contains(" day")) {
						da = da.substring(da.indexOf("Posted ") + 7);
						int dat;
						dat = Integer.parseInt(da.split(" ")[0]);
						dat = -dat;
						System.out.println("dat=" + dat);
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DATE, dat);
						Date dd = cal.getTime();
						jobCreatedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(dd);
					} else {
						Calendar cal = Calendar.getInstance();
						Date dd = cal.getTime();
						jobCreatedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(dd);
					}

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
					jobDataModel.setJobSourcePortal("naukri");
					jobDataModel.setJobRoleCategory(jobRoleCategory);
					jobDataModel.setParsedToSQL(false);
					this.pulledJobDataModelRepository.save(jobDataModel);
					total++;
					// System.out.println("Parsed job number:--"+total++);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					try {
						companyName = driver.findElement(
								By.xpath("//*[@id='pageC']/div[1]/a[2]"))
								.getAttribute("title");
						jobTitle = driver.findElement(
								By.xpath("//*[@id='jdDiv']/div[1]/div[1]"))
								.getText();
						jobExperience = driver
								.findElement(
										By.xpath("//*[@id='jdDiv']/div[1]/div[2]/p/span[1]"))
								.getText();
						jobDescription = driver
								.findElement(
										By.xpath("//*[@id='apply_career']/div[1]/div[2]"))
								.getText();
						jobSkills = driver
								.findElement(
										By.xpath("//*[@id='apply_career']/div[1]/div[2]/div"))
								.getText();
						jobLocation = driver
								.findElement(
										By.xpath("//*[@id='jdDiv']/div[1]/div[2]/p/span[2]"))
								.getText();
						String da = driver.findElement(
								By.xpath("//*[@id='postDateInfo']")).getText();
						if (da.contains(" day")) {
							da = da.substring(da.indexOf("Posted: ") + 8);
							int dat;
							dat = Integer.parseInt(da.split(" ")[0]);
							dat = -dat;
							System.out.println("dat=" + dat);
							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.DATE, dat);
							Date dd = cal.getTime();
							jobCreatedDate = new SimpleDateFormat("yyyy-MM-dd")
									.format(dd);
						} else {
							Calendar cal = Calendar.getInstance();
							Date dd = cal.getTime();
							jobCreatedDate = new SimpleDateFormat("yyyy-MM-dd")
									.format(dd);
						}

						System.out.println("Title=" + jobTitle);
						System.out.println("jobCreatedDate=" + jobCreatedDate);
						System.out.println("experience=" + jobExperience);
						System.out.println("Location=" + jobLocation);
						System.out.println("jobSkills=" + jobSkills);
						System.out.println("jobSourcePortalId="
								+ jobSourcePortalId);
						System.out.println("jobDescription=" + jobDescription);

						final PulledJobDataModel jobDataModel = new PulledJobDataModel();
						jobDataModel.setJobTitle(jobTitle);
						jobDataModel.setJobSourcePortalId(jobSourcePortalId);
						jobDataModel.setJobComapnyName(companyName);
						jobDataModel.setJobDescription(jobDescription);
						jobDataModel.setJobExperience(jobExperience);
						jobDataModel.setJobSkills(jobSkills);
						jobDataModel.setLocation(jobLocation);
						jobDataModel.setJobCreatedDate(jobCreatedDate);
						jobDataModel.setJobLinkToJobSourcePortal(nxturl);
						jobDataModel.setJobSourcePortal("naukri");
						jobDataModel.setParsedToSQL(false);
						this.pulledJobDataModelRepository.save(jobDataModel);
						total++;
					} catch (Exception e1) {
						System.out.println(e1.getMessage());
						skipped++;
						continue;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
		System.out.println("Now Mapping To Mysql!!!");
		mongoToMySQLAdapterCall.call();
	}
}
