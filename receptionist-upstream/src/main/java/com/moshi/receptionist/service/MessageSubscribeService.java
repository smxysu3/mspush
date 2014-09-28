package com.moshi.receptionist.service;

import com.moshi.receptionist.redis.common.listener.MessageListener;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.receptionist.service
		* <p>File: MessageSubscribeService.java 创建时间:2014-8-6下午5:48:31</p>
		* <p>Title: 消息订阅服务</p>
		* <p>Description: 初始化--- 订阅 ----取消订阅 </p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 模块名称</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface MessageSubscribeService extends MessageListener {

	/**
	 * 
			*@name 初始化
			*@Description 相关说明 
			*@Time 创建时间:2014-8-6下午5:49:47
			* @author 徐剑
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void initialization();
	void subscribe( final String  messageTags);
	void unSubscribe(final String  messageTags);
	void registerMessageListener(MessageListener listener);
	void unRegisterMessageListener(MessageListener listener);
	boolean isBusy(int currentNum,int limit);
}
