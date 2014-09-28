package com.moshi.push.recepsrv.conf;




public class RecepsrvConfig {
    private String recepsrvHome = System.getProperty("recepsrv.home.dir",
        System.getenv("RECEPSRV_HOME"));
    private int maxIdle = 10;
    private String redisHost ="127.0.0.1";
    private int    redisPort = 6379;
    private int    subscribeThreads=16;
    private int    pollMessageThreads=16;
    private String nodeName;
    public String getNodeName() {
		return nodeName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	public int getSubscribeThreads() {
		return subscribeThreads;
	}


	public void setSubscribeThreads(int subscribeThreads) {
		this.subscribeThreads = subscribeThreads;
	}


	public int getPollMessageThreads() {
		return pollMessageThreads;
	}


	public void setPollMessageThreads(int pollMessageThreads) {
		this.pollMessageThreads = pollMessageThreads;
	}


	public String getRecepsrvHome() {
		return recepsrvHome;
	}




	public String getRedisHost() {
		return redisHost;
	}


	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}


	public int getRedisPort() {
		return redisPort;
	}


	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}


	public void setRecepsrvHome(String recepsrvHome) {
		this.recepsrvHome = recepsrvHome;
	}


	public int getMaxIdle() {
		return maxIdle;
	}


	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}


	

}
