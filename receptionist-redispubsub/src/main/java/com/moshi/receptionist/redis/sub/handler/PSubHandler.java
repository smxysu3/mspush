package com.moshi.receptionist.redis.sub.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moshi.receptionist.redis.common.Constants;
import com.moshi.receptionist.redis.common.listener.MessageListener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PSubHandler  extends JedisPubSub   {

	private static final Logger log = LoggerFactory.getLogger(PSubHandler.class);
	private Jedis jedis;
	private String clientId;
	private static Map<String, PSubHandler> pool = new ConcurrentHashMap<String, PSubHandler>();
	private List<MessageListener> messageListeners = new ArrayList<MessageListener>();
	private PSubHandler(Jedis jedis,String clientId){
		this.jedis = jedis;
		this.clientId = clientId;
	}
	public void handle(String channel,String message){
		if(log.isDebugEnabled()){
			log.debug("on message recv the channel is {} and the message is {}",channel,message);
		}
		int index = message.indexOf("/");
		if(index < 0){
			return;
		}
		Long txid = Long.valueOf(message.substring(0,index));
		String messageTag = message.substring(index+1);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String key =Constants.MESSAGE_BOX+messageTag+":"+df.format(new Date());
		while(true){
			    	String lm = jedis.lindex(key, 0);//获取第一个消息
			    	if(lm == null){
			    		break;
			    	}
			    	int li = lm.indexOf("/");
			    	//如果消息不合法，删除并处理
			    	if(li < 0){
			    		String result = jedis.lpop(key);//删除当前message
			    		//为空
			    		if(result == null){
			    			break;
			    		}
			    		for(MessageListener listener:messageListeners){
			    			listener.onMessageEvent(messageTag, lm);
			    		}
			    		continue;
			    	}
			    	Long lxid = Long.valueOf(lm.substring(0,li));//获取消息的txid
			    	//直接消费txid之前的残留消息
			    	if(txid >= lxid){
			    		jedis.lpop(key);//删除当前message
			    		continue;
			    	}else{
			    		break;
			    	}
			    	
		}
	}
	
	public void subscribe(String channel){
		StringBuffer key = new StringBuffer(); 
		key.append(clientId)
			.append("/")
			.append(channel);
		if(log.isDebugEnabled()){
			log.debug("subscribe key is {}.",key);
		}
		boolean exist = jedis.sismember(Constants.SUBSCRIBE_CENTER+channel,key.toString());
		if(!exist){
			jedis.sadd(Constants.SUBSCRIBE_CENTER+channel, key.toString());
		}
	}
	
	public void unsubscribe(String channel){
		StringBuffer key = new StringBuffer(); 
		key.append(clientId)
			.append("/")
			.append(channel);
		if(log.isDebugEnabled()){
			log.debug("unsubscribe key is {}.",key);
		}
		jedis.srem(Constants.SUBSCRIBE_CENTER+channel, key.toString());//从“活跃订阅者”集合中删除
		//jedis.del(key);//删除“订阅者消息队列”
	}
	public void registerMessageListener(MessageListener listener){
		this.messageListeners.add(listener);
	}
	public void removeMessageListener(MessageListener listener){
		this.messageListeners.remove(listener);
	}
	@Override
	public void onMessage(String channel, String message) {
		//此处我们可以取消订阅
		if(message.equalsIgnoreCase("quit")){
			this.unsubscribe(channel);
		}
		this.handle(channel, message);
	}
	
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		if(log.isDebugEnabled()){
			log.debug("pattern message the p {} c {} m {}.",pattern,channel,message);
		}
		this.handle(channel, message);
		
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		this.subscribe(channel);
		if(log.isDebugEnabled()){
			log.debug("onSubscribe the channel is {} and current subscribedChannels size is {}.",channel,subscribedChannels);
		}
		
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		this.unsubscribe(channel);
		if(log.isDebugEnabled()){
			log.debug("onUnSubscribe the channel is {} and current subscribedChannels size is {}.",channel,subscribedChannels);
		}
		
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		if(log.isDebugEnabled()){
			log.debug("onPattern UnSubscribe the pattern is {} and current subscribedChannels size is {}.",pattern,subscribedChannels);
		}
		
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		if(log.isDebugEnabled()){
			log.debug("onPattern Subscribe the pattern is {} and current subscribedChannels size is {}.",pattern,subscribedChannels);
		}
	}
	
	

	@Override
	public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
        for(String channel : channels){
        	this.unsubscribe(channel);
        }
    }
	
	public static PSubHandler getInstance(Jedis jedis,String clientId){
		if(!pool.containsKey(clientId)){
			PSubHandler handler = new PSubHandler(jedis, clientId);
			pool.put(clientId, handler);
		}
		return pool.get(clientId);
	}
	
}