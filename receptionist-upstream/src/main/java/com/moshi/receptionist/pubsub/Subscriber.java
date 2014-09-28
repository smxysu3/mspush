package com.moshi.receptionist.pubsub;

import java.util.Set;

import com.moshi.receptionist.messagelistener.MessageEventListener;

public interface Subscriber extends MessageEventListener {
	
	void subscribe(String ...topics);
	void unSubscribe(String ... topics);
	Set<String> showSubscribed();

}
