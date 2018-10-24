package com.gemei.common;

import java.util.ArrayList;
import java.util.List;

import com.gemei.util.PropertiesUtil;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

public class RedisShardedPool {

	private static ShardedJedisPool pool;//jedis链接池 分片的jedispool
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
	
	
	private static void initPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		
		//连接耗尽时，是否阻塞，false会抛出异常，true阻塞指导超时
		config.setBlockWhenExhausted(true);
		
		JedisShardInfo info1 = new JedisShardInfo(redis1ID,redis1Port,1000*2);
		JedisShardInfo info2 = new JedisShardInfo(redis2ID,redis2Port,1000*2);
		JedisShardInfo info3 = new JedisShardInfo(redis3ID,redis3Port,1000*2);
		JedisShardInfo info4 = new JedisShardInfo(redis4ID,redis4Port,1000*2);
		JedisShardInfo info5 = new JedisShardInfo(redis5ID,redis5Port,1000*2);
		JedisShardInfo info6 = new JedisShardInfo(redis6ID,redis6Port,1000*2);
		
		List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(6);
		jedisShardInfoList.add(info1);
		jedisShardInfoList.add(info2);
		jedisShardInfoList.add(info3);
		jedisShardInfoList.add(info4);
		jedisShardInfoList.add(info5);
		jedisShardInfoList.add(info6);
		
		pool = new ShardedJedisPool(config,jedisShardInfoList,Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);
		
		
	}
	
	static{
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }


    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }



    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }
	
    public static void main(String[] args) {
    	ShardedJedis jedis = pool.getResource();
    	for(int i = 0;i<10;i++){
    		jedis.set("key"+1, "value"+i);
    	}
        returnResource(jedis);

       // pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("end");


    }
}
