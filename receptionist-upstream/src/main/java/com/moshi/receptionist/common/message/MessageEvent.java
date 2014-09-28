package com.moshi.receptionist.common.message;

import com.moshi.receptionist.remoting.protocol.RemotingSerializable;


public class MessageEvent {
	
	private RemotingSerializable content;
	private String toTag;
	private String messageId;
	public RemotingSerializable getContent() {
		return content;
	}
	public void setContent(RemotingSerializable content) {
		this.content = content;
	}
	public String getToTag() {
		return toTag;
	}
	public void setToTag(String toTag) {
		this.toTag = toTag;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	

}
