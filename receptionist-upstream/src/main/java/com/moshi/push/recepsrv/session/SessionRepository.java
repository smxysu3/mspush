package com.moshi.push.recepsrv.session;

import com.moshi.receptionist.common.exception.SessionExistException;

import io.netty.channel.Channel;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.receptionist.common.listener
		* <p>File: SessionRepository.java 创建时间:2014-7-31上午10:18:59</p>
		* <p>Title: 标题（要求能简洁地表达出类的功能和职责）</p>
		* <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 模块名称</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface SessionRepository {
	
	Session buildUserSession(Channel channel,String sessionName,String token)throws SessionExistException;
	Session findSession(String sessionName);
	boolean isExist(String sessionName);
	void removeSession(String sessionName);
	void removeSession(Session session);
	void registerEventListener(SessionRepositoryEventListener listener);
	void unRegisterEventListener(SessionRepositoryEventListener listener);
	int countSize();

}
