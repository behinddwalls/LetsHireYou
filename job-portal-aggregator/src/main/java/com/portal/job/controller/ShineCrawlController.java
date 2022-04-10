package com.portal.job.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portal.job.aggregator.shine.ShineCrawl;

@Controller
@RequestMapping("/api")
public class ShineCrawlController {

    @Autowired
    private ShineCrawl shineCrawl;

    @RequestMapping("shine")
    public @ResponseBody String getShineCrawl() {
        shineCrawl.getJobs();
        return StringUtils.EMPTY;
    }

}
