package com.moshi.push.recepsrv.session;

public interface SessionRemoveEventListener  {
	/**
	 * 
			*@name 会话被移除事件
			*@Description 会话被移除了意味着我们没办法再通过仓库查找对应的会话
			*@Time 创建时间:2014-7-24上午11:30:53
			* @author 徐剑
			*@param session
			* @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	void onSessionRemoved(Session session);

}
