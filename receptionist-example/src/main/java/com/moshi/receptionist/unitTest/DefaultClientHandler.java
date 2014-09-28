package com.moshi.receptionist.unitTest;

import java.util.UUID;


import com.moshi.push.recepsrv.request.AuthenticationRequestHeader;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DefaultClientHandler extends ChannelInboundHandlerAdapter {


	private final String userName;
	private ChannelHandlerContext ctx;
	public DefaultClientHandler(String userName) {
		this.userName = userName;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
        this.ctx = ctx;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println(cause);
	}

	public void keepAlive(){
		RemotingCommand heartbeat = RemotingCommand.createRequestCommand(10002, null);
		ctx.writeAndFlush(heartbeat);
		
	}
	public void auth(){
		AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
        requestHeader.setAppTag("moshi");
        requestHeader.setSignature(UUID.randomUUID().toString());
        requestHeader.setUserName(userName);
        RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
        ctx.writeAndFlush(request);
	}

}
