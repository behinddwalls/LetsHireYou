package com.portal.job.httpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientWrapper {

	private final HttpClient client = HttpClientBuilder.create().build();

	public HttpResponse post(HttpRequest request) throws Exception {

		HttpPost post = new HttpPost(request.getUrl());
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : request.getheaderMap()
				.entrySet()) {
			post.setHeader(entry.getKey(), entry.getValue());
		}

		if (request.geturlParameters() != null
				&& !request.geturlParameters().isEmpty()) {
			for (Entry<String, String> entry : request.geturlParameters()
					.entrySet()) {
				urlParameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}

		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return response;
		} else {
			throw new Exception("Http call failed");
		}

	}

	public HttpResponse get(HttpRequest request) throws Exception {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		if (request.geturlParameters() != null
				&& !request.geturlParameters().isEmpty()) {
			for (Map.Entry<String, String> entry : request.geturlParameters()
					.entrySet()) {
				urlParameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}

//		URI uri = new URIBuilder().setHost(request.getUrl())
//				.setParameters(urlParameters).build();

		HttpGet get = new HttpGet(request.getUrl());

		if (null != request.getheaderMap() && !request.getheaderMap().isEmpty()) {
			for (Map.Entry<String, String> entry : request.getheaderMap()
					.entrySet()) {
				get.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse response = client.execute(get);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return response;
		} else {
			throw new Exception("Http call failed");
		}

	}
	

}
