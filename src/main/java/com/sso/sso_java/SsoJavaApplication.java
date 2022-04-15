package com.sso.sso_java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sso.sso_java.mapper")
public class SsoJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoJavaApplication.class, args);
    }

}
