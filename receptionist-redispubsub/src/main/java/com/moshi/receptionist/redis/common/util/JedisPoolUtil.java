package com.moshi.receptionist.redis.common.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.redis.common.exception.JedisUselessException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
		* 墨仕服务器支撑
		* <p>org.receptionist.redis.common.util
		* <p>File: JedisPoolUtil.java 创建时间:2014-8-6下午5:15:02</p>
		* <p>Title: JedisPool 的工具类</p>
		* <p>Description: 从reids.properties文件加载配置参数进行初始化池</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: jedis.pubsub</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class JedisPoolUtil {
	 private static final Logger log = LoggerFactory.getLogger(JedisPoolUtil.class);
	 private static final String BUNDLENAME="redis";
	 /**
	  * jedisPool
	  */
	 private static JedisPool pool;
	 /**
	  * 静态加载配置文件进行池化
	  */
	 static{
		 ResourceBundle bundle = ResourceBundle.getBundle(BUNDLENAME);
		 if(bundle==null){
			 throw new IllegalArgumentException(
					 "[redis.properties] is not found!");
		 }
		 JedisPoolConfig config = new JedisPoolConfig();
		 config.setMaxIdle(Integer.valueOf(bundle
		 .getString("redis.pool.maxIdle")));
		 config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		 config.setTestOnBorrow(Boolean.valueOf(bundle
		 .getString("redis.pool.testOnBorrow")));
		 config.setTestOnReturn(Boolean.valueOf(bundle
		 .getString("redis.pool.testOnReturn")));
		 config.setJmxEnabled(Boolean.valueOf(bundle
		 .getString("redis.pool.jmx.enable")));
		 config.setJmxNamePrefix(bundle.getString("redis.pool.jmx.nameprefix"));
		 config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxtotal")));
		 pool = new JedisPool(config, bundle.getString("redis.ip"),
		 Integer.valueOf(bundle.getString("redis.port")));
//		 Jedis jedis = pool.getResource();//预热
//		 pool.returnResource(jedis);
		 }
	 public static Jedis getResource() throws JedisUselessException{
		 Jedis jedis = null;
		try {
			jedis = pool.getResource();
		} catch (Exception e) {
			log.error("get jedis instance error {}.",e.getMessage());
			throw new JedisUselessException();
		}
		 return jedis;
	 }
 
	 public static void returnResource(Jedis jedis){
		 if(log.isDebugEnabled()){
			 log.debug("return jedis resource:{}",jedis);
		 }
		 pool.returnResource(jedis);
	 
	 }
 

 
 
	 
}
