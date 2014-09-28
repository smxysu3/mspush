package com.moshi.receptionist.jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.sub.PSubClient;
import com.moshi.receptionist.redis.sub.handler.PSubHandler;

public class Subscriber {
	private static final String HOST="192.168.1.150";
	private static final int PORT=6379;
	/**
	 *@name 中文名称
	 *@Description 相关说明 
	 *@Time 创建时间:2014-7-29下午1:30:27
	 * @author 徐剑
	 *@param args
	 * @throws InterruptedException 
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */

	public static void main(String[] args) throws InterruptedException {
		final String channel = "15960265837";
		final String channel2 = "15960265838";
//		final String channel2 = "pubsub-channel-b";
//		final String channel3 = "pubsub-channel-c";
		 final PSubClient subClient = new PSubClient(HOST,PORT,"subClient-1");
		Thread subThread = new Thread(new Runnable() {
		
			@	Override
			public void run() {
				System.out.println("----------subscribe operation begin-------");
				//在API级别，此处为轮询操作，直到unsubscribe调用，才会返回
				try {
					subClient.sub(channel);
				} catch (JedisUselessException e) {
					System.out.println("订阅通道异常----------------->");
					e.printStackTrace();
				}
				System.out.println("----------subscribe operation end-------");
				
			}
		});
//		//subThread.setDaemon(true);
		subThread.start();
		//subClient.unsubscribe(channel);
//		Thread.currentThread().sleep(1000*5);
//		for(int i =0;i<100;i++){
//			subClient.getHandler().subscribe(channel+i);
//			Thread.currentThread().sleep(1000*2);
//		}
		
	}
	

}
