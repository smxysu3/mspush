/**
		* 项目receptionist-upstream -包 com.moshi.receptionist.service.impl
		* <p>File: ExceptionListener.java 创建时间:2014-8-6上午10:49:10</p>
		* <p>Title: 标题（要求能简洁地表达出类的功能和职责）</p>
		* <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
		* <p>Copyright: Copyright (c) 2014
		巨汇_version_0.0.1_2014.03.05_SUR_0.0.1</p>
		* <p>Company: 墨仕</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
		*/
	
package com.moshi.receptionist.service.impl;

public interface SubscribeCallBack {
	
	public void onSubscribeFail(String clientId,String messageTag);
	public void onSubscribeSuccess(String clientId,String messageTag);

}
