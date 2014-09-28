package com.moshi.receptionist.example;

public interface KeepAliveCallBack {

	void keepAliveSucc(LocalSession session);
	void keepAliveFail(LocalSession session);
}
