package com.moshi.receptionist.redis.sub;

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
import com.moshi.receptionist.redis.sub.handler.PSubHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PSubClient  {
	private static final Logger log = LoggerFactory.getLogger(PSubClient.class);
	private JedisPubSub handler;//单处理器  如果要支撑大量消息传输可以实现一个pool()
	private List<MessageListener> messageListeners = new ArrayList<MessageListener>();
	public PSubClient(String host,int port,String clientId ){
		handler = PSubHandler.getInstance(new Jedis(host,port),clientId);
	}
	public PSubClient(String host,int port,String clientId,JedisPubSub handler){
		this.handler = handler;
	}
	//这个会被阻塞住
	public void sub(String channel) throws JedisUselessException{
		Jedis jedis = JedisPoolUtil.getResource();
		try {
			jedis.subscribe(handler, channel);
		} catch (Exception e) {
			log.error("subscribe error  {}.",e.getMessage());
			throw new JedisUselessException();
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
	}
	
	public JedisPubSub getHandler() {
		return handler;
	}

//	private  void openMessageBox(String channel){
//		Jedis jedis = JedisPoolUtil.getResource();
//		try {
//			String parten = Constants.MESSAGE_BOX+channel+":*";
//			Set<String> keys = jedis.keys(parten);
//			List<String> queryKeys = new ArrayList<String>();
//			queryKeys.addAll(keys);
//			Comparator<String> keyComparator = new Comparator<String>() {
//				
//				@Override
//				public int compare(String o1, String o2) {
//					String subStr1 = o1.split(":")[2];
//					String subStr2 = o2.split(":")[2];
//					return Integer.valueOf(subStr1)-Integer.valueOf(subStr2);
//				}
//			};
//			Collections.sort(queryKeys,keyComparator);
//			if(log.isDebugEnabled()){
//				log.debug("current channel has {} message queue.",keys.size());
//			}
//			for(String key:queryKeys){
//				long unConsumeMessageSize = jedis.llen(key);
//				for(int i =0;i< unConsumeMessageSize;i++){
//					String message = jedis.lpop(key);
//					if(log.isDebugEnabled()){
//						log.debug("the message recv is {}.",message);
//					}
//					for(MessageListener listener:messageListeners){
//						listener.onMessageEvent(channel, message);
//					}
//				}
//			}
//		} catch (Exception e) {
//			log.error("open message box error {}.",e.getMessage());
//		}finally{
//			JedisPoolUtil.returnResource(jedis);
//		}
//	}
	public void unsubscribe(String channel){
		handler.unsubscribe(channel);
	}
	public void registerMessageListener(MessageListener listener){
		this.messageListeners.add(listener);
	}
	public void unRegisterMessageListener(MessageListener listener){
		this.messageListeners.remove(listener);
	}
	
}
