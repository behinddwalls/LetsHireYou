package com.portal.job.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;

@Repository
public class JobPortalBaseDao<T, ID extends Serializable> extends
		GenericDAOImpl<T, ID> {
	@Autowired
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * to be used if we are to allow an entity edited across multiple sessions
	 * to be saved.
	 * 
	 * @param entity
	 * @return
	 */
	public T mergeEntity(T entity) {
		return this._merge(entity);
	}

	/**
	 * 
	 * @param entityPropertyMap
	 * @param clazz
	 * @return put the 'Equal' operator for a property.
	 */
	public List<T> getEntitiesByPropertyValue(
			Map<String, ? extends Object> entityPropertyMap, Class<T> clazz) {
		Search search = new Search(clazz);
		for (String key : entityPropertyMap.keySet()) {
			search.addFilterEqual(key, entityPropertyMap.get(key));
		}
		return this.search(search);
	}

	public List<T> getEntitiesByPropertyValue(
			Map<String, ? extends Object> entityPropertyMap, Class<T> clazz,
			int pageNumber, int pageSize) {
		Search search = new Search(clazz);
		for (String key : entityPropertyMap.keySet()) {
			search.addFilterEqual(key, entityPropertyMap.get(key));
		}
		// set the pageSize and pageOffset value for the results.
		search.setPage(pageNumber);
		search.setMaxResults(pageSize);
		return this.search(search);
	}

	/**
	 * 
	 * @param entityPropertyMap
	 * @param clazz
	 * @return put the 'ILIKE' operator for a property.
	 */
	public List<T> getEntitiesSimilarToPropertyValue(
			Map<String, String> entityPropertyMap, Class<T> clazz) {
		Search search = new Search(clazz);
		for (String key : entityPropertyMap.keySet()) {
			search.addFilterILike(key, entityPropertyMap.get(key));
		}
		return this.search(search);
	}

	/**
	 * 
	 * @param entityPropertyMap
	 * @param clazz
	 * @return This behaves as applying the IN operator of particular
	 *         properties.
	 */
	public List<T> getEntitiesByPropertyValues(
			Map<String, Collection<?>> entityPropertyMap, Class<T> clazz) {
		Search search = new Search(clazz);
		for (String key : entityPropertyMap.keySet()) {
			search.addFilterIn(key, entityPropertyMap.get(key));
		}
		return this.search(search);
	}

	public boolean removeEntitiesByPropertyValue(
			Map<String, ? extends Object> entityPropertyMap, Class<T> clazz) {
		Search search = new Search(clazz);
		for (String key : entityPropertyMap.keySet()) {
			search.addFilterEqual(key, entityPropertyMap.get(key));
		}
		List<T> objects = this.search(search);
		// T[] arrayObject = objects.toArray(new T[objects.size()]);
		// now remove the all entries.
		// TODO- Need to find the way of doing it in batch.
		for (T obj : objects) {
			this.remove(obj);
		}
		return true;
	}

	/**
	 * 
	 * @param search
	 * @return Takes the search object and applies the filtering criteria
	 *         present in the Search object to fetch the entities.
	 */
	public List<T> getEntities(final Search search) {
		return this.search(search);
	}

	/**
	 * This is used to get Ids from table, provided field is provided in Search
	 * instance.
	 * 
	 * @param search
	 * @return
	 */
	public List<Long> getEntityIds(final Search search) {
		return this.search(search);
	}

	/**
	 * 
	 * @param search
	 * @return Takes the search object and applies the filtering criteria
	 *         present in the Search object to fetch the entities.
	 */
	public List<T> getEntities(final Search search, int pageOffset, int pageSize) {
		// set the pageSize and pageOffset value for the results.
		search.setPage(pageOffset);
		search.setMaxResults(pageSize);
		return this.search(search);
	}

	/**
	 * 
	 * @param search
	 * @return Total number of entities complying with the searching criteria.
	 */
	public int getEntitiesCount(Search search) {
		return this.count(search);
	}

}
