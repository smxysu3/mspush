package com.moshi.receptionist.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.redis.common.Constants;
import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.common.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

public class UnSubscribeTask implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(UnSubscribeTask.class);
	private final String messageTag;
	private final String clientId;
	private UnSubscribeCallBack callBack;

	public UnSubscribeTask(String clientId,String messageTag,UnSubscribeCallBack callBack){
		this.clientId=clientId;
		this.messageTag=messageTag;
		this.callBack = callBack;
	}
	@Override
	public void run() {
		Jedis jedis;
		try {
			jedis = JedisPoolUtil.getResource();
		} catch (JedisUselessException e1) {
			callBack.onUnsubscribeFail(clientId, messageTag);
			return;
		}
		try {
			String key = Constants.SUBSCRIBE_CENTER+clientId+":"+messageTag;
			System.out.println("unSubscribe key:"+key);
			jedis.del(key);//从“活跃订阅者”集合中删除
		} catch (Exception e) {
			callBack.onUnsubscribeFail(clientId, messageTag);
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
		if(log.isDebugEnabled()){
			log.debug("{} unSubscribe Success!!!",messageTag);
		}
		callBack.onUnSubscribeSuccess(clientId, messageTag);
	}

}
