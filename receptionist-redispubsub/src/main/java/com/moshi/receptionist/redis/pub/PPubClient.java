package com.moshi.receptionist.redis.pub;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moshi.receptionist.redis.common.Constants;
import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.common.util.JedisPoolUtil;

import redis.clients.jedis.Jedis;
public class PPubClient {

	private static final Logger log = LoggerFactory.getLogger(PPubClient.class);
	/**
	 * 发布的每条消息，都需要在“订阅者消息队列”中持久
	 * @param message
	 * @throws JedisUselessException 
	 */
	private void put(String message,String channel,int offline_time_day) throws JedisUselessException{

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String key =Constants.MESSAGE_BOX+channel+":"+df.format(new Date());
		Jedis jedis = JedisPoolUtil.getResource();
		try {
			if(!jedis.exists(key)){
				jedis.rpush(key, message);
				jedis.expire(key, 60*60*24*offline_time_day);//不是那么精确，但是总比没有好
			}else{
				jedis.rpush(key, message);
			}
		} catch (Exception e) {
			log.error("pub message to MESSAGE_BOX error {}.",e.getMessage());
			throw new JedisUselessException();
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
	}
	
	public void pub(String channel,String message,int offline_time_day) throws JedisUselessException{
		//每个消息，都有具有一个全局唯一的id
		//txid为了防止订阅端在数据处理时“乱序”，这就要求订阅者需要解析message
		Jedis jedis = JedisPoolUtil.getResource();
		try {
			Long txid = jedis.incr(Constants.MESSAGE_TXID+channel);
			String content = txid + "/" + message;
			//非事务
			this.put(content,channel,offline_time_day);
			Set<String> localtions = findLocations(channel);
				for(String localtion:localtions){
					if(log.isDebugEnabled()){
						log.debug("publish {} to location {}.",channel,localtion);
					}
					jedis.publish(localtion, txid+"/"+channel);//为每个消息设定id，最终消息格式1000/channel
				}
		} catch (Exception e) {
			log.error("publish location error {}.",e.getMessage());
			throw new JedisUselessException();
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
		
	}
	public Set<String> findLocations(String channel) throws JedisUselessException{
		String pattern = Constants.SUBSCRIBE_CENTER+"*"+channel;
		Jedis jedis = JedisPoolUtil.getResource();
		Set<String> keys = new HashSet<String>(1);
		try {
			keys = jedis.keys(pattern);
		} catch (Exception e) {
			log.error("find locations error {}.",e.getMessage());
			throw new JedisUselessException();
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
		Set<String> locations = new HashSet<String>(1);//期望查询上来的只有1个
		for(String member:keys){
				String location = member.split(":")[1];
				locations.add(location);
		}
		return locations;
	}
	
	public void close(String channel) throws JedisUselessException{
		Jedis jedis = JedisPoolUtil.getResource();
		try {
			jedis.publish(channel, "quit");
			jedis.del(channel);//删除
		} catch (Exception e) {
			log.error("close channel error {}.",e.getMessage());
			throw new JedisUselessException();
		}finally{
			JedisPoolUtil.returnResource(jedis);
		}
	}

}
