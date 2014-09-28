package com.moshi.push.recepsrv.session;


import java.util.Date;

import com.moshi.receptionist.remoting.protocol.RemotingCommand;


import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.push.recepsrv.session
		* <p>File: UserSession.java 创建时间:2014年7月21日下午7:46:54</p>
		* <p>Title: 用户会话</p>
		* <p>Description: 封装Channel 之上的上层会话</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 会话</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class DefaultUserSession implements Session {
	/**
	 * 会话ID
	 */
	private final String sessionId;
	private int currentIdleTime;
	/**
	 * 会话名称
	 */
	private final String sessionName;
	/**
	 * 通道
	 */
	private final Channel channel;
	/**
	 * 创建时间 
	 */
	private final Date creationTime;
	/**
	 *  鉴权
	 */
	private boolean authend;
	
	public DefaultUserSession (Channel channel,String sessionName,String token){
		this.channel = channel;
		this.sessionName = sessionName;
		this.sessionId = token;
		this.creationTime = new Date();
	}


	@Override
	public void deliver(RemotingCommand message) {
		channel.write(message);
		
	}

	@Override
	public boolean isAuthend() {
		return authend;
	}

	@Override
	public String getSessionName() {
		return sessionName;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public void deliverRawText(String text) {
		channel.write(text);
		
	}

	@Override
	public void authentication() {
		this.authend=true;
		
	}

	@Override
	public boolean isExpire(long cycle) {
		return System.currentTimeMillis()-cycle>creationTime.getTime();
	}


	@Override
	public int currentIdleTime() {
		return currentIdleTime;
	}
	
	public void idle(){
		this.currentIdleTime++;
	}
	
	public void alive(){
		this.currentIdleTime=0;
	}


	@Override
	public void kick() {
		this.channel.close();
		
	}


	@Override
	public void setAtrribute(AttributeKey<Object> key, Object value) {
		channel.attr(key).set(value);
	}


	@Override
	public Object getAttribute(AttributeKey<Object> key) {
		return channel.attr(key).get();
	}


	@Override
	public void removeAttribute(AttributeKey<Object> key) {
		channel.attr(key).remove();
	}


	@Override
	public Channel getChannel() {
		return channel;
	}
}
