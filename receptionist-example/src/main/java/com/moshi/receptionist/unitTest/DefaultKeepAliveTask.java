package com.moshi.receptionist.unitTest;

public class DefaultKeepAliveTask implements Runnable {

	private final DefaultClientHandler handler;
	public DefaultKeepAliveTask(DefaultClientHandler handler) {
		super();
		this.handler = handler;
	}
	@Override
	public void run() {
		handler.keepAlive();

	}

}
