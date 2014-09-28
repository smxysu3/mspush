package com.moshi.push.recepsrv.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import io.netty.channel.Channel;

import com.moshi.push.recepsrv.conf.RecepsrvConfig;
import com.moshi.receptionist.common.exception.SessionExistException;
import com.moshi.receptionist.remoting.ChannelEventListener;
/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.push.recepsrv.routeinfo
		* <p>File: SessionManager.java 创建时间:2014年7月21日下午1:29:28</p>
		* <p>Title: 会话管理器</p>
		* <p>Description: 管理各个链路的会话信息</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 推送服务</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class SessionManager implements ChannelEventListener,SessionRepository {
	private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
	private List<Channel> unAuthendChannel = new CopyOnWriteArrayList<Channel>();
	private Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
    private RecepsrvConfig recepsrvConfig;
    private int delayTasks = 5;
    private List<SessionRepositoryEventListener> sessionRepositoryEventListeners = new ArrayList<SessionRepositoryEventListener>();
	public SessionManager(RecepsrvConfig recepsrvConfig) {
		this.recepsrvConfig = recepsrvConfig;
		
	}

	@Override
	public void onChannelConnect(String remoteAddr, Channel channel) {
		if(log.isDebugEnabled()){
			log.debug("{} is connected!",remoteAddr);
		}
		channel.attr(Session.CONNECT_TIMESTAMP).set(System.currentTimeMillis());
		unAuthendChannel.add(channel);
	}

	@Override
	public void onChannelClose(String remoteAddr, Channel channel) {
		if(log.isDebugEnabled()){
			log.debug("{} is closed!",remoteAddr);
		}
		if(unAuthendChannel.remove(channel)){
			return;
		}
		removeSession(channel.attr(Session.SESSION_NAME).get());
	}

	@Override
	public void onChannelException(String remoteAddr, Channel channel) {
		if(log.isDebugEnabled()){
			log.debug("{} is throw exception!",remoteAddr);
		}
		onChannelClose(remoteAddr, channel);

	}

	@Override
	public void onChannelIdle(String remoteAddr, Channel channel) {
		if(log.isDebugEnabled()){
			log.debug("{} is   idle!",remoteAddr);
		}
		if(unAuthendChannel.remove(channel)){
			return;
		}
		String sessionName = channel.attr(Session.SESSION_NAME).get();
		Session session = sessions.get(sessionName);
		int currentIdleTime = session.currentIdleTime();
		if(currentIdleTime>recepsrvConfig.getMaxIdle()){
			channel.close();
			removeSession(sessionName);
			return;
		}
		session.idle();

	}

	@Override
	public Session findSession(String sessionName) {
		return sessions.get(sessionName);
	}

	@Override
	public boolean isExist(String sessionName) {
		return sessions.containsKey(sessionName);
	}

	@Override
	public void removeSession(String sessionName) {
		Session session =sessions.get(sessionName);
		if(session!=null){
			sessions.remove(sessionName);
			for(SessionRepositoryEventListener listener:sessionRepositoryEventListeners){
				listener.onSessionRemoved(session);
			}
		}
		
	}

	@Override
	public void removeSession(Session session) {
		removeSession(session.getSessionName());
		
	}

	@Override
	public Session buildUserSession(Channel channel, String sessionName,
			String token) throws SessionExistException {
		unAuthendChannel.remove(channel);
		if(this.sessions.containsKey(sessionName)){
			Session session = findSession(sessionName);
			if(session!=null&&session.getChannel()==channel){
				return session;
			}
			throw new SessionExistException();
		}
		Session session = new DefaultUserSession(channel, sessionName, token);
		sessions.put(sessionName, session);
		channel.attr(Session.SESSION_NAME).set(sessionName);
		for(SessionRepositoryEventListener listener:sessionRepositoryEventListeners){
			listener.onUserSessionBuild(session);
		}
		return session;
	}

	@Override
	public void registerEventListener(SessionRepositoryEventListener listener) {
		sessionRepositoryEventListeners.add(listener);
		
	}

	@Override
	public void unRegisterEventListener(SessionRepositoryEventListener listener) {
		sessionRepositoryEventListeners.remove(listener);
		
	}

	public int getDelayTasks() {
		return delayTasks;
	}

	public void setDelayTasks(int delayTasks) {
		this.delayTasks = delayTasks;
	}

	@Override
	public int countSize() {
		return sessions.size();
	}


}
