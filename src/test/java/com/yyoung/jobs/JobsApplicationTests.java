package com.yyoung.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootTest
class JobsApplicationTests {

    @Test
    void contextLoads() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext();
        System.out.println(beanFactory.isPrototype("ChatController"));
    }

}
