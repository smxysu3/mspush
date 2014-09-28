package com.moshi.receptionist.unitTest;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ClientMain {

	private static String host;
	private static String localName;
	/**
	 *@name 中文名称
	 *@Description 相关说明 
	 *@Time 创建时间:2014-8-20上午11:27:26
	 * @author 徐剑
	 *@param args
	 * @throws InterruptedException 
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */

	public static void main(String[] args) throws InterruptedException {
		host = args[0];
    	localName = args[2];
    	System.out.println("remote server addr:"+host +"vusers =["+args[1]+"]");
		try {
			createVUsers(Integer.valueOf(args[1]));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	public static void createVUsers(int num) throws InterruptedException{
		ExecutorService authPool = Executors.newFixedThreadPool(16);
		ScheduledThreadPoolExecutor keepAlivePool = new ScheduledThreadPoolExecutor(16);
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		 for (int i = 0; i < num; i++) {
			 DefaultClient c = new DefaultClient(host, 8888, localName+i,eventLoopGroup);
			 c.start();
			 authPool.execute(new VUserAuthTask(c.getHandler()));
			 keepAlivePool.scheduleAtFixedRate(new DefaultKeepAliveTask(c.getHandler()), 50, 50, TimeUnit.SECONDS);
			 Thread.sleep(10);
	        }
	}

}
