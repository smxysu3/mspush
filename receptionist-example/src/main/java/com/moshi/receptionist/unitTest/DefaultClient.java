package com.moshi.receptionist.unitTest;

import com.moshi.receptionist.remoting.netty.NettyDecoder;
import com.moshi.receptionist.remoting.netty.NettyEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DefaultClient  {
	
	private final String host;
	private final int port;
	private final String userName;
	private EventLoopGroup eventLoopGroup;
	private Bootstrap bootstrap;
	private DefaultClientHandler handler;
	
	public DefaultClientHandler getHandler() {
		return handler;
	}
	public DefaultClient(String host,int port,String userName,EventLoopGroup eventLoopGroup){
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.eventLoopGroup = eventLoopGroup;
	}
	public void start() throws InterruptedException {
			bootstrap = new Bootstrap();
			handler = new DefaultClientHandler(userName);
				bootstrap.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(
								new NettyDecoder(),
								new NettyEncoder(),
								handler);
						
					}
				});
				// Start the client.
				bootstrap.connect(host, port).sync(); // (5)
				// Wait until the connection is closed.
				//f.channel().closeFuture().sync();
		}
	

}
