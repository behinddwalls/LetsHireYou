package com.portal.job.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

	private final String url;
	private final Map<String, String> headerMap;
	private final Map<String, String> urlParameters;

	private HttpRequest(Builder builder) {
		this.url = builder.url;
		this.headerMap = builder.headerMap;
		this.urlParameters = builder.urlParameters;

	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getheaderMap() {
		return headerMap;
	}

	public Map<String, String> geturlParameters() {
		return urlParameters;
	}

	public static class Builder {
		private String url;
		private Map<String, String> headerMap;
		private Map<String, String> urlParameters;

		public Builder(String url) {
			this.url = url;
			this.headerMap = new HashMap<String, String>();
			this.urlParameters = new HashMap<String, String>();
		}

		public Builder header(final String key, final String value) {
			this.headerMap.put(key, value);
			return this;
		}

		public Builder urlParameter(final String key, final String value) {
			this.urlParameters.put(key, value);
			return this;
		}

		// Return the finally consrcuted reqst objec
		public HttpRequest build() {
			return new HttpRequest(this);
		}

	}

}
