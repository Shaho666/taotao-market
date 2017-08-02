package com.redis.test;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class AppTest {

	@Test
	public void testJedisPool() throws Exception {
//		//创建一个连接池对象
//		//系统中应该是单例的。
//		JedisPool jedisPool = new JedisPool("192.168.25.153", 6379);
//		//从连接池中获得一个连接
//		Jedis jedis = jedisPool.getResource();
//		String result = jedis.get("test");
//		System.out.println(result);
//		//jedis必须关闭
//		jedis.close();
//		
//		//系统关闭时关闭连接池
//		jedisPool.close();
		
	}

	@Test
	public void testJedisCluster() throws Exception {
		//创建一个JedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("119.29.195.17", 7001));
		nodes.add(new HostAndPort("119.29.195.17", 7002));
		nodes.add(new HostAndPort("119.29.195.17", 7003));
		nodes.add(new HostAndPort("119.29.195.17", 7004));
		nodes.add(new HostAndPort("119.29.195.17", 7005));
		nodes.add(new HostAndPort("119.29.195.17", 7006));
		
		//在nodes中指定每个节点的地址
		//jedisCluster在系统中是单例的。
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		System.out.println(jedisCluster);
		jedisCluster.set("name", "zhangsan");
		jedisCluster.set("value", "100");
		String name = jedisCluster.get("name");
		String value = jedisCluster.get("value");
		System.out.println(name);
		System.out.println(value);
		
		
		//系统关闭时关闭jedisCluster
		jedisCluster.close();
	}

	public static void main(String[] args) {
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		
		List<Integer> another = new ArrayList<Integer>();
		another.add(9999);
		another.add(8888);
		
		list.addAll(another);
		System.out.println(list);
		
	}
	
}
