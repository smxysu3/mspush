package com.moshi.push.recepsrv.session.impl;


import com.moshi.push.recepsrv.session.Session;
import com.moshi.push.recepsrv.session.SessionRepository;
import com.moshi.receptionist.common.exception.SessionNotFoundException;
import com.moshi.receptionist.common.message.MessageEvent;
import com.moshi.receptionist.common.router.RoutingTable;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;
import com.moshi.receptionist.remoting.protocol.RemotingSerializable;

public class DefaultRoutingTable implements RoutingTable {
	

	private SessionRepository sessionRepository;
	
	public DefaultRoutingTable(SessionRepository repository){
		this.sessionRepository = repository;
	}
	@Override
	public void routeMessage(MessageEvent event) {
		String sessionName = event.getToTag();
		if(!sessionRepository.isExist(sessionName)){
			throw new SessionNotFoundException();
		}
		Session session = sessionRepository.findSession(sessionName);
		RemotingCommand message = RemotingCommand.createRequestCommand(10003, null);
		RemotingSerializable content = event.getContent();
		message.setBody(content.encode());
		session.deliver(message);
		
	}


}
