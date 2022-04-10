package com.portal.job.handler;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.portal.job.exceptions.HttpClientException;
import com.portal.job.httpclient.JobPortalHttpClient;
import com.portal.job.httpclient.JobPortalHttpRequest;

@Component
public class GoogleRecaptchaHandler {

	private static final String SECRET = "secret";
	private static final String RESPONSE = "response";
	private static final String RECAPTCH_API_PATH = "path";
	private static final String SUCCESS = "success";

	@Resource(name = "reCaptchaConfig")
	private Map<String, String> reCaptchaConfig;
	@Autowired
	private JobPortalHttpClient client;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	public boolean isRecaptchaValid(final String recaptchaResponse) {
		if (StringUtils.isEmpty(recaptchaResponse)) {
			return false;
		}
		JobPortalHttpRequest request = new JobPortalHttpRequest.Builder()
				.protocol(reCaptchaConfig.get(JobPortalHttpClient.PROTOCOL))
				.host(reCaptchaConfig.get(JobPortalHttpClient.HOST))
				.path(reCaptchaConfig.get(RECAPTCH_API_PATH))
				.queryParam(SECRET, reCaptchaConfig.get(SECRET))
				.queryParam(RESPONSE, recaptchaResponse).build();

		try {
			HttpResponse response = client.executePost(request);
			JsonNode node = mappingJackson2HttpMessageConverter
					.getObjectMapper().readTree(
							response.getEntity().getContent());
			return node.get(SUCCESS).asBoolean();
		} catch (HttpClientException | UnsupportedOperationException
				| IOException e) {
			return false;
		}
	}
}
