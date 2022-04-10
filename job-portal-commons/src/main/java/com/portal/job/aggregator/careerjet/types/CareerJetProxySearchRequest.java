package com.portal.job.aggregator.careerjet.types;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public final class CareerJetProxySearchRequest {

    private static final String ipAdressesPrefix = "1.2.3.";
    private static final String AFFLIATE_ID = "bc01a6910086364aeb158887338ca041";
    private static final String DEFAULT_USER_AGENT = "Mozilla";
    private static final String DEFAULT_REQUEST_URL = "http://evenrank.com";

    private final String ipAddress;
    private final String userAgent;
    private final String affiliateId;
    private final String requesterUrl;
    private final String keyword;
    private final String location;
    private final int pageSize;
    private final int pageNumber;
    private final SortOrder sortOrder;

    public static class Builder {
        private String ipAddress;
        private String userAgent;
        private String affiliateId;
        private String requesterUrl;
        private int pageSize = 50;
        private int pageNumber = 1;
        private SortOrder sortOrder;

        // required
        private String keyword;
        private String location;

        public Builder(final String keyword, final String location) {
            if (StringUtils.isEmpty(keyword) || StringUtils.isEmpty(location)) {
                throw new IllegalArgumentException("Keyword or location can not be null");
            }
            this.keyword = keyword;
            this.location = location;
        }

        public Builder pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder ipAddress(final String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder affiliateId(final String affiliateId) {
            this.affiliateId = affiliateId;
            return this;
        }

        public Builder userAgent(final String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder pageNumber(final int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder sortOrder(final SortOrder sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public CareerJetProxySearchRequest build() {
            return new CareerJetProxySearchRequest(this);
        }
    }

    public CareerJetProxySearchRequest(final Builder builder) {
        this.location = builder.location;
        this.keyword = builder.keyword;
        this.requesterUrl = builder.requesterUrl == null ? DEFAULT_REQUEST_URL : builder.requesterUrl;
        this.ipAddress = builder.ipAddress == null ? getRandomIpAddress() : builder.ipAddress;
        this.affiliateId = builder.affiliateId == null ? AFFLIATE_ID : builder.affiliateId;
        this.userAgent = builder.userAgent == null ? DEFAULT_USER_AGENT : builder.userAgent;
        this.pageSize = builder.pageSize;
        this.pageNumber = builder.pageNumber;
        this.sortOrder = builder.sortOrder == null ? SortOrder.DATE : builder.sortOrder;
    }

    private String getRandomIpAddress() {
        final Random random = new Random();
        int randomNumber = random.nextInt(100);
        return ipAdressesPrefix + Integer.toString(randomNumber);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public String getRequesterUrl() {
        return requesterUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLocation() {
        return location;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public enum SortOrder {
        DATE, RELEVANCE, SALARY;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return "CareerJetProxySearchRequest [ipAddress=" + ipAddress + ", userAgent=" + userAgent + ", affiliateId="
                + affiliateId + ", requesterUrl=" + requesterUrl + ", keyword=" + keyword + ", location=" + location
                + ", pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", sortOrder=" + sortOrder + "]";
    }

}
