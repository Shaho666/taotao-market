package com.redis.service.impl;

import com.redis.service.JedisClient;

import redis.clients.jedis.JedisCluster;


public class JedisClientCluster implements JedisClient {

	private JedisCluster jedisCluster;
	
	@Override
	public String get(String key) {
		String string = jedisCluster.get(key);
		return string;
	}

	@Override
	public String set(String key, String value) {
		String string = jedisCluster.set(key, value);
		return string;
	}

	@Override
	public long incr(String key) {
		Long result = jedisCluster.incr(key);
		return result;
	}

	@Override
	public Long hset(String hkey, String key, String value) {
		Long result = jedisCluster.hset(hkey, key, value);
		return result;
	}

	@Override
	public String hget(String hkey, String key) {
		String string = jedisCluster.hget(hkey, key);
		return string;
	}

	@Override
	public Long del(String key) {
		Long result = jedisCluster.del(key);
		return result;
	}

	@Override
	public Long hdel(String hkey, String key) {
		Long result = jedisCluster.hdel(hkey, key);
		return result;
	}

	@Override
	public Long expire(String key, int second) {
		Long result = jedisCluster.expire(key, second);
		return result;
	}

	@Override
	public void close() {
		jedisCluster.close();
	}
	
}
