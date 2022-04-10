package com.portal.job.dao.mongodb;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

/**
 * This class provide and interface to generic mongo operations using
 * MongoTemplate.
 * 
 * @author behinddwalls
 *
 * @param <T>
 * @param <I>
 */
@Repository
public class JobPortalMongoTemplateRepositoryImpl<T, I extends Serializable>
		implements JobPortalMongoTemplateRepository<T, I> {

	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.portal.job.dao.mongodb.IJobPortalMongoRepository#upsert(org.
	 * springframework.data.mongodb.core.query.Query,
	 * org.springframework.data.mongodb.core.query.Update, java.lang.Class)
	 */
	public WriteResult upsert(final Query query, final Update update,
			final Class<T> entityClass) {

		WriteResult writeResult = mongoTemplate.upsert(query, update,
				entityClass);
		return writeResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.portal.job.dao.mongodb.IJobPortalMongoRepository#updateMulti(org.
	 * springframework.data.mongodb.core.query.Query,
	 * org.springframework.data.mongodb.core.query.Update, java.lang.Class)
	 */
	public WriteResult updateMulti(final Query query, final Update update,
			final Class<T> entityClass) {
		WriteResult writeResult = mongoTemplate.updateMulti(query, update,
				entityClass);
		return writeResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.portal.job.dao.mongodb.IJobPortalMongoRepository#findAndModify(org
	 * .springframework.data.mongodb.core.query.Query,
	 * org.springframework.data.mongodb.core.query.Update,
	 * org.springframework.data.mongodb.core.FindAndModifyOptions,
	 * java.lang.Class)
	 */
	public T findAndModify(final Query query, final Update update,
			final FindAndModifyOptions options, final Class<T> entityClass) {
		T result = mongoTemplate.findAndModify(query, update, options,
				entityClass);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.portal.job.dao.mongodb.IJobPortalMongoRepository#updateFirst(org.
	 * springframework.data.mongodb.core.query.Query,
	 * org.springframework.data.mongodb.core.query.Update, java.lang.Class)
	 */
	public WriteResult updateFirst(final Query query, final Update update,
			final Class<T> entityClass) {
		WriteResult writeResult = mongoTemplate.updateFirst(query, update,
				entityClass);
		return writeResult;
	}
}
