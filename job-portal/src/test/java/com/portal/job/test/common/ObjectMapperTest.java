package com.portal.job.test.common;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.portal.job.mapper.CustomObjectMapper;

public class ObjectMapperTest {
	private ObjectMapper adapter;
	
	@Before
	public void setup() {

		adapter = new CustomObjectMapper();

	}

	@Test
	public void testClassNameSerialization() throws Exception {

		List<String> list = new ArrayList<String>();

		list.add("Test");

		Assert.assertEquals("[\"Test\"]", adapter.writeValueAsString(list));

	}

	@Test
	public void testEmptyBeanSerialization() throws Exception {

		adapter.writeValueAsString(new EmptyBean());

	}

	/**
	 * 
	 * Test class does not have any test3 field.
	 * 
	 * We should still be able to de-serialize because of the properties we have
	 * set
	 * 
	 * @throws Exception
	 */

	@Test
	public void testSuccessOnUnknownProperties() throws Exception {

		adapter.readValue("{" + "\"testField1\":\"test1\","

		+ "\"testField2\":\"test2\"," + "\"testField3\":\"test3\"}",

		TestBean.class);

	}

	@Test(expected = Exception.class)
	public void ExceptionTest() throws Exception {

		adapter.readValue("{" + "testField1\":\"test1\","

		+ "\"testField2\":\"test2\"," + "\"testField3\":\"test3\"}",

		TestBean.class);

	}

}

class TestBean {

	private String testField1;

	private String testField2;

	public String getTestField1() {

		return testField1;

	}

	public void setTestField1(String testField1) {

		this.testField1 = testField1;

	}

	public String getTestField2() {

		return testField2;

	}

	public void setTestField2(String testField2) {

		this.testField2 = testField2;

	}

	@Override
	public String toString() {

		return "EmptyBean [testField1=" + testField1 + ", testField2="

		+ testField2 + "]";

	}

}

class EmptyBean {

	@SuppressWarnings("unused")
	private String field;

}
