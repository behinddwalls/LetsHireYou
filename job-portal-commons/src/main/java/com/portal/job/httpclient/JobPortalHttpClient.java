package com.portal.job.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.portal.job.exceptions.HttpClientException;

/**
 * @author behinddwalls
 *
 */
@Component
public class JobPortalHttpClient {

	private final HttpClientContext context = HttpClientContext.create();
	private final CloseableHttpClient client = HttpClients.createDefault();

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String PROTOCOL = "protocol";

	public HttpClient getClient() {
		return client;
	}

	public URI buildURI(final JobPortalHttpRequest request) throws URISyntaxException {

		if (!StringUtils.isEmpty(request.getFullUri())) {
			return new URI(request.getFullUri());
		}
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(request.getProtocol());
		uriBuilder.setHost(request.getHost());
		uriBuilder.setPath(request.getPath());
		if (!StringUtils.isEmpty(request.getPort())) {
			uriBuilder.setPort(Integer.valueOf(request.getPort()));
		}
		if (null != request.getQueryParams()) {
			final List<NameValuePair> params = Lists.newArrayList();
			request.getQueryParams().forEach((key, value) -> {
				params.add(new BasicNameValuePair(key, value));
			});
			uriBuilder.addParameters(params);
		}
		return uriBuilder.build();
	}

	private void addHeaders(HttpRequest request, JobPortalHttpRequest jobPortalHttpRequest) {
		if (null != jobPortalHttpRequest.getRequestHeaders()) {
			jobPortalHttpRequest.getRequestHeaders().forEach((key, value) -> {
				request.addHeader(key, value);
			});

		}
	}

	private void setCredntialsForBasicAuth(HttpRequest request, JobPortalHttpRequest jobPortalHttpRequest)
			throws AuthenticationException {
		if (null != jobPortalHttpRequest.getCredentials())
			request.addHeader(new BasicScheme().authenticate(jobPortalHttpRequest.getCredentials(), request, context));
	}

	public HttpResponse executeGet(JobPortalHttpRequest httpRequest) throws HttpClientException {
		try {
			HttpGet get = new HttpGet(this.buildURI(httpRequest).toString());
			this.addHeaders(get, httpRequest);
			this.setCredntialsForBasicAuth(get, httpRequest);
			return this.client.execute(get, context);
		} catch (URISyntaxException | IOException | AuthenticationException e) {
			throw new HttpClientException("Request failed", e);
		}
	}

	public HttpResponse executePost(JobPortalHttpRequest httpRequest) throws HttpClientException {
		try {
			System.out.println(this.buildURI(httpRequest));
			HttpPost post = new HttpPost(this.buildURI(httpRequest));
			this.addHeaders(post, httpRequest);
			post.setEntity(httpRequest.getHttpEntity());
			this.setCredntialsForBasicAuth(post, httpRequest);
			return this.client.execute(post, context);
		} catch (URISyntaxException | IOException | AuthenticationException e) {
			throw new HttpClientException("Request failed", e);
		}
	}
}
