package com.portal.job.mapper;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Component;

@Component
public class CustomObjectMapper extends ObjectMapper{
	
	public CustomObjectMapper(){
		super();
		this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	}

}
