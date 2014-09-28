package com.moshi.push.recepsrv;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.moshi.push.recepsrv.conf.RecepsrvConfig;
import com.moshi.receptionist.common.MQVersion;
import com.moshi.receptionist.common.MixAll;
import com.moshi.receptionist.common.util.ServerUtil;
import com.moshi.receptionist.remoting.netty.NettyServerConfig;
import com.moshi.receptionist.remoting.netty.NettySystemConfig;
import com.moshi.receptionist.remoting.protocol.RemotingCommand;


public class RecepsrvStartup {
    public static Properties properties = null;
    public static CommandLine commandLine = null;

    public static Options buildCommandlineOptions(final Options options) {
        Option opt = new Option("c", "configFile", true, "Recep server config properties file");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("p", "printConfigItem", false, "Print all config item");
        opt.setRequired(false);
        options.addOption(opt);

        return options;
    }


    public static void main(String[] args) {
        main0(args);
    }


    public static RecepsrvController main0(String[] args) {
    	//获取版本号
        System.setProperty(RemotingCommand.RemotingVersionKey, Integer.toString(MQVersion.CurrentVersion));

        // Socket发送缓冲区大小
        if (null == System.getProperty(NettySystemConfig.SystemPropertySocketSndbufSize)) {
            NettySystemConfig.SocketSndbufSize = 2048;
        }

        // Socket接收缓冲区大小
        if (null == System.getProperty(NettySystemConfig.SystemPropertySocketRcvbufSize)) {
            NettySystemConfig.SocketRcvbufSize = 1024;
        }

        try {
            // 解析命令行
            Options options = ServerUtil.buildCommandlineOptions(new Options());
            commandLine =
                    ServerUtil.parseCmdLine("recepsrv", args, buildCommandlineOptions(options),
                        new PosixParser());
            if (null == commandLine) {
                System.exit(-1);
                return null;
            }

            // 初始化配置文件
            final RecepsrvConfig recepsrvConfig = new RecepsrvConfig();
            final NettyServerConfig nettyServerConfig = new NettyServerConfig();
            if (commandLine.hasOption('c')) {
                String file = commandLine.getOptionValue('c');
                if (file != null) {
                    InputStream in = new BufferedInputStream(new FileInputStream(file));
                    properties = new Properties();
                    properties.load(in);
                    MixAll.properties2Object(properties, recepsrvConfig);
                    MixAll.properties2Object(properties, nettyServerConfig);
                    System.out.println("load config properties file OK, " + file);
                    in.close();
                }
            }

            // 打印默认配置
            if (commandLine.hasOption('p')) {
                MixAll.printObjectProperties(null, recepsrvConfig);
                MixAll.printObjectProperties(null, nettyServerConfig);
                System.exit(0);
            }

            MixAll.properties2Object(ServerUtil.commandLine2Properties(commandLine), recepsrvConfig);
            if (null == recepsrvConfig.getRecepsrvHome()) {
                System.out.println("Please set the " + "RECEPSRV_HOME"
                        + " variable in your environment to match the location of the Receptionist Server installation");
                System.exit(-2);
            }

            // 初始化Logback
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure(recepsrvConfig.getRecepsrvHome() + "/logback_recepsrv.xml");
            final Logger log = LoggerFactory.getLogger("recepServer");

            // 打印服务器配置参数
            MixAll.printObjectProperties(log, recepsrvConfig);
            MixAll.printObjectProperties(log, nettyServerConfig);

            // 初始化服务控制对象
            final RecepsrvController controller = new RecepsrvController(recepsrvConfig, nettyServerConfig);
            boolean initResult = controller.initialize();
            if (!initResult) {
                controller.shutdown();
                System.exit(-3);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                private volatile boolean hasShutdown = false;
                private AtomicInteger shutdownTimes = new AtomicInteger(0);


                @Override
                public void run() {
                    synchronized (this) {
                        log.info("shutdown hook was invoked, {}" ,this.shutdownTimes.incrementAndGet());
                        if (!this.hasShutdown) {
                            this.hasShutdown = true;
                            long begineTime = System.currentTimeMillis();
                            controller.shutdown();
                            long consumingTimeTotal = System.currentTimeMillis() - begineTime;
                            log.info("shutdown hook over, consuming time total(ms): " + consumingTimeTotal);
                        }
                    }
                }
            }, "ShutdownHook"));

            // 启动服务
            controller.start();

            String tip = "The Receptionist Server boot success.";
            log.info(tip);
            System.out.println(tip);

            return controller;
        }
        catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }
}
