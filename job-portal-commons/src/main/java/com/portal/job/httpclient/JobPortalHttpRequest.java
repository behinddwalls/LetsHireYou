package com.portal.job.httpclient;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.google.common.collect.Maps;

/**
 * Request Builder
 * 
 * @author preetam
 *
 */
public class JobPortalHttpRequest {

	private final String protocol;
	private final String host;
	private final String path;
	private final String port;
	private final String fullUri;
	private final Map<String, String> queryParams;
	private final Map<String, String> requestHeaders;
	private final HttpEntity httpEntity;
	private final Credentials credentials;

	public static class Builder {

		private String protocol;
		private String host;
		private String path;
		private String port;
		private String fullUri;
		private Map<String, String> queryParams = Maps.newHashMap();
		private Map<String, String> requestHeaders = Maps.newHashMap();
		private HttpEntity httpEntity;
		private Credentials credentials;

		public Builder protocol(final String protocol) {
			this.protocol = protocol;
			return this;
		}

		public Builder host(final String host) {
			this.host = host;
			return this;
		}

		public Builder fullUri(final String fullUri) {
			this.fullUri = fullUri;
			return this;
		}

		public Builder basicAuth(final String username, final String password) {
			this.credentials = new UsernamePasswordCredentials(username,
					password);
			return this;
		}

		public Builder path(final String path) {
			this.path = path;
			return this;
		}

		public Builder port(final String port) {
			this.port = port;
			return this;
		}

		public Builder queryParams(final Map<String, String> queryParams) {
			this.queryParams.putAll(queryParams);
			return this;
		}

		public Builder queryParam(final String key, final String value) {
			this.queryParams.put(key, value);
			return this;
		}

		public Builder requestHeaders(final Map<String, String> requestHeaders) {
			this.requestHeaders.putAll(requestHeaders);
			return this;
		}

		public Builder requestHeader(final String key, final String value) {
			this.requestHeaders.put(key, value);
			return this;
		}

		public Builder httpEntity(final HttpEntity httpEntity) {
			this.httpEntity = httpEntity;
			return this;
		}

		public JobPortalHttpRequest build() {
			return new JobPortalHttpRequest(this);
		}

	}

	public JobPortalHttpRequest(final Builder builder) {
		this.protocol = builder.protocol;
		this.path = builder.path;
		this.host = builder.host;
		this.requestHeaders = builder.requestHeaders;
		this.queryParams = builder.queryParams;
		this.httpEntity = builder.httpEntity;
		this.port = builder.port;
		this.credentials = builder.credentials;
		this.fullUri = builder.fullUri;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public String getPath() {
		return path;
	}

	public String getPort() {
		return port;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	@Override
	public String toString() {
		return "JobPortalHttpRequest [protocol=" + protocol + ", host=" + host
				+ ", path=" + path + ", queryParams=" + queryParams
				+ ", requestHeaders=" + requestHeaders + ", httpEntity="
				+ httpEntity + "]";
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public String getFullUri() {
		return fullUri;
	}

}
