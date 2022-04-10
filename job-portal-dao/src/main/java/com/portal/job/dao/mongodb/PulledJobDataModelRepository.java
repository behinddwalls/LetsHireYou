package com.portal.job.dao.mongodb;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portal.job.dao.mongodb.model.PulledJobDataModel;

import antlr.collections.List;

@Repository
public interface PulledJobDataModelRepository extends MongoRepository<PulledJobDataModel, Serializable> {
	public PulledJobDataModel findOneByjobSourcePortalId(final String jobSourcePortalId);
	
	public java.util.List<PulledJobDataModel> findByParsedToSQL(final boolean parsedToSQL); 

}
