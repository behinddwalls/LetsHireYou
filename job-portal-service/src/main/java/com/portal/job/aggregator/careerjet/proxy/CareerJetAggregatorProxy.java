package com.portal.job.aggregator.careerjet.proxy;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

//import com.careerjet.webservice.api.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.portal.job.aggregator.careerjet.types.CareerJetProxySearchRequest;
import com.portal.job.aggregator.careerjet.types.CareerJetProxySearchResponse;
import com.portal.job.exception.CareerJetProxySearchException;

@Component
public class CareerJetAggregatorProxy {

    private Logger log = LoggerFactory.getLogger(CareerJetAggregatorProxy.class);
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    // private Client client;

    public CareerJetAggregatorProxy() {
        // this.client = new Client("en_IN");
    }

    public CareerJetProxySearchResponse search(final CareerJetProxySearchRequest request)
            throws CareerJetProxySearchException {
        Map<?, ?> responseMap = null;// client.search(prepareRequest(request));
        try {
            return getObjectMapper().readValue(getObjectMapper().writeValueAsString(responseMap),
                    CareerJetProxySearchResponse.class);
        } catch (IOException e) {
            log.error("Failed to search - careerjet", e);
            throw new CareerJetProxySearchException(e);
        }
    }

    private Map<String, String> prepareRequest(final CareerJetProxySearchRequest request) {
        final Map<String, String> argsMap = Maps.newHashMap();
        argsMap.put("keywords", request.getKeyword());
        argsMap.put("location", request.getLocation());
        argsMap.put("sort", request.getSortOrder().name().toLowerCase());

        argsMap.put("affid", request.getAffiliateId());

        argsMap.put("user_ip", request.getIpAddress());
        argsMap.put("user_agent", request.getUserAgent());
        argsMap.put("url", request.getRequesterUrl());

        // pagination
        argsMap.put("pagesize", Integer.toString(request.getPageSize()));
        argsMap.put("page", Integer.toString(request.getPageNumber()));

        return argsMap;
    }

    private ObjectMapper getObjectMapper() {
        return this.mappingJackson2HttpMessageConverter.getObjectMapper();
    }

}
