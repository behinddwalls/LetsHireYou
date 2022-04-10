package com.portal.job.model;

import java.util.Optional;

public class ExperienceProcessedData {
    private int expMonthsIncludingCurrent;
    private Optional<String> latestCompanyName;
    private Optional<String> latestJobTitle;

    public ExperienceProcessedData(int expMonthsIncludingCurrent, Optional<String> latestCompanyName) {
        super();
        this.expMonthsIncludingCurrent = expMonthsIncludingCurrent;
        this.latestCompanyName = latestCompanyName;
    }

    public int getExpMonthsIncludingCurrent() {
        return expMonthsIncludingCurrent;
    }

    public void setExpMonthsIncludingCurrent(int expMonthsIncludingCurrent) {
        this.expMonthsIncludingCurrent = expMonthsIncludingCurrent;
    }

    public Optional<String> getLatestCompanyName() {
        return latestCompanyName;
    }

    public void setLatestCompanyName(Optional<String> latestCompanyName) {
        this.latestCompanyName = latestCompanyName;
    }

    public Optional<String> getLatestJobTitle() {
        return latestJobTitle;
    }

    public void setLatestJobTitle(Optional<String> latestJobTitle) {
        this.latestJobTitle = latestJobTitle;
    }

}
