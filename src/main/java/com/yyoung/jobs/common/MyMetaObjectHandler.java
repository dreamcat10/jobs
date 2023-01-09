package com.yyoung.jobs.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

//自动填充功能
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时填充
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "loggingTime", LocalDateTime.class, LocalDateTime.now());
    }

    //更新时填充
    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictUpdateFill(metaObject, "loggingTime", LocalDateTime.class, LocalDateTime.now());
    }
}
