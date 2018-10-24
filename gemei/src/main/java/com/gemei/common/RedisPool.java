package com.gemei.common;

import com.gemei.util.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



public class RedisPool {
	private static JedisPool pool;//jedis链接池
	private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
	
	private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
	private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
	
	private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
	private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));
	
	private static String redisID = PropertiesUtil.getProperty("redis.ip");
	private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
	private static void initPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		
		//连接耗尽时，是否阻塞，false会抛出异常，true阻塞指导超时
		config.setBlockWhenExhausted(true);
		
		pool = new JedisPool(config,redisID,redisPort,1000*2);
	}
	
	static{
        initPool();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }


    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }



    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }
	
    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("xie","xievalue");
        returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("end");


    }
	

}
