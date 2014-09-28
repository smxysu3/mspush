package com.moshi.receptionist.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import com.moshi.receptionist.remoting.RemotingClient;
import com.moshi.receptionist.remoting.netty.NettyClientConfig;
import com.moshi.receptionist.remoting.netty.NettyRemotingClient;


public class Client implements VUserCreateCallBack,KeepAliveCallBack {
	private  AtomicInteger failNum = new AtomicInteger(0);
	private  AtomicInteger successNum = new AtomicInteger(0);
	private static String host;
	private static String localName;
	private ExecutorService authPool = Executors.newFixedThreadPool(32);
	private ScheduledThreadPoolExecutor heartPool = new ScheduledThreadPoolExecutor(32);
	//private ExecutorService  pacemakerPool = Executors.newFixedThreadPool(32);           
    public static RemotingClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        RemotingClient client = new NettyRemotingClient(config);
        client.registerProcessor(10003, new DefaultServerRequestProcessor(), Executors.newFixedThreadPool(1));
        client.start();
        return client;
    }

    public static void createVUsers(int num) throws InterruptedException {
    	Client main = new Client();
    	
        for (int i = 0; i < num; i++) {
        	LocalSession  session= new LocalSession(createRemotingClient(), localName+i, host);
        	VUserAuthTask task = new VUserAuthTask(session, main);
        	main.authPool.execute(task);
        	Thread.sleep(50);
        }
        System.out.println("===========================会话失败的个数为："+main.failNum);
        System.out.println("===========================会话成功的个数为："+main.successNum);
    }

    public static void main(String[] args){
    	host = args[0];
    	localName = args[2];
    	System.out.println("remote server addr:"+host +"vusers =["+args[1]+"]");
		try {
			createVUsers(Integer.valueOf(args[1]));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	@Override
	public void authSuccess(LocalSession session) {
		successNum.incrementAndGet();
		heartPool.scheduleAtFixedRate(new KeepAliveTask(session, this), 50, 50, TimeUnit.SECONDS);
	}

	@Override
	public void authFail(LocalSession session) {
		failNum.incrementAndGet();
		VUserAuthTask task = new VUserAuthTask(session, this);
    	this.authPool.execute(task);
	}

	@Override
	public void keepAliveSucc(LocalSession session) {
		
	}

	@Override
	public void keepAliveFail(LocalSession session) {
		//heartPool.scheduleAtFixedRate(new KeepAliveTask(session, this), 1, 60, TimeUnit.SECONDS);
//		System.out.println("用户"+session.getUserName()+" 心跳失败了 --->启用起搏器");
//		
//		pacemakerPool.execute(new KeepAliveTask(session, this));//启用起搏器处理
	}

}



