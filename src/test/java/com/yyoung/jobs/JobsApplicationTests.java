package com.yyoung.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;

@SpringBootTest
class JobsApplicationTests {

    @Resource
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        ValueOperations value = redisTemplate.opsForValue();
        value.set("ha", "hi");
        System.out.println(value.get("ha"));


    }

}
