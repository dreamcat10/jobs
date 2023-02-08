package com.yyoung.jobs.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("nettyServer")
@Slf4j
public class NettyServer {
    @Autowired
    MyChannelInitializer myChannelInitializer;

    private static int port = 8848;
    private static EventLoopGroup boos = new NioEventLoopGroup();
    private static EventLoopGroup works = new NioEventLoopGroup();
    private static ServerBootstrap bootstrap = new ServerBootstrap();

    public void run(){
        log.info("netty服务器开始启动。。。");
        try {
            ChannelFuture future = bootstrap.group(boos, works)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(myChannelInitializer)
                    .bind(port)
                    .sync();
            log.info("netty服务器成功启动!");

            future.channel().closeFuture().sync();
            log.info("netty服务器成功关闭!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boos.shutdownGracefully();
            works.shutdownGracefully();
        }

    }
}
