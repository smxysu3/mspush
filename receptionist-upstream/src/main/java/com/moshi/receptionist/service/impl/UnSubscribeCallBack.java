package com.moshi.receptionist.service.impl;

public interface UnSubscribeCallBack {

	public void onUnsubscribeFail(String clientId,String messageTag);
	public void onUnSubscribeSuccess(String clientId,String messageTag);
}
