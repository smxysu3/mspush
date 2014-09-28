package com.moshi.receptionist.unitTest;

public class VUserAuthTask implements Runnable {
	private final DefaultClientHandler handler;
	public VUserAuthTask(DefaultClientHandler handler) {
		this.handler = handler;
	}
	@Override
	public void run() {
		handler.auth();
	}

	

}
