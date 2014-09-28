package com.moshi.receptionist.example;

import com.moshi.receptionist.remoting.exception.RemotingConnectException;
import com.moshi.receptionist.remoting.exception.RemotingSendRequestException;
import com.moshi.receptionist.remoting.exception.RemotingTimeoutException;
import com.moshi.receptionist.remoting.exception.RemotingTooMuchRequestException;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

public class KeepAliveTask implements Runnable {
	private final LocalSession session;
	private final KeepAliveCallBack callBack;
	public KeepAliveTask(LocalSession session,KeepAliveCallBack callBack) {
		this.session = session;
		this.callBack = callBack;
	}
	@Override
	public void run() {
		try {
			keepAlive();
		} catch (Exception e) {
			callBack.keepAliveFail(session);
		}

	}

	public void keepAlive() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException{
		RemotingCommand heartbeat = RemotingCommand.createRequestCommand(10002, null);
		 session.getClient().invokeOneway(session.getHostAndPortString(), heartbeat, 1000 * 5);

	}

}
