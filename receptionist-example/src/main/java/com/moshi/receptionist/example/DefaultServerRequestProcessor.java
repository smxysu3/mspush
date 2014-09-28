package com.moshi.receptionist.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.remoting.common.RemotingHelper;
import com.moshi.receptionist.remoting.netty.NettyRequestProcessor;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

import io.netty.channel.ChannelHandlerContext;


public class DefaultServerRequestProcessor implements NettyRequestProcessor {
	private static final Logger log = LoggerFactory.getLogger(DefaultServerRequestProcessor.class);
	@Override
	public RemotingCommand processRequest(ChannelHandlerContext ctx,
			RemotingCommand request) throws Exception {
		log.debug("receive request, {} {} {}",//
                request.getCode(), //
                RemotingHelper.parseChannelRemoteAddr(ctx.channel()), //
                request);
		switch (request.getCode()) {
		case 10003:
			log.info("the  body is :{}.",new String(request.getBody()));
			break;

		default:
			break;
		}
		return null;
	}

}
