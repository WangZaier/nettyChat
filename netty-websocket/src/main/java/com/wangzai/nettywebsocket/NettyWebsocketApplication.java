package com.wangzai.nettywebsocket;

import com.wangzai.nettywebsocket.Server.NettyServer;
import com.wangzai.nettywebsocket.Server.NettyWebsocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NettyWebsocketApplication {





	public static void main(String[] args) throws Exception {
//		SpringApplication.run(NettyWebsocketApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(NettyWebsocketApplication.class, args);
//		ServerBootstrap serverBootstrap = context.getBean(ServerBootstrap.class);
//		serverBootstrap.bind(8888).sync();
		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.start();

	}


}
