package com.moshi.push.recepsrv.session;

import com.moshi.receptionist.remoting.protocol.RemotingCommand;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.push.recepsrv.session
		* <p>File: Session.java 创建时间:2014年7月21日下午7:49:13</p>
		* <p>Title: 会话</p>
		* <p>Description: Channel的上层封装</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 会话</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface Session  {
	
	
	
	public static final AttributeKey<Object> APP_TAG = AttributeKey.valueOf("app_tag");
	public static final AttributeKey<String> SESSION_NAME=AttributeKey.valueOf("session_name");
	public static final AttributeKey<Long> CONNECT_TIMESTAMP=AttributeKey.valueOf("connect_timestamp");
	/**
	 * 
			*@name 交付消息
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:49:55
			* @author 徐剑
			*@param message
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void deliver(RemotingCommand message);
	/**
	 * 
			*@name 鉴权与否
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:50:20
			* @author 徐剑
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	boolean isAuthend();
	/**
	 * 
			*@name 获取会话名称
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:50:37
			* @author 徐剑
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	String getSessionName();
	/**
	 * 
			*@name 获取会话ID
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:51:01
			* @author 徐剑
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	String getSessionId();
	/**
	 * 
			*@name 交付源数据
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:51:17
			* @author 徐剑
			*@param text
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void deliverRawText(String text);
	/**
	 * 
			*@name 显式的鉴权
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:51:31
			* @author 徐剑
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void authentication();
	/**
	 * 
			*@name 失效与否
			*@Description 相关说明 
			*@Time 创建时间:2014年7月21日下午7:51:56
			* @author 徐剑
			*@param cycle
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	boolean isExpire(long cycle);
	/**
	 * 
			*@name 获取当前闲置次数
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:12:34
			* @author 徐剑
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	int currentIdleTime();
	/**
	 * 
			*@name 活跃
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:12:54
			* @author 徐剑
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void alive();
	/**
	 * 
			*@name 闲置
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:13:06
			* @author 徐剑
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void idle();
	/**
	 * 
			*@name 剔除
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:13:17
			* @author 徐剑
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void kick();
	/**
	 * 
			*@name 设置属性
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:13:27
			* @author 徐剑
			*@param key
			*@param value
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void setAtrribute(AttributeKey<Object> key,Object value);
	/**
	 * 
			*@name 获得属性值
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:13:37
			* @author 徐剑
			*@param key
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	Object getAttribute(AttributeKey<Object> key);

	/**
	 * 
			*@name 删除属性
			*@Description 相关说明 
			*@Time 创建时间:2014年7月22日下午7:13:51
			* @author 徐剑
			*@param key
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void removeAttribute(AttributeKey<Object> key);
	/**
	 * 
			*@name 获取通道
			*@Description 相关说明 
			*@Time 创建时间:2014年7月23日下午3:28:38
			* @author 徐剑
			*@return
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	Channel getChannel();
}

