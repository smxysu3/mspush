package com.moshi.receptionist.upstream;

import static org.junit.Assert.assertTrue;




import java.util.UUID;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.moshi.push.recepsrv.request.AuthenticationRequestHeader;
import com.moshi.receptionist.remoting.InvokeCallback;
import com.moshi.receptionist.remoting.RemotingClient;
import com.moshi.receptionist.remoting.exception.RemotingConnectException;
import com.moshi.receptionist.remoting.exception.RemotingSendRequestException;
import com.moshi.receptionist.remoting.exception.RemotingTimeoutException;
import com.moshi.receptionist.remoting.exception.RemotingTooMuchRequestException;
import com.moshi.receptionist.remoting.netty.NettyClientConfig;
import com.moshi.receptionist.remoting.netty.NettyRemotingClient;
import com.moshi.receptionist.remoting.netty.ResponseFuture;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;


public class RequestTest {
    public static RemotingClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        RemotingClient client = new NettyRemotingClient(config);
        client.registerProcessor(10003, new DefaultServerRequestProcessor(), Executors.newCachedThreadPool());
        client.start();
        return client;
    }


    


    @Test
    public void test_RPC_Sync() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException {
        RemotingClient client = createRemotingClient();

        for (int i = 0; i < 100; i++) {
            AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
            requestHeader.setAppTag("moshi");
            requestHeader.setSignature(UUID.randomUUID().toString());
            requestHeader.setUserName("15960265837");
            RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
            request.setRemark("鉴权请求");
            RemotingCommand response = client.invokeSync("127.0.0.1:8888", request, 1000 * 3000);
            System.out.println("invoke result = " + response);
            assertTrue(response != null);
        }

        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }

    @Test
    public void test_RPC_Heartbeat() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException {
        RemotingClient client = createRemotingClient();

            AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
            requestHeader.setAppTag("moshi");
            requestHeader.setSignature(UUID.randomUUID().toString());
            requestHeader.setUserName("15960265837");
            RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
            request.setRemark("鉴权请求");
            RemotingCommand response = client.invokeSync("127.0.0.1:8888", request, 1000 * 10);
            System.out.println("invoke result = " + response);
            for(int i =0;i<100;i++){
            	Thread.currentThread().sleep(1000*20);
            	RemotingCommand heartbeat = RemotingCommand.createRequestCommand(10002, null);
            	response =  client.invokeSync("127.0.0.1:8888", heartbeat, 1000*10);
            	System.out.println("heartbeat result = " + response);
            }
        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }
    @Test
    public void test_RPC_Oneway() throws InterruptedException, RemotingConnectException,
            RemotingTimeoutException, RemotingTooMuchRequestException, RemotingSendRequestException {
        RemotingClient client = createRemotingClient();

        for (int i = 0; i < 100; i++) {
        	AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
            requestHeader.setAppTag("moshi");
            requestHeader.setSignature(UUID.randomUUID().toString());
            requestHeader.setUserName("15960265837");
            RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
            request.setRemark(String.valueOf(i));
            client.invokeOneway("127.0.0.1:8888", request, 1000 * 3);
        }

        client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }


    @Test
    public void test_RPC_Async() throws InterruptedException, RemotingConnectException,
            RemotingTimeoutException, RemotingTooMuchRequestException, RemotingSendRequestException {

        for (int i = 0; i < 100; i++) {
        	RemotingClient client = createRemotingClient();
        	AuthenticationRequestHeader requestHeader = new AuthenticationRequestHeader();
            requestHeader.setAppTag("moshi");
            requestHeader.setSignature(UUID.randomUUID().toString());
            requestHeader.setUserName("1596026583"+i);
            RemotingCommand request = RemotingCommand.createRequestCommand(10001, requestHeader);
            request.setRemark(String.valueOf(i));
            client.invokeAsync("127.0.0.1:8888", request, 1000 * 3, new InvokeCallback() {
                @Override
                public void operationComplete(ResponseFuture responseFuture) {
                    System.out.println(responseFuture.getResponseCommand());
                }
            });
            Thread.sleep(1000 * 1);
        }

        Thread.sleep(1000 * 3);

       // client.shutdown();
        System.out.println("-----------------------------------------------------------------");
    }



}



