package com.portal.job.solr.annotation;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.job.exceptions.HttpClientException;
import com.portal.job.httpclient.JobPortalHttpClient;
import com.portal.job.httpclient.JobPortalHttpRequest;

@Component
public class SolrFieldProcessor {

	@Autowired
	private JobPortalHttpClient client;

	private JobPortalHttpRequest.Builder getJobRequestHttpRequestBuilder(
			final String path) {
		final JobPortalHttpRequest.Builder requestBuilder = new JobPortalHttpRequest.Builder()
				.protocol("http").host("localhost").port("8983").path(path)
				.queryParam("wt", "json");
		return requestBuilder;
	}

	public <T> void parseAnnotations(final Class<T> clazz)
			throws HttpClientException, ParseException, IOException {

		if (null != clazz) {

			String collectionName = "";

			for (final Field field : clazz.getDeclaredFields()) {

				SolrField solrField = field.getAnnotation(SolrField.class);

				if (solrField != null) {

					ObjectNode pNode = JsonNodeFactory.instance.objectNode();
					ObjectNode cNode = JsonNodeFactory.instance.objectNode();

					cNode.put("indexed", solrField.indexed());
					cNode.put("stored", solrField.stored());
					if (!StringUtils.isEmpty(solrField.multiValued()))
						cNode.put("multiValued",
								Boolean.valueOf(solrField.multiValued()));

					if (StringUtils.isEmpty(solrField.type())) {
						cNode.put("type", field.getType().getSimpleName()
								.toLowerCase());
					} else {
						cNode.put("type", solrField.type());
					}
					if (StringUtils.isEmpty(solrField.name())) {
						cNode.put("name", field.getName());
					} else {
						cNode.put("name", solrField.name());
					}
					if (!StringUtils.isEmpty(solrField.required()))
						cNode.put("required",
								Boolean.valueOf(solrField.required()));

					pNode.put("add-field", cNode);

					final JobPortalHttpRequest request = getJobRequestHttpRequestBuilder(
							"/solr/" + collectionName + "/schema").httpEntity(
							new StringEntity(pNode.toString(),
									ContentType.APPLICATION_JSON)).build();
					System.out.println(request);
					try {

						HttpResponse response = client.executePost(request);
						System.out.println(response.getStatusLine()
								.getStatusCode());
						System.out.println(EntityUtils.toString(response
								.getEntity()));

					} catch (HttpClientException | ParseException | IOException e) {
						e.printStackTrace();
					}

					System.out.println(pNode);
				}
			}
		}

	}
}
