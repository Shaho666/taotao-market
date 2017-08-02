package com.taotao.search.scheduler;

import org.springframework.beans.factory.annotation.Value;

import com.taotao.common.utils.HttpClientUtil;

public class SearchScheduler {

	@Value("${UPDATE_SOLR_URL}")
	private String UPDATE_SOLR_URL;
	
	public void execute() {
		HttpClientUtil.doGet(UPDATE_SOLR_URL);
	}
	
}
