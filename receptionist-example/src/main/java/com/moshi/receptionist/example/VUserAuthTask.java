package com.moshi.receptionist.example;

import java.util.UUID;
import com.moshi.push.recepsrv.request.AuthenticationRequestHeader;
import com.moshi.receptionist.common.protocol.ResponseCode;
import com.moshi.receptionist.remoting.InvokeCallback;
import com.moshi.receptionist.remoting.exception.RemotingConnectException;
import com.moshi.receptionist.remoting.exception.RemotingSendRequestException;
import com.moshi.receptionist.remoting.exception.RemotingTimeoutException;
import com.moshi.receptionist.remoting.exception.RemotingTooMuchRequestException;
import com.moshi.receptionist.remoting.netty.ResponseFuture;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

public class VUserAuthTask implements Runnable {

	private final LocalSession session;
	private final VUserCreateCallBack callBack;
	public VUserAuthTask(LocalSession session, VUserCreateCallBack callBack) {
		this.session = session;
		this.callBack = callBack;
	}
	@Override
	public void run() {
			try {
				doAuth();
			} catch (Exception e) {
				callBack.authFail(session);
				e.printStackTrace();
			} 

	}
	public void doAuth() throws RemotingConnectException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException, InterruptedException{
		AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
        requestHeader.setAppTag("moshi");
        requestHeader.setSignature(UUID.randomUUID().toString());
        requestHeader.setUserName(session.getUserName());
        RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
        session.getClient().invokeAsync(session.getHostAndPortString(), request, 1000 * 10, new InvokeCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
            	if(responseFuture.getResponseCommand()==null){
            		callBack.authFail(session);
            		return;
            	}
                if(responseFuture.getResponseCommand().getCode()==ResponseCode.SUCCESS){
                	callBack.authSuccess(session);
                }else{
                	callBack.authFail(session);
                }
            }
        });

	}


}
