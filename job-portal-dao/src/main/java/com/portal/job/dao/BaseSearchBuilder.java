package com.portal.job.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.googlecode.genericdao.search.Search;

public class BaseSearchBuilder {

    // Member field.
    private Search searchEntities;

    public BaseSearchBuilder() {
        searchEntities = new Search();
    }

    /**
     * 
     * @return Returns the search Object. to it.
     */
    public Search buildSearchObject() {
        return this.searchEntities;
    }

    /**
     * 
     * @param entityPropertyMap
     * @param clazz
     * @return 'Equal' operator.
     */
    public BaseSearchBuilder searchEntitiesByPropertyValue(
            Map<String, ? extends Object> entityPropertyMap) {
        for (String key : entityPropertyMap.keySet()) {
            this.searchEntities.addFilterEqual(key, entityPropertyMap.get(key));
        }
        return this;
    }

    /**
     * 
     * @param entityPropertyMap
     * @param clazz
     * @return 'Equal' operator.
     */
    public BaseSearchBuilder discardEntitiesByPropertyValue(
            Map<String, ? extends Object> entityPropertyMap) {
        for (String key : entityPropertyMap.keySet()) {
            this.searchEntities.addFilterNotEqual(key, entityPropertyMap.get(key));
        }
        return this;
    }

    /**
     * 
     * @param entityPropertyMap
     * @param clazz
     * @return 'LIKE' operator
     */
    public BaseSearchBuilder searchEntitiesSimilarToPropertyValue(
            Map<String, String> entityPropertyMap) {
        for (String key : entityPropertyMap.keySet()) {
            this.searchEntities.addFilterILike(key, entityPropertyMap.get(key));
        }
        return this;
    }

    /**
     * 
     * @param entityPropertyMap
     * @param clazz
     * @return 'IN' operator.
     */
    public BaseSearchBuilder searchEntitiesByPropertyValues(
            Map<String, Collection<?>> entityPropertyMap) {
        for (String key : entityPropertyMap.keySet()) {
            this.searchEntities.addFilterIn(key, entityPropertyMap.get(key));
        }
        return this;
    }

    /**
     * 
     * @param entityPropertyMap
     * @return Provide the Sorting in Ascending order for the given property.
     */
    public BaseSearchBuilder sortAscEntitiesByPropertyValue(String entityProperty) {
        this.searchEntities.addSortAsc(entityProperty);
        return this;
    }

    /**
     * 
     * @param entityPropertyMap
     * @return Provide the Sorting in Descending order for the given property.
     */
    public BaseSearchBuilder sortDescEntitiesByPropertyValue(String entityProperty) {

        this.searchEntities.addSortDesc(entityProperty);
        return this;
    }

}
