package com.moshi.receptionist.example;

import com.moshi.receptionist.remoting.RemotingClient;

public class LocalSession {
	private final RemotingClient client;
	private final String userName;
	private final String hostAndPortString;
	public LocalSession(RemotingClient client,
			String userName, String hostAndPortString) {
		this.client = client;
		this.userName = userName;
		this.hostAndPortString = hostAndPortString;
	}
	public RemotingClient getClient() {
		return client;
	}
	public String getUserName() {
		return userName;
	}
	public String getHostAndPortString() {
		return hostAndPortString;
	}

}
