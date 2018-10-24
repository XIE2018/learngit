package com.gemei.common;

import javax.annotation.PostConstruct;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import com.gemei.util.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedissonManager {
	
	private Config config = new Config();
	
	private Redisson redisson = null;
	
	
	
	public Redisson getRedisson() {
		return redisson;
	}


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
	
	@PostConstruct
	private void init(){
		try {
			config.useSingleServer().setAddress(new StringBuilder().append(redis1ID).append(":").append(redis1Port).toString());
			redisson = (Redisson) Redisson.create(config);
			
			log.info("初始化失败");
		} catch (Exception e) {
			log.error("redisson init error");
		}
	}
	
	
	
	
	
	
	

}
