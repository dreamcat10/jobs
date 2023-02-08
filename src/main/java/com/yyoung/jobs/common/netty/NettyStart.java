package com.yyoung.jobs.common.netty;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通过监听Spring容器启动 来运行我们的netty服务
 */
@Component
public class NettyStart implements ApplicationRunner {

    @Resource
    private NettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
            nettyServer.run();
    }
}
