package com.sso.sso_java.config;


import com.sso.sso_java.Interceptor.JWTInterceptor;
import com.sso.sso_java.Interceptor.SuperInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author A
 * @date 2022/2/11 - 02 - 11 - 14:02
 * JWT拦截器配置
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private JWTInterceptor jwtInterceptor;
    @Resource
    private SuperInterceptor superInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("进入拦截器");
        //所有路径都被拦截
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns(
                "/api/user/login/**",
                "/api/user/register",
                "/api/email/forget-password-send"
        );         //添加不拦截路径;
        registry.addInterceptor(superInterceptor).addPathPatterns("/api/user/edit-admin");

    }
}