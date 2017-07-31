package com.taotao.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.RedisSyncService;


@Service
public class RedisSyncServiceImpl implements RedisSyncService {

	@Autowired
	private JedisClient jedisClient;
	
	/*REDIS_CONTENT*/
	@Value("${REDIS_CONTENT_KEY}")
	private String REDIS_CONTENT_KEY;
	
	@Override
	public TaotaoResult syncContent(String key) {
		jedisClient.hdel(REDIS_CONTENT_KEY, key);
		
		return TaotaoResult.ok();
	}

}
