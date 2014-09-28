package com.moshi.receptionist.example;

import com.moshi.receptionist.remoting.protocol.RemotingCommand;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LightWeightClientHandler extends SimpleChannelInboundHandler<RemotingCommand>  {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RemotingCommand arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
