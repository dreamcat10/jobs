package com.yyoung.jobs;


import com.yyoung.jobs.common.netty.NettyServer;
import com.yyoung.jobs.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.yyoung.jobs.filter")
public class JobsApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(JobsApplication.class, args);

        NettyServer nettyServer = applicationContext.getBean(NettyServer.class);
        nettyServer.run();
    }

}
