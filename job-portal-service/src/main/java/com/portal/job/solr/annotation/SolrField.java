package com.portal.job.solr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SolrField {

	/**
	 * @return
	 */
	boolean stored() default true;

	/**
	 * @return
	 */
	boolean indexed() default true;

	/**
	 * @return
	 */
	String multiValued() default "";

	/**
	 * @return
	 */
	String type() default "";

	/**
	 * @return
	 */
	String required() default "";

	/**
	 * If not set the fields name or the one defined.
	 * 
	 * @return
	 */
	String name() default "";

}
