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
import org.springframework.stereotype.Service;

import com.portal.job.dao.mongodb.PulledJobDataModelRepository;
import com.portal.job.dao.mongodb.model.PulledJobDataModel;

@Service
public class ShineCrawl {

	@Autowired
	private PulledJobDataModelRepository pulledJobDataModelRepository;
	public void getJobs() {
		// TODO Auto-generated method stub
		WebDriver driver=new FirefoxDriver();
		String websiteaddress = "http://www.shine.com/job-search/simple/analytics/";
		driver.get(websiteaddress);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath(".//*[@id='search_content']/div[2]/div[2]/span[2]/select/option[2]")).click();
        try 
        {
			Thread.sleep(1000);
		} 
        catch (InterruptedException e1) 
        {
        	
		}
        Set<String> uniqueurl = new HashSet<String>();
        String jobCreatedDate="";
        
        int total=1;
        int fl=0;
        int pg=1;
        int toturl=0;
        
        WebDriverWait wait = new WebDriverWait(driver,30);
        
        while(true)
        {
        	try
        	{
        		int urltp=0;
        		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search_listing ']")));
        		List<WebElement> jobs=driver.findElements(By.xpath("//div[@class='search_listing ']"));
        		for (WebElement job : jobs) 
        		{
        			String url="";
        			try
        			{
        				url=job.findElement(By.xpath(".//*[@class='cls_searchresult_a']")).getAttribute("href");
        			}
        			catch(Exception e)
        			{
        				continue;
        			}
        			
        			toturl++;
        			urltp++;
            		String s[]=url.split("/");
            		String id=s[s.length-2];
            		
            		if(pulledJobDataModelRepository.findOneByjobSourcePortalId(id)!=null)
            			continue;
            		
        			Map hmp=new HashMap();
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
        			
        			String dparr[]=job.findElement(By.xpath(".//*[@class='share_links']")).getText().trim().split("-");
        			String dd=(dparr[0].split(" "))[2];
        			String mm=(String) hmp.get(dparr[1]);
        			String yyyy=dparr[2];
        			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        			jobCreatedDate=yyyy+"-"+mm+"-"+dd;
        			Date dp=format.parse(jobCreatedDate);
        			Date date=new Date();
        			String currdt=new SimpleDateFormat("yyyy-MM-dd").format(date);
        			Date ndt=format.parse(currdt);
        			
        			if(TimeUnit.MILLISECONDS.toDays((ndt.getTime()-dp.getTime()))>30)
        			{
        				fl=1;
        				break;
        			}
        			
        			System.out.println(url);
        			uniqueurl.add(url);
        		}
        		System.out.println("Total Url this page--->"+urltp+"----------------------------");
        		if(fl==1)
        			break;
        		pg++;
        		if(pg==6)
        			break;
        		System.out.println("Next Page:-->"+pg+"-----------------------------------------");
        		try
        		{
        			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li["+pg+"]/a")));
        			driver.findElement(By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li["+pg+"]/a")).click();
        		}
        		catch(Exception e)
        		{
        			Thread.sleep(10000);
        			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li["+pg+"]/a")));
        			driver.findElement(By.xpath(".//*[@id='search_content']/div[3]/div[2]/div[53]/div/div[2]/ul/li["+pg+"]/a")).click();
        		}
        		Thread.sleep(10000);
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		continue;
        	}
        }
        
        System.out.println("Total url: "+toturl+"\n");
        
        for (Iterator<String> it = uniqueurl.iterator(); it.hasNext(); ) 
        {
        	try
        	{
        		
        		String nxturl=it.next();
        		String s[]=nxturl.split("/");
        		String jobSourcePortalId=s[s.length-2];
        		
        		driver.get(nxturl);
        		String companyName,jobTitle,jobExperience,jobDescription,jobFunction,jobIndustryName,jobSalary,jobSkills,jobOtherSkills;
        		companyName=jobTitle=jobExperience=jobDescription=jobFunction=jobIndustryName=jobSkills=jobSalary=jobOtherSkills="";
        		try
        		{
        			companyName=driver.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[1]/a/span/span/h2")).getText();
        			jobTitle=driver.findElement(By.xpath("//h1[@itemprop='title']")).getText();
        			jobExperience=driver.findElement(By.xpath("//span[@itemprop='experienceRequirements']")).getText();
        			jobDescription=driver.findElement(By.xpath("//span[@itemprop='description']")).getText();
            		jobFunction=driver.findElement(By.xpath("//span[@itemprop='occupationalCategory']")).getText();
            		jobIndustryName=driver.findElement(By.xpath("//span[@itemprop='industry']")).getText();
            		jobSalary=driver.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[2]/ul/li[2]/i/span")).getText();
            		jobSkills="";
            		
            		int fljs=0;
            		
            		try
            		{
            			int cnt=1;
            			while(true)
            			{
            				jobSkills+=driver.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[3]/ul/li/i/span["+cnt+"]/a")).getText()+"\n";
            				cnt++;
            			}
            		}
            		catch (Exception e)
            		{
            			fljs=1;
            		}
            		try
            		{
            			int cnt=1;
            			while(true)
            			{
            				jobOtherSkills+=driver.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[4]/ul/li/i/span["+cnt+"]/a")).getText()+"\n";
            				cnt++;
            			}
            		}
            		catch (Exception e)
            		{
            			if(fljs==1)
            			{
            				fljs=0;
            				continue;
            			}
            		}
        		}
        		catch(Exception e)
        		{
        			continue;
        		}
        		String location="";
        		try
        		{
        			location=driver.findElement(By.xpath("html/body/div[2]/div[4]/div[3]/div[2]/div/div[1]/div[2]/ul/li[2]/span[2]/div/span[2]/span.text()")).getText();
        		}
        		catch(Exception e)
        		{
        			location=driver.findElement(By.xpath(".//*[@itemprop='jobLocation']/span[@itemprop='name']")).getText();
        		}
        		
        		System.out.println("Title="+jobTitle);
       	 		System.out.println("jobCreatedDate="+jobCreatedDate);//--
       	 		System.out.println("experience="+jobExperience);
       	 		System.out.println("Location="+location);//--
       	 		System.out.println("jobSkills="+jobSkills);
       	 		System.out.println("jobOtherSkills="+jobOtherSkills);
       	 		System.out.println("jobDescription="+jobDescription);
       	 		System.out.println("jobFunction="+jobFunction);
       	 		System.out.println("Industry="+jobIndustryName);
       	 		System.out.println("jobSalary="+jobSalary);
       	 		
       	 		final PulledJobDataModel jobDataModel=new PulledJobDataModel();

       	 		jobDataModel.setJobSourcePortalId(jobSourcePortalId);
       	 		jobDataModel.setJobComapnyName(companyName);
       	 		jobDataModel.setJobDescription(jobDescription);
       	 		jobDataModel.setJobExperience(jobExperience);
       	 		jobDataModel.setJobFunction(jobFunction);
       	 		jobDataModel.setJobIndustryName(jobIndustryName);
       	 		jobDataModel.setJobSkills(jobSkills);
       	 		jobDataModel.setLocation(location);
       	 		jobDataModel.setJobDescription(jobDescription);
       	 		jobDataModel.setJobCreatedDate(jobCreatedDate);
       	 		jobDataModel.setJobSalary(jobSalary);
       	 		this.pulledJobDataModelRepository.save(jobDataModel);
       	 		
       	 		System.out.println("Parsed job number:--"+total++);
        	}
        	catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        		break;
        	}
        }
        System.out.println("total="+total);
	}
}
