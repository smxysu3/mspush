package com.moshi.push.recepsrv.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

import com.moshi.push.recepsrv.RecepsrvController;
import com.moshi.push.recepsrv.protocol.RequestCode;
import com.moshi.push.recepsrv.request.AuthenticationRequestHeader;
import com.moshi.push.recepsrv.session.Session;
import com.moshi.receptionist.common.exception.SessionExistException;
import com.moshi.receptionist.common.protocol.ResponseCode;
import com.moshi.receptionist.remoting.common.RemotingHelper;
import com.moshi.receptionist.remoting.exception.RemotingCommandException;
import com.moshi.receptionist.remoting.netty.NettyRequestProcessor;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

public class DefaultRequestProcessor implements NettyRequestProcessor {
	private static final Logger log = LoggerFactory.getLogger("ReceptionistServer");
	private RecepsrvController recepsrvController;
	public DefaultRequestProcessor(RecepsrvController recepsrvController) {
		this.recepsrvController = recepsrvController;
	}

	@Override
	public RemotingCommand processRequest(ChannelHandlerContext ctx,
			RemotingCommand request) throws Exception {
		 if (log.isDebugEnabled()) {
	            log.debug("receive request, {} {} {}",//
	                request.getCode(), //
	                RemotingHelper.parseChannelRemoteAddr(ctx.channel()), //
	                request);
	        }
		 switch(request.getCode()){
		 // do logic
		 case RequestCode.AUTHENTICATION:
			 return doAuthentication(ctx,request);
		 case RequestCode.HEARTBEAT:
			 return doHeartBeat(ctx,request);
		 default:
	            break;
		 }
		return null;
	}

	private RemotingCommand doHeartBeat(ChannelHandlerContext ctx,
			RemotingCommand request) {
		log.info("心跳请求------------------------->{},",ctx.channel());
		String sessionName = ctx.channel().attr(Session.SESSION_NAME).get();
		if(sessionName!=null){
			Session session = recepsrvController.getSessionRepository()
											.findSession(sessionName);
			session.alive();
		}
		return null;
	}

	private RemotingCommand doAuthentication(ChannelHandlerContext ctx,
			RemotingCommand request) throws RemotingCommandException {
		log.info("鉴权请求------------------------->{},",ctx.channel());
		final RemotingCommand response =
                RemotingCommand.createResponseCommand(10001,null);
        final AuthenticationRequestHeader requestHeader = 
        		(AuthenticationRequestHeader) request.decodeCommandCustomHeader(AuthenticationRequestHeader.class);
        //-----------------------------------------------------------------------------
        if(recepsrvController.getMessageSubscribeService().isBusy(recepsrvController.getSessionRepository().countSize(),
        		recepsrvController.getNettyServerConfig().getLossOfhead())){
        	response.setCode(ResponseCode.SYSTEM_BUSY);
        	return response;
        }
        String userName = requestHeader.getUserName();
        String appTag   = requestHeader.getAppTag();
        String signature= requestHeader.getSignature();
    try {
        Session session = recepsrvController.getSessionRepository().buildUserSession(ctx.channel(), userName, signature);
        session.setAtrribute(Session.APP_TAG, appTag);
        response.setCode(ResponseCode.SUCCESS);
	} catch (SessionExistException e) {
		Session oldSession = recepsrvController.getSessionRepository()
											.findSession(userName);
		oldSession.kick();
		recepsrvController.getSessionRepository()
						.buildUserSession(ctx.channel(), userName, signature);
	}
		return response;
	}

}
