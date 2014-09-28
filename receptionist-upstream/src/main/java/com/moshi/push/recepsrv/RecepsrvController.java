package com.moshi.push.recepsrv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.push.recepsrv.conf.RecepsrvConfig;
import com.moshi.push.recepsrv.processor.DefaultRequestProcessor;
import com.moshi.push.recepsrv.session.SessionManager;
import com.moshi.push.recepsrv.session.SessionRepository;
import com.moshi.push.recepsrv.session.SessionRepositoryEventListener;
import com.moshi.push.recepsrv.session.impl.DefaultRoutingTable;
import com.moshi.receptionist.common.ThreadFactoryImpl;
import com.moshi.receptionist.common.router.RoutingTable;
import com.moshi.receptionist.remoting.ChannelEventListener;
import com.moshi.receptionist.remoting.RemotingServer;
import com.moshi.receptionist.remoting.netty.NettyRemotingServer;
import com.moshi.receptionist.remoting.netty.NettyServerConfig;
import com.moshi.receptionist.service.MessageSubscribeService;
import com.moshi.receptionist.service.impl.RedisMessageSubscribeService;


public class RecepsrvController {
    private static final Logger log = LoggerFactory.getLogger(RecepsrvController.class);
    // Recep Server配置
    private final RecepsrvConfig recepsrvConfig;
    // 通信层配置
    private final NettyServerConfig nettyServerConfig;
    // 服务端通信层对象
    private RemotingServer remotingServer;
    // 服务端网络请求处理线程池
    private ExecutorService remotingExecutor;
    
    private List<ServerShutdownListenner> shotdowns = new ArrayList<ServerShutdownListenner>();
    
   
    
    /**
     * 通道事件管理器
     */
    private ChannelEventListener channelEventListener;
    
    public MessageSubscribeService getMessageSubscribeService() {
		return messageSubscribeService;
	}


	private SessionRepository sessionRepository;
    
    private MessageSubscribeService messageSubscribeService;
    
    // 定时线程
    private final ScheduledExecutorService scheduledExecutorService = Executors
        .newSingleThreadScheduledExecutor(new ThreadFactoryImpl("RecepSrvScheduledThread"));




    public RecepsrvController(RecepsrvConfig recepsrvConfig,
			NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
		this.recepsrvConfig = recepsrvConfig;
		this.channelEventListener = new SessionManager(this.recepsrvConfig);
		this.sessionRepository = (SessionRepository) this.channelEventListener;
		RoutingTable routingTable = new DefaultRoutingTable(sessionRepository);
		messageSubscribeService = new RedisMessageSubscribeService(recepsrvConfig,routingTable);
		this.registerShotdownListener((ServerShutdownListenner) messageSubscribeService);
		messageSubscribeService.initialization();
		sessionRepository.registerEventListener((SessionRepositoryEventListener) messageSubscribeService);
	}


	public boolean initialize() {
		log.info("initialize Receptionist Service ");
		 // 初始化通信层
        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig, this.channelEventListener);
     // 初始化线程池
        this.remotingExecutor =
                Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads(),
                    new ThreadFactoryImpl("RemotingExecutorThread_"));
        this.registerProcessor();
        return true;
    }


    private void registerProcessor() {
        this.remotingServer
            .registerDefaultProcessor(new DefaultRequestProcessor(this), this.remotingExecutor);
    }


    public void start() throws Exception {
        this.remotingServer.start();
    }


    public void shutdown() {
        this.remotingServer.shutdown();
        this.remotingExecutor.shutdown();
        this.scheduledExecutorService.shutdown();
        for(ServerShutdownListenner listenner:shotdowns){
        	listenner.onShotdown();
        }
    }




    public NettyServerConfig getNettyServerConfig() {
        return nettyServerConfig;
    }




    public RemotingServer getRemotingServer() {
        return remotingServer;
    }


    public void setRemotingServer(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }


	public ChannelEventListener getChannelEventListener() {
		return channelEventListener;
	}


	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}


	public void registerShotdownListener(ServerShutdownListenner listenner){
		this.shotdowns.add(listenner);
	}

	
}
