package com.portal.job.service.aggregator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.portal.job.aggregator.careerjet.proxy.CareerJetAggregatorProxy;
import com.portal.job.aggregator.careerjet.types.CareerJetJobDataModel;
import com.portal.job.aggregator.careerjet.types.CareerJetProxySearchRequest;
import com.portal.job.aggregator.careerjet.types.CareerJetProxySearchResponse;
import com.portal.job.dao.mongodb.PulledJobDataModelRepository;
import com.portal.job.dao.mongodb.model.PulledJobDataModel;

@Service
public class CareerJetJobAggregatorService {

    private static final Logger log = LoggerFactory.getLogger(CareerJetJobAggregatorService.class);
    @Autowired
    private CareerJetAggregatorProxy careerJetAggregatorProxy;
    @Autowired
    private PulledJobDataModelRepository pulledJobDataModelRepository;

    public void aggregateJobs(final String keyword, final String location) {

        try {
            CareerJetProxySearchRequest.Builder requestBuilder = new CareerJetProxySearchRequest.Builder(keyword,
                    location);
            CareerJetProxySearchResponse response = this.careerJetAggregatorProxy.search(requestBuilder.build());
            log.debug("CareerJet Search Response-1 - ", response);
            int totalPages = 100;
            if (Integer.valueOf(response.getPages()) < 100) {
                totalPages = Integer.valueOf(response.getPages());
            }
            // paginate for jobs
            for (int page = 1; page <= totalPages; page++) {
                requestBuilder.pageNumber(page);
                CareerJetProxySearchResponse innerResponse = this.careerJetAggregatorProxy.search(requestBuilder
                        .build());
                preparePulledJobDataModelsAndSaveToDB(innerResponse, keyword);
            }

        } catch (Exception e) {
            log.error("Failed to aggregate ", e);
        }
    }

    private List<PulledJobDataModel> preparePulledJobDataModelsAndSaveToDB(final CareerJetProxySearchResponse response,
            final String keyword) {
        final List<PulledJobDataModel> pulledJobDataModels = Lists.newArrayList();
        if (response != null && response.getType().equals(CareerJetProxySearchResponse.Type.JOBS)) {

            for (CareerJetJobDataModel careerJetJobDataModel : response.getJobs()) {
                final PulledJobDataModel jobDataModel = new PulledJobDataModel();

                jobDataModel.setJobComapnyName(careerJetJobDataModel.getCompany());
                jobDataModel.setJobDescription(careerJetJobDataModel.getDescription());
                jobDataModel.setJobFunction(keyword);
                jobDataModel.setJobIndustryName(keyword);

                jobDataModel.setLocation(careerJetJobDataModel.getLocations());
                jobDataModel.setJobSalary(careerJetJobDataModel.getSalary());
                jobDataModel.setJobSourcePortal(careerJetJobDataModel.getSite());

                jobDataModel.setJobTitle(careerJetJobDataModel.getTitle());
                jobDataModel.setJobModifiedDate(careerJetJobDataModel.getDate());
                jobDataModel.setJobCreatedDate(careerJetJobDataModel.getDate());

                // save to db
                this.pulledJobDataModelRepository.save(jobDataModel);

            }
        }
        return pulledJobDataModels;

    }
}
