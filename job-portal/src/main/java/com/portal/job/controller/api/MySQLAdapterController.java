package com.portal.job.controller.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.portal.job.model.JobDataModel;
import com.portal.job.service.JobDetailService;
import com.portal.job.service.aggregator.JobDataModelListWrapper;

@Controller
@RequestMapping("/api")
public class MySQLAdapterController {

	@Autowired
	private JobDetailService jobDetailService;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@RequestMapping(value = "maptosql", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String mappingToSQL(
			@RequestBody String jobDataModelListWrapperString)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("inside api");
		JobDataModelListWrapper jobDataModelListWrapper = mappingJackson2HttpMessageConverter
				.getObjectMapper().readValue(jobDataModelListWrapperString,
						JobDataModelListWrapper.class);
		LinkedList<JobDataModel> jobDataModelList = jobDataModelListWrapper
				.getJobDataModelList();
		for (JobDataModel jobDataModel : jobDataModelList) {
			try {
				jobDetailService.createOrUpdate(jobDataModel,
						Long.parseLong(jobDataModel.getRecruiterId()));
			} catch (NumberFormatException | ParseException e) {
				e.printStackTrace();
				continue;
			}
		}
		return "true";
	}

	/**
	 * Command to test
	 * 
	 * <pre>
	 *  curl -X POST -d '{"name":"Preetam"}' 'http://localhost:8080/api/test'  -v --user 'job-portal:job-portal123!@#' -H "Content-Type: application/json" --basic
	 * </pre>
	 * 
	 * @param text
	 * @return
	 * @throws JsonProcessingException
	 *//*
	@RequestMapping(value = "test", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String test(@RequestBody String text)
			throws JsonProcessingException {
		System.out.println("inside api");
		mongoToMySQLAdapterCall.call();
		return text;
	}

	public class TestModel {

		public TestModel() {
			super();
			// TODO Auto-generated constructor stub
		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}*/
}