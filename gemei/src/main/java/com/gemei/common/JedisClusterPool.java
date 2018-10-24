package com.gemei.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gemei.util.PropertiesUtil;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;


public class JedisClusterPool {
	
		private static JedisCluster jedisCluster = null;
		private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
		
		private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
		private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
		
		private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
		private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));
		
		private static String redis1ID = PropertiesUtil.getProperty("redis1.ip");
		private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
		private static String redis2ID = PropertiesUtil.getProperty("redis2.ip");
		private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
		private static String redis3ID = PropertiesUtil.getProperty("redis3.ip");
		private static Integer redis3Port = Integer.parseInt(PropertiesUtil.getProperty("redis3.port"));
		private static String redis4ID = PropertiesUtil.getProperty("redis4.ip");
		private static Integer redis4Port = Integer.parseInt(PropertiesUtil.getProperty("redis4.port"));
		private static String redis5ID = PropertiesUtil.getProperty("redis5.ip");
		private static Integer redis5Port = Integer.parseInt(PropertiesUtil.getProperty("redis5.port"));
		private static String redis6ID = PropertiesUtil.getProperty("redis6.ip");
		private static Integer redis6Port = Integer.parseInt(PropertiesUtil.getProperty("redis6.port"));
		
		
		public synchronized static JedisCluster getJedisCluster(){
			
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			
			config.setTestOnBorrow(testOnBorrow);
			config.setTestOnReturn(testOnReturn);
			
			//连接耗尽时，是否阻塞，false会抛出异常，true阻塞指导超时
			config.setBlockWhenExhausted(true);
			
			Set<HostAndPort> nodes = new HashSet<HostAndPort>();
			
			nodes.add(new HostAndPort(redis1ID,redis1Port));
			nodes.add(new HostAndPort(redis2ID,redis2Port));
			nodes.add(new HostAndPort(redis3ID,redis3Port));
			nodes.add(new HostAndPort(redis4ID,redis4Port));
			nodes.add(new HostAndPort(redis5ID,redis5Port));
			nodes.add(new HostAndPort(redis6ID,redis6Port));
			
			//如果jedisCluster不等于空就会创建一个实例对象
			if(jedisCluster == null){
				jedisCluster = new JedisCluster(nodes,config);
			}
			return jedisCluster;
		}
		
		
		public static void main(String[] args) {
			
			JedisCluster jedis = JedisClusterPool.getJedisCluster();
			
		    for(int i = 0;i<10;i++){
		    	jedis.set("key_xie"+i, "value"+i);
	    	}

		    // pool.destroy();//临时调用，销毁连接池中的所有连接
		    System.out.println("end");
		}
}
	
		

