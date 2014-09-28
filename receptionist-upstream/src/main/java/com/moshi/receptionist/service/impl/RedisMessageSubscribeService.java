package com.moshi.receptionist.service.impl;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.moshi.push.recepsrv.ServerShutdownListenner;
import com.moshi.push.recepsrv.conf.RecepsrvConfig;
import com.moshi.push.recepsrv.session.Session;
import com.moshi.push.recepsrv.session.SessionRepositoryEventListener;
import com.moshi.receptionist.common.message.MessageEvent;
import com.moshi.receptionist.common.router.RoutingTable;
import com.moshi.receptionist.redis.common.listener.MessageListener;
import com.moshi.receptionist.redis.sub.handler.PSubHandler;
import com.moshi.receptionist.router.event.SimpleMessageEvent;
import com.moshi.receptionist.service.MessageSubscribeService;
public class RedisMessageSubscribeService implements MessageSubscribeService ,
SessionRepositoryEventListener,
SubscribeCallBack,
EventListenerFailCallBack,
UnSubscribeCallBack,
ServerShutdownListenner{
	private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscribeService.class);
	private PSubHandler subHandler;
	private RoutingTable routingTable;
	private ExecutorService subscribePool;
	private ExecutorService eventListenerPool;
	private ExecutorService pollMessagesPool;
	private RecepsrvConfig serverConfig;
	private String clientId;
	private AtomicInteger subscribeNum  = new AtomicInteger(0);
	public RedisMessageSubscribeService(RecepsrvConfig config){
		this.serverConfig=config;
		clientId = serverConfig.getNodeName() ;
		try {
			if(clientId==null){
				clientId = InetAddress.getLocalHost().getHostName();
			}
		} catch (UnknownHostException e) {
			clientId="unKnowHost";
		}
		int subPoolSize = serverConfig.getSubscribeThreads();
		int pollMsgSize = serverConfig.getPollMessageThreads();
		if(subPoolSize<1){
			subscribePool = Executors.newCachedThreadPool();
		}else{
			subscribePool = Executors.newFixedThreadPool(subPoolSize);
		}
		//----------------------------------------------------------------------------------------
		if(pollMsgSize<1){
			pollMessagesPool = Executors.newCachedThreadPool();
		}else{
			pollMessagesPool = Executors.newFixedThreadPool(pollMsgSize);
		}
		eventListenerPool = Executors.newSingleThreadExecutor();//单线程的接收redis 事件通知
	}
	public RedisMessageSubscribeService(RecepsrvConfig config,RoutingTable router){
		this(config);
		routingTable = router;
	}


	@Override
	public void onUserSessionBuild(Session session) {
		subscribe(session.getSessionName());
		
	}

	@Override
	public void onSessionRemoved(Session session) {
		unSubscribe(session.getSessionName());
		
	}
	@Override
	public void registerMessageListener(MessageListener listener) {
		subHandler.registerMessageListener(listener);

	}
	@Override
	public void unRegisterMessageListener(MessageListener listener) {
		subHandler.removeMessageListener(listener);
		
	}
	@Override
	public void onMessageEvent(String channel, String message) {
		MessageEvent event = new MessageEvent();
		event.setContent(new SimpleMessageEvent(message));
		event.setMessageId(RandomStringUtils.random(10));
		event.setToTag(channel);
		routingTable.routeMessage(event);
		
	}
	@Override
	public void initialization() {
		subHandler =PSubHandler.getInstance(new Jedis(serverConfig.getRedisHost(),serverConfig.getRedisPort()), clientId);
		subHandler.registerMessageListener(this);
		createEventListener();
	}
	@Override
	public void subscribe(final String messageTags) {
		pollMessagesPool.execute(new OpenMessageTask(messageTags, this));
		subscribePool.execute(new SubscribeTask(clientId, messageTags,this));
	}
	@Override
	public void unSubscribe(final String messageTags) {
		subscribePool.execute(new UnSubscribeTask(clientId, messageTags, this));
	}
	@Override
	public void onUnsubscribeFail(String clientId, String messageTag) {
		unSubscribe(messageTag);
		
	}
	@Override
	public void onSubscribeFail(String clientId, String messageTag) {
		log.warn("subscribe fail, resub again :{}/{}.",clientId,messageTag);
		subscribe(messageTag);
		
	}
	private void createEventListener(){
		Runnable task = new EventListenerCreateTask(serverConfig.getRedisHost(), serverConfig.getRedisPort(), clientId, this,subHandler);
		eventListenerPool.execute(task);
	}
	@Override
	public void onPipleFail(String clientId) {
		createEventListener();
		
	}
	@Override
	public void onSubscribeSuccess(String clientId, String messageTag) {
		int num = subscribeNum.incrementAndGet();
		if(log.isDebugEnabled()){
			log.debug("current subscribed users = {}.",num);
		}
		
	}
	@Override
	public void onUnSubscribeSuccess(String clientId, String messageTag) {
		int  num = subscribeNum.decrementAndGet();
		if(log.isDebugEnabled()){
			log.debug("current subscribed users = {}.",num);
		}
		
	}
	@Override
	public boolean isBusy(int currentNum, int limit) {
		return currentNum-limit>subscribeNum.get();
	}
	@Override
	public void onShotdown() {
		this.eventListenerPool.shutdown();
		this.pollMessagesPool.shutdown();
		this.subscribePool.shutdown();
		
	} 

}
