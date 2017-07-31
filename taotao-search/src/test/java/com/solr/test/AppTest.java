package com.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppTest {

	@Test
	public void testSolrCloud() throws Exception {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");

		SolrServer bean = (SolrServer) context.getBean("httpSolrServer");
		System.out.println(bean);
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		QueryResponse response = bean.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		
		System.out.println(solrDocumentList);
		System.out.println(solrDocumentList.getNumFound());

		context.close();

	}
	
}
