package com.moshi.push.recepsrv.request;

import com.moshi.receptionist.remoting.CommandCustomHeader;
import com.moshi.receptionist.remoting.annotation.CFNotNull;
import com.moshi.receptionist.remoting.exception.RemotingCommandException;

/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.push.recepsrv.request
		* <p>File: AuthenticationRequestHeader.java 创建时间:2014年7月22日下午3:15:23</p>
		* <p>Title: 鉴权请求包头</p>
		* <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 协议</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class AuthenticationRequestHeader implements CommandCustomHeader {

	@CFNotNull
	private  String appTag;//应用标识
	@CFNotNull
	private  String userName;//用户名
	@CFNotNull
	private  String signature;//签名校验
	@Override
	public void checkFields() throws RemotingCommandException {
	}
	public String getAppTag() {
		return appTag;
	}
	public void setAppTag(String appTag) {
		this.appTag = appTag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
