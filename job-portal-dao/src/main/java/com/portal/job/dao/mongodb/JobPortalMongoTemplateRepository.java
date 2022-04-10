package com.portal.job.dao.mongodb;

import java.io.Serializable;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

/**
 * @author behinddwalls
 *
 * @param <T>
 * @param <I>
 */
public interface JobPortalMongoTemplateRepository<T, I extends Serializable> {

	/**
	 * If document is matched, update it, else create a new document by
	 * combining the query and update object, it’s works like
	 * findAndModifyElseCreate() Cheers :)
	 * 
	 * @param query
	 * @param update
	 * @param entityClass
	 * @return
	 */
	public WriteResult upsert(final Query query, final Update update,
			final Class<T> entityClass);

	/**
	 * Updates all documents that matches the query.
	 * 
	 * @param query
	 * @param update
	 * @param entityClass
	 * @return
	 */
	public WriteResult updateMulti(final Query query, final Update update,
			final Class<T> entityClass);

	/**
	 * Find and modify and get the newly updated object from a single operation.
	 * 
	 * @param query
	 * @param update
	 * @param options
	 *            {@link FindAndModifyOptions} <li>
	 *            FindAndModifyOptions().returnNew(true) = newly updated
	 *            document</li><li>
	 *            FindAndModifyOptions().returnNew(false) = old document (not
	 *            update yet)</li>
	 * @param entityClass
	 * @return
	 */
	public T findAndModify(final Query query, final Update update,
			final FindAndModifyOptions options, final Class<T> entityClass);

	/**
	 * Updates the first document that matches the query. In this case, only the
	 * single field “age” is updated.
	 * 
	 * <pre>
	 * // returns only 'name' field
	 * Query query = new Query();
	 * query.addCriteria(Criteria.where(&quot;name&quot;).is(&quot;appleC&quot;));
	 * query.fields().include(&quot;name&quot;);
	 * 
	 * User userTest3 = mongoOperation.findOne(query, User.class);
	 * System.out.println(&quot;userTest3 - &quot; + userTest3);
	 * 
	 * Update update = new Update();
	 * update.set(&quot;age&quot;, 100);
	 * 
	 * repo.updateFirst(query, update, User.class);
	 * 
	 * // returns everything
	 * Query query1 = new Query();
	 * query1.addCriteria(Criteria.where(&quot;name&quot;).is(&quot;appleC&quot;));
	 * 
	 * User userTest3_1 = repo.findOne(query1, User.class);
	 * System.out.println(&quot;userTest3_1 - &quot; + userTest3_1);
	 * </pre>
	 * 
	 * @param query
	 * @param update
	 * @param entityClass
	 * @return
	 */
	public WriteResult updateFirst(final Query query, final Update update,
			final Class<T> entityClass);

}
