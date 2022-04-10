package com.portal.job.solr.client;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.job.exceptions.SolrClientException;
import com.portal.job.httpclient.JobPortalHttpClient;

/**
 * @author behinddwalls
 *
 */
@Component
public class JobPortalSolrClient {

	private static final Logger log = LoggerFactory
			.getLogger(JobPortalSolrClient.class);

	private final SolrClient solrClient;

	private JobPortalHttpClient jobPortalHttpClient;

	@Autowired
	public JobPortalSolrClient(final JobPortalHttpClient jobPortalHttpClient) {
		this.solrClient = new HttpSolrClient("http://localhost:8983/solr/",
				jobPortalHttpClient.getClient());
		this.jobPortalHttpClient = jobPortalHttpClient;

		// attach shutdown hook
		Runtime.getRuntime().addShutdownHook(
				new SolrClientCloseOnShutDownHook());
	}

	/**
	 * @param object
	 * @return
	 * @throws SolrClientException
	 */
	public NamedList<Object> writeObjectToSolr(final Object object)
			throws SolrClientException {

		try {
			UpdateResponse response = solrClient.addBean(object, 10);
			if (response.getStatus() == HttpStatus.SC_OK) {
				return response.getResponse();
			} else {
				log.error("Returned empty response.");
			}
		} catch (IOException | SolrServerException e) {
			log.error("Failed to write object to Solr ", e);
			throw new SolrClientException(e);
		}
		return null;

	}

	/**
	 * Hook to close client on jvm down.
	 * 
	 * @author behinddwalls
	 *
	 */
	private class SolrClientCloseOnShutDownHook extends Thread {
		@Override
		public void run() {
			if (null != solrClient) {
				try {
					solrClient.close();
				} catch (IOException e) {
					log.error("Unable to close solr client");
				}
			}
		}
	}

}
