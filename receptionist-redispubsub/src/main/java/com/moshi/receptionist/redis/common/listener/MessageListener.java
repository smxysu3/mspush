package com.moshi.receptionist.redis.common.listener;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.receptionist.redis.common.listener
		* <p>File: MessageListener.java 创建时间:2014-8-6下午5:13:49</p>
		* <p>Title: 消息监听器</p>
		* <p>Description: 任何需要监听消息的组件都必须实现这个接口</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: jedis.pubsub</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface MessageListener {

	void onMessageEvent(String channel,String message);
}
