package com.alexmisko.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertFill start ...");
        Date date = new Date();
        date.setHours(new Date().getHours() + 8);
        this.setFieldValByName("createTime",date,metaObject);
        this.setFieldValByName("updateTime",date,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updateFill start ...");
        Date date = new Date();
        date.setHours(new Date().getHours() + 8);
        this.setFieldValByName("updateTime",date,metaObject);
    }
}
