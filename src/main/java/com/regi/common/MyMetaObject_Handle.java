package com.regi.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@Slf4j
public class MyMetaObject_Handle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long id = Thread.currentThread().getId();
        log.info("线程id为:{}",id);
       metaObject.setValue("updateTime",LocalDateTime.now());
       metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
