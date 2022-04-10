package com.portal.job.aggregator.careerjet.types;

import java.util.List;

public class CareerJetProxySearchResponse {

    private Type type;
    private String pages;
    private String hits;
    private List<CareerJetJobDataModel> jobs;

    public enum Type {
        JOBS, ERROR;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public List<CareerJetJobDataModel> getJobs() {
        return jobs;
    }

    public void setJobs(List<CareerJetJobDataModel> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return "CareerJetProxySearchResponse [type=" + type + ", pages=" + pages + ", hits=" + hits + ", jobs=" + jobs
                + "]";
    }
}
