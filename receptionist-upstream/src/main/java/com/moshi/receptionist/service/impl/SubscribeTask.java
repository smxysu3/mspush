package com.moshi.receptionist.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.redis.common.Constants;
import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.common.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

public class SubscribeTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SubscribeTask.class);
	private final String messageTag;
	private final String clientId;
	private SubscribeCallBack callBack;
	public SubscribeTask(String clientId,String messagetag,SubscribeCallBack callBack){
		this.clientId = clientId;
		this.messageTag = messagetag;
		this.callBack = callBack;
	}
	@Override
	public void run() {
		Jedis jedis;
		try {
			jedis = JedisPoolUtil.getResource();
		} catch (JedisUselessException e1) {
			callBack.onSubscribeFail(clientId, messageTag);
			return;
		}
		try {
			String key = Constants.SUBSCRIBE_CENTER+clientId+":"+messageTag;
			String pattern = Constants.SUBSCRIBE_CENTER+"*:"+messageTag;
			if(jedis.exists(key)){
				return;
			}
			if(log.isDebugEnabled()){
				log.debug("ready to listening {} .",messageTag);
			}
			Set<String> keys = jedis.keys(pattern);
			for(String subl:keys){
				if(log.isDebugEnabled()){
					log.debug("del subscribe history {} in location {} .",messageTag,subl);
				}
				jedis.del(subl);
			}
			jedis.set(key, String.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			callBack.onSubscribeFail(clientId, messageTag);
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
		callBack.onSubscribeSuccess(clientId, messageTag);
	}

}
