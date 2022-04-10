package com.portal.job.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.job.dao.mongodb.PulledJobDataModelRepository;

@RequestMapping("api/google")
@Controller
public class GoogleSearchCrawler {

    @Autowired
    private PulledJobDataModelRepository pulledJobDataModelRepository;

    @RequestMapping
    public String test() {

        // System.setProperty("webdriver.chrome.driver",
        // "/Users/preetam/Downloads/chromedriver");

        // String userProfile =
        // "/Users/preetam/Library/Application Support/Google/Chrome/Default";
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("user-data-dir=" + userProfile);
        // options.addArguments("--start-maximized");
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&es_th=1&ie=UTF-8#q=bangalore+%2B+software+engineer+%2B+pub+site:linkedin.com&start=0");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        int i = 1;
        int j = 0;
        try {
            Thread.sleep(20000);

            driver.get("https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&es_th=1&ie=UTF-8#q=bangalore+%2B+software+engineer+%2B+pub+site:linkedin.com&start=0");
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Thread.sleep(10000);
        } catch (InterruptedException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        for (i = 1; i <= 11; i++) {
            boolean hasResult = false;
            try {
                if (i == 11) {
                    hasResult = true;
                    throw new Exception("Next Page");
                }

                WebElement element = driver.findElement(By.xpath(".//*[@id='rso']/div/div/div[" + i + "]/div/h3/a"));
                System.out.println(element.getAttribute("href"));
                hasResult = true;
                String savestr = "/Users/preetam/google-linkedin-profile-software-engineer-bangalore.txt";
                File f = new File(savestr);

                PrintWriter out = null;
                if (f.exists() && !f.isDirectory()) {
                    out = new PrintWriter(new FileOutputStream(new File(savestr), true));
                    out.append(element.getAttribute("href") + System.getProperty("line.separator"));
                    out.close();
                } else {
                    out = new PrintWriter(savestr);
                    out.println(element.getAttribute("href") + System.getProperty("line.separator"));
                    out.close();
                }

            } catch (Exception e) {
                if (!hasResult) {
                    System.out.println("Braking...");
                    break;
                }

                j = j + 10;
                driver.get("https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&es_th=1&ie=UTF-8#q=bangalore+%2B+software+engineer+%2B+pub+site:linkedin.com&start="
                        + j);
                i = 0;
                System.out.println("Page No." + (j / 10 + 1));
                try {
                    Thread.sleep(2000);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        return "";
    }
}
