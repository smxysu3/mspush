package com.moshi.receptionist.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QAtest {

	/**
	 *@name 中文名称
	 *@Description 相关说明 
	 *@Time 创建时间:2014-8-15上午9:39:13
	 * @author 徐剑
	 *@param args
	 * @throws InterruptedException 
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ExecutorService pool = Executors.newFixedThreadPool(32);
			
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						System.out.println(this+" i am alive !");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			});
			pool.execute(t);
			while(true){
				System.out.println("main thread sleep!");
				Thread.sleep(2000);
				
			}
			
		}

}
