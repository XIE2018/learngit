package com.gemei.util;

import com.gemei.common.JedisClusterPool;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

@Slf4j
public class RedisClusterPoolUtil {
	/**
	 * 设置KEY的有效期，单位是秒
	 * @param key
	 * @param exTime
	 * @return
	 */
	public static Long expire(String key,int exTime){
		JedisCluster jedis = null;
		Long result = null;
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.expire(key, exTime);
		} finally {
			log.error("expire key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	//exTime的单位是秒
	public static String setEx(String key,String value,int exTime){
		JedisCluster jedis = null;
		String result = null;
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.setex(key, exTime, value);
		} finally {
			log.error("setEx key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	public static String set(String key,String value){
		JedisCluster jedis = null;
		String result = null;
		
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.set(key, value);
		} finally {
			log.error("set key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	public static String getset(String key,String value){
		JedisCluster jedis = null;
		String result = null;
		
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.getSet(key, value);
		} finally {
			log.error("getset key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	public static String get(String key){
		JedisCluster jedis = null;
		String result = null;
		
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.get(key);
		} finally {
			log.error("get key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	public static Long del(String key){
		JedisCluster jedis = null;
		Long result = null;
		
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.del(key);
		} finally {
			log.error("del key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	public static Long setnx(String key,String value){
		JedisCluster jedis = null;
		Long result = null;
		
		try {
			jedis = JedisClusterPool.getJedisCluster();
			result = jedis.setnx(key, value);
		} finally {
			log.error("setnx key:{}",key);
			if (jedis != null) {
				jedis.close();
			}
		} 
		return result;
	}
	
	public static void main(String[] args){
		
		RedisClusterPoolUtil.set("keyTest", "value");
		
	}
}
