package com.moshi.push.recepsrv.session;

public interface SessionBuildEventListener {
	/**
	 * 
			*@name 会话构建成功事件
			*@Description 会话被工厂构建需要处理相应的业务
			*@Time 创建时间:2014-7-24上午11:30:16
			* @author 徐剑
			*@param session
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void onUserSessionBuild(Session session);
}
