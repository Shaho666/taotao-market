package com.jedis.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.rest.dao.JedisClient;

public class AppTest {

	@Test
	public void testredis() throws Exception {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");

		JedisClient jedisClient = (JedisClient) context.getBean("jedisClient");
		System.out.println(jedisClient);
		
		//redis01/redis-cli -p 7001 -c
		jedisClient.set("err", "0");
		System.out.println(jedisClient.get("err"));
		jedisClient.incr("err");
		
		System.out.println(jedisClient.get("err"));
		jedisClient.del("err");
		
		jedisClient.close();

		context.close();
		
	}
	
}
