package com.gemei.task;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gemei.common.Const;
import com.gemei.common.RedissonManager;
import com.gemei.service.IOrderService;
import com.gemei.util.PropertiesUtil;
import com.gemei.util.RedisClusterPoolUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CloseOrderTask {
	
	@Autowired
	private IOrderService iOrderService;
	
	@Autowired
	private RedissonManager redissonManager;
	
	//@Scheduled(cron="0 */1 * * * ?")//每1分钟(没个1分钟的整数倍)
	public void closeOrderTaskV1(){
		log.info("关闭订单定时任务启动");
		int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
		//iOrderService.closeOrder(hour);
		log.info("关闭订单定时任务结束");
	}
	
	//@Scheduled(cron="0 */1 * * * ?")
	public void closeOrderTaskV2(){
		log.info("关闭订单定时任务启动");
		Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "50000"));
		
		Long setnxResult = RedisClusterPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
		if(setnxResult != null && setnxResult.intValue() == 1){
			this.closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
		}else{
			log.info("没有获得锁");
		}
		
		log.info("关闭订单定时任务结束");
	}
	@Scheduled(cron="0 */1 * * * ?")
	public void closeOrderTaskV3(){
		log.info("关闭订单定时任务启动");
		Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "50000"));
		
		Long setnxResult = RedisClusterPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
		if(setnxResult != null && setnxResult.intValue() == 1){
			this.closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
		}else{
			//未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
			String lockValueStr = RedisClusterPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
			if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
				String getSetResult = RedisClusterPoolUtil.getset(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
				if(getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))){
					this.closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
				}else{
					log.info("没有获取到锁");
				}
			}
			log.info("没有获得锁");
		}
		
		log.info("关闭订单定时任务结束");
	}
	
	@Scheduled(cron="0 */1 * * * ?")
	public void closeOrderTaskV4(){
		RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
		boolean getLock = false;
		try {
			if(getLock = lock.tryLock(0,5,TimeUnit.SECONDS)){
				log.info("Redisson获取到分布式锁");
				int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
				iOrderService.closeOrder(hour);
			}else{
				log.info("Redisson没有获取到分布式锁");
			}
		} catch (InterruptedException e) {
			log.error("Redisson分布式锁获取异常",e);
		} finally {
			if(!getLock){
				return;
			}
			lock.unlock();
			log.info("Redisson分布式锁释放异常");
		}
	}
	
	private void closeOrder(String lockName){
		RedisClusterPoolUtil.expire(lockName, 50);//有效期50秒，防止死锁
		
		int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
		iOrderService.closeOrder(hour);
		RedisClusterPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
	}
}
