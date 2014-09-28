package com.moshi.receptionist.redis.common;
/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.receptionist.redis.common
		* <p>File: Constants.java 创建时间:2014-8-6下午5:18:04</p>
		* <p>Title: 常量配置</p>
		* <p>Description: 对key的前缀进行预定义</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: jedis.pubsub</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class Constants {

	public static final String host = "127.0.0.1";//ip
	public static final int port = 6379;//port
	public static final String SUBSCRIBE_CENTER = "_-subscribe-center-_:";//订阅中心
	public static final String MESSAGE_TXID = "_-message-txid-_:";//消息序列
	public static final String MESSAGE_BOX="_-message-box-_:";//消息盒子

}
