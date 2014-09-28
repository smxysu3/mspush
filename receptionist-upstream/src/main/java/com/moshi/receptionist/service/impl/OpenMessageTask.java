package com.moshi.receptionist.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.redis.common.Constants;
import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.common.listener.MessageListener;
import com.moshi.receptionist.redis.common.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;

public class OpenMessageTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(OpenMessageTask.class);
	private final String messageTag;
	private final MessageListener messageListener;
	public OpenMessageTask(String messageTag,MessageListener messageListener) {
		this.messageTag = messageTag;
		this.messageListener = messageListener;
	}

	@Override
	public void run() {
		openMessageBox(messageTag);

	}
	private  void openMessageBox(String channel){
		Jedis jedis;
		try {
			jedis = JedisPoolUtil.getResource();
		} catch (JedisUselessException e1) {
			log.error("jedis Error ,please check the redis service is usefull!!!!");
			return;
		}
		String parten = Constants.MESSAGE_BOX+channel+":*";
		try {
		Set<String> keys = jedis.keys(parten);
		List<String> queryKeys = new ArrayList<String>();
		queryKeys.addAll(keys);
		Comparator<String> keyComparator = new Comparator<String>() {
			
			@Override
			public int compare(String o1, String o2) {
				String subStr1 = o1.split(":")[2];
				String subStr2 = o2.split(":")[2];
				return Integer.valueOf(subStr1)-Integer.valueOf(subStr2);
			}
		};
		Collections.sort(queryKeys,keyComparator);
		if(log.isDebugEnabled()){
			log.debug("current channel has {} message queue.",keys.size());
		}
		for(String key:queryKeys){
			long unConsumeMessageSize = jedis.llen(key);
			for(int i =0;i< unConsumeMessageSize;i++){
				String message = jedis.lpop(key);
				if(log.isDebugEnabled()){
					log.debug("the message recv is {}.",message);
				}
				messageListener.onMessageEvent(channel, message);
			}
		}
	} catch (Exception e) {
		log.error("open message box error {}.",e.getMessage());
	}finally{
		JedisPoolUtil.returnResource(jedis);
	}
}
}
