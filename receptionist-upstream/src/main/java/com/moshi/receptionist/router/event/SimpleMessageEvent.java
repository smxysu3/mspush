package com.moshi.receptionist.router.event;

import com.moshi.receptionist.remoting.protocol.RemotingSerializable;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.receptionist.router.event
		* <p>File: SimpleMessageEvent.java 创建时间:2014-8-6下午5:47:10</p>
		* <p>Title: 简单消息事件数据包</p>
		* <p>Description:基于字符文本格式的数据包 </p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: receptionist</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class SimpleMessageEvent extends RemotingSerializable {

	private String message;
	public SimpleMessageEvent(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
