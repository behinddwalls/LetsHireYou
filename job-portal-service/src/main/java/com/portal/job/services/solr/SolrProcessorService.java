package com.portal.job.services.solr;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Service;

import com.portal.job.exceptions.HttpClientException;
import com.portal.job.solr.annotation.SolrFieldProcessor;

@Service
public class SolrProcessorService {

	@Autowired
	private SolrFieldProcessor solrFieldProcessor;

	// @PostConstruct
	public void init() throws ParseException, HttpClientException, IOException,
			ClassNotFoundException {

		// create scanner and disable default filters (that is the 'false'
		// argument)
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		// add include filters which matches all the classes (or use your own)
		provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern
				.compile(".*")));

		// get matching classes defined in the package
		final Set<BeanDefinition> classes = provider
				.findCandidateComponents("com.portal.job.solr.repo.model");

		// this is how you can load the class type from BeanDefinition instance
		for (BeanDefinition bean : classes) {
			Class<?> clazz = Class.forName(bean.getBeanClassName());
			solrFieldProcessor.parseAnnotations(clazz);
		}

	}
}
