package com.moshi.receptionist.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moshi.receptionist.remoting.InvokeCallback;
import com.moshi.receptionist.remoting.RPCHook;
import com.moshi.receptionist.remoting.RemotingClient;
import com.moshi.receptionist.remoting.common.RemotingHelper;
import com.moshi.receptionist.remoting.exception.RemotingConnectException;
import com.moshi.receptionist.remoting.exception.RemotingSendRequestException;
import com.moshi.receptionist.remoting.exception.RemotingTimeoutException;
import com.moshi.receptionist.remoting.exception.RemotingTooMuchRequestException;
import com.moshi.receptionist.remoting.netty.NettyClientConfig;
import com.moshi.receptionist.remoting.netty.NettyDecoder;
import com.moshi.receptionist.remoting.netty.NettyEncoder;
import com.moshi.receptionist.remoting.netty.NettyRequestProcessor;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;

public class LightWeightClient  implements
		RemotingClient {
	private static final Logger log = LoggerFactory.getLogger(RemotingHelper.RemotingLogName);
	private final Bootstrap bootstrap = new Bootstrap();
	private EventLoopGroup group = new NioEventLoopGroup();
	private NettyClientConfig nettyClientConfig;
	private String host;
	private int port;
	@Override
	public void start() {
		try {
			
			bootstrap.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
            	 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(//
                         new NettyEncoder(), //
                         new NettyDecoder(), //
                         new IdleStateHandler(0, 0, nettyClientConfig.getClientChannelMaxIdleTimeSeconds()),//
                         new LightWeightClientHandler());
                 }
             });

            // Start the client.
            ChannelFuture f = bootstrap.connect(host,port).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerRPCHook(RPCHook rpcHook) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNameServerAddressList(List<String> addrs) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getNameServerAddressList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemotingCommand invokeSync(String addr, RemotingCommand request,
			long timeoutMillis) throws InterruptedException,
			RemotingConnectException, RemotingSendRequestException,
			RemotingTimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invokeAsync(String addr, RemotingCommand request,
			long timeoutMillis, InvokeCallback invokeCallback)
			throws InterruptedException, RemotingConnectException,
			RemotingTooMuchRequestException, RemotingTimeoutException,
			RemotingSendRequestException {
		// TODO Auto-generated method stub

	}

	@Override
	public void invokeOneway(String addr, RemotingCommand request,
			long timeoutMillis) throws InterruptedException,
			RemotingConnectException, RemotingTooMuchRequestException,
			RemotingTimeoutException, RemotingSendRequestException {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerProcessor(int requestCode,
			NettyRequestProcessor processor, ExecutorService executor) {
		// TODO Auto-generated method stub

	}

	
}
