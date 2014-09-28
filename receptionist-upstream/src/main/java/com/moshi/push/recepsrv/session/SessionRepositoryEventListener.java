package com.moshi.push.recepsrv.session;


/**
 * 
		* 墨仕服务器支撑
		* <p>com.moshi.push.recepsrv.session
		* <p>File: SessionRepositoryListener.java 创建时间:2014-7-24上午11:28:09</p>
		* <p>Title: 会话仓库事件监听器</p>
		* <p>Description: 当仓库中的会话 纳入，脱离了管理 必须要通知观察者做出变化</p>
		* <p>Copyright: Copyright (c) 2014 墨仕</p>
		* <p>Company: 墨仕</p>
		* <p>模块: 模块名称</p>
		* @author 徐剑
		* @version 0.0.1
		* @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface SessionRepositoryEventListener {
	
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
