package com.portal.job.services.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class TestSolrService {

	public static void main(String[] args) throws IOException,
			SolrServerException {
		new TestSolrService().test();
	}

	public void test() throws IOException, SolrServerException {

		SolrClient client = new HttpSolrClient("http://localhost:8983/solr/");

		Test test = new Test();
		test.setId("3");
		test.name="TEST";
		Child c = new Child();
		c.flag = 1;
		c.id = "1";
		test.setChild(c);
		client.addBean("test", test, 10);

		client.close();

	}

	public class Child {
		@Field
		public String id;
		@Field("flag_l")
		public int flag;
	}

	public class Test {

		@Field
		private String id;

		@Field("name")
		private String name;

		@Field(child = true)
		private Child child;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Child getChild() {
			return child;
		}

		public void setChild(Child child) {
			this.child = child;
		}

	}
}
