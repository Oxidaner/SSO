package com.sso.sso_java.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author A
 * @date 2022/2/27 - 02 - 27 - 16:58
 * com.fwf.ripple.config
 */
@Slf4j
@Component
public class mybatisplusConfig implements MetaObjectHandler {
    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdAt",new Date(),metaObject);
        this.setFieldValByName("updatedAt",new Date(),metaObject);
    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("updatedAt",new Date(),metaObject);
    }
}
