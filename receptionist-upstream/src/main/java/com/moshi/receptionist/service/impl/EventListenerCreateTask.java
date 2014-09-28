package com.moshi.receptionist.service.impl;

import com.moshi.receptionist.redis.sub.PSubClient;
import com.moshi.receptionist.redis.sub.handler.PSubHandler;


public class EventListenerCreateTask implements Runnable {
	private final String redisHost;
    private final int redisPort;
    private final String clientId;
    private final EventListenerFailCallBack callBack;
    private final PSubHandler handler;
    
	public EventListenerCreateTask(String redisHost, int redisPort,
			String clientId, EventListenerFailCallBack callBack,
			PSubHandler handler) {
		this.redisHost = redisHost;
		this.redisPort = redisPort;
		this.clientId = clientId;
		this.callBack = callBack;
		this.handler = handler;
	}

	@Override
	public void run() {
		PSubClient client = new PSubClient(redisHost, redisPort, clientId, handler);
		try {
			client.sub(clientId);
		} catch (Exception e) {
			callBack.onPipleFail(clientId);
		}

	}

}
