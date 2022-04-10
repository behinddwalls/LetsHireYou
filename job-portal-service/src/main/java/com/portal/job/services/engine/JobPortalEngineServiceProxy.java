package com.portal.job.services.engine;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import com.portal.job.constants.EngineConstants;
import com.portal.job.exceptions.HttpClientException;
import com.portal.job.httpclient.JobPortalHttpClient;
import com.portal.job.httpclient.JobPortalHttpRequest;
import com.portal.job.model.ComputedJobsByUser;
import com.portal.job.model.ComputedUsersByJob;
import com.portal.job.model.engine.ComputeScoreOutput;
import com.portal.job.model.engine.EngineRequestContext;

/**
 * @author behinddwalls
 *
 */
@Service
public class JobPortalEngineServiceProxy {

	private static final Logger log = LoggerFactory
			.getLogger(JobPortalEngineServiceProxy.class);

	@Resource(name = "engineServiceEndpoint")
	private Map<String, String> engineServiceEndpoint;
	@Resource(name = "engineApiEndpoints")
	private Map<String, String> engineApiEndpoints;
	@Resource(name = "engineServiceEndpointCredentials")
	private Map<String, String> engineServiceEndpointCredentials;
	@Autowired
	private JobPortalHttpClient client;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	private JobPortalHttpRequest.Builder getJobRequestHttpRequestBuilder() {
		final JobPortalHttpRequest.Builder requestBuilder = new JobPortalHttpRequest.Builder()
				.protocol(
						engineServiceEndpoint.get(JobPortalHttpClient.PROTOCOL))
				.host(engineServiceEndpoint.get(JobPortalHttpClient.HOST))
				.port(engineServiceEndpoint.get(JobPortalHttpClient.PORT))
				.basicAuth(
						engineServiceEndpointCredentials
								.get(JobPortalHttpClient.USERNAME),
						engineServiceEndpointCredentials
								.get(JobPortalHttpClient.PASSWORD));
		return requestBuilder;
	}

	public ComputeScoreOutput callComputeScore() throws HttpClientException,
			ParseException, IOException {
		JobPortalHttpRequest request = getJobRequestHttpRequestBuilder().path(
				engineApiEndpoints.get(EngineConstants.COMPUTE_USERS_BY_JOB))
				.build();

		final HttpResponse response = client.executePost(request);
		final HttpEntity httpEntity = response.getEntity();
		ComputeScoreOutput output = mappingJackson2HttpMessageConverter
				.getObjectMapper().readValue(httpEntity.getContent(),
						ComputeScoreOutput.class);
		return output;
	}

	/**
	 * 
	 * @param engineRequestContext
	 * @return
	 * @throws HttpClientException
	 * @throws ParseException
	 * @throws IOException
	 */
	public ComputedUsersByJob callEngineToGetComputedUsersByJob(
			final EngineRequestContext engineRequestContext)
			throws HttpClientException, ParseException, IOException {
		// Now set these Values in the 'request' HttpEntity to pass data to
		// Server. Convert the POJO Data to 'Json'.
		StringEntity engineRequestContextAsStringEntity = new StringEntity(
				mappingJackson2HttpMessageConverter.getObjectMapper()
						.writeValueAsString(engineRequestContext)
						.replaceAll("[^\\x20-\\x7e]", "")
						.replaceAll("[^\\u0000-\\uFFFF]", ""),
				ContentType.APPLICATION_JSON);

		// set the ContentType to 'JSON' .
		JobPortalHttpRequest request = getJobRequestHttpRequestBuilder()
				.requestHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.path(engineApiEndpoints
						.get(EngineConstants.COMPUTE_USERS_BY_JOB))
				.httpEntity(engineRequestContextAsStringEntity).build();
		final HttpResponse response = client.executePost(request);

		if (null != response
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return (response.getEntity() != null) ? mappingJackson2HttpMessageConverter
					.getObjectMapper().readValue(
							response.getEntity().getContent(),
							ComputedUsersByJob.class) : null;
		}
		return null;
	}

	/**
	 * 
	 * @param engineRequestContext
	 * @return
	 * @throws HttpClientException
	 * @throws ParseException
	 * @throws IOException
	 * 
	 *             It calls the Engine and with the Data Set as
	 *             'Set<EngineUserData>'(This will always have a single value
	 *             only.Since we are using the same model 'EngineRequestContext'
	 *             as input request when calling
	 *             'callEngineToGetComputedUsersByJob() api', so For Consistency
	 *             Purpose We are taking Set<?> value for ) and
	 */
	public ComputedJobsByUser callEngineToGetComputedJobsByUser(
			final EngineRequestContext engineRequestContext)
			throws HttpClientException, ParseException, IOException {
		// Now set these Values in the 'request' HttpEntity to pass data to
		// Server. Convert the POJO Data to 'Json'.
		StringEntity engineRequestContextAsStringEntity = new StringEntity(
				mappingJackson2HttpMessageConverter.getObjectMapper()
						.writeValueAsString(engineRequestContext)
						.replaceAll("[^\\x20-\\x7e]", "")
						.replaceAll("[^\\u0000-\\uFFFF]", ""),
				ContentType.APPLICATION_JSON);
		log.info("##Input Data"
				+ mappingJackson2HttpMessageConverter.getObjectMapper()
						.writeValueAsString(engineRequestContext));
		// set the ContentType to 'JSON' .
		JobPortalHttpRequest request = getJobRequestHttpRequestBuilder()
				.requestHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.path(engineApiEndpoints
						.get(EngineConstants.COMPUTE_JOBS_BY_USER))
				.httpEntity(engineRequestContextAsStringEntity).build();
		final HttpResponse response = client.executePost(request);
		
		if (null != response
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return (response.getEntity() != null) ? mappingJackson2HttpMessageConverter
					.getObjectMapper().readValue(
							response.getEntity().getContent(),
							ComputedJobsByUser.class) : null;
		}
		return null;
	}
}
