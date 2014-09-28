package com.moshi.receptionist.jedis;

import org.apache.commons.lang.RandomStringUtils;

import com.moshi.receptionist.redis.common.exception.JedisUselessException;
import com.moshi.receptionist.redis.pub.PPubClient;

public class Publisher {
	private static final String HOST="192.168.1.150";
	private static final int PORT=6379;
	/**
	 *@name 中文名称
	 *@Description 相关说明 
	 *@Time 创建时间:2014-7-29下午1:28:31
	 * @author 徐剑
	 *@param args
	 * @throws InterruptedException 
	 * @throws JedisUselessException 
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */

	public static void main(String[] args) throws InterruptedException, JedisUselessException {
		PPubClient pubClient = new PPubClient();
		final String channel = "1596026583";
//		final String channel2 = "pubsub-channel-b";
//		final String channel3 = "pubsub-channel-c";
		int i = 0;
		while(i < 100){
			String message = RandomStringUtils.random(32, true, true);//apache-commons
			pubClient.pub(channel+i, message,10);
			System.out.println("pub ... channel:"+channel+i+"  message:"+message);
			i++;
			Thread.sleep(1000);
		}

	}

}
