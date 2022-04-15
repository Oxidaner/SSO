package com.sso.sso_java;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sso.sso_java.pojo.ResultData;
import com.sso.sso_java.service.SendEmail;
import com.sso.sso_java.utils.JWTUtils;
import com.sso.sso_java.utils.LegalUtils;
import com.sso.sso_java.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class SsoJavaApplicationTests {

    @Resource
    private SendEmail sendMail;

    @Test
    void contextLoads() {
    }


    @Resource(name = "redisTemplate1")
    private RedisTemplate test1RedisTemplate;

    @Resource(name = "redisTemplate2")
    private RedisTemplate test2RedisTemplate;

    @Test
    public void testRedisTemplate() {
        // 测试用两个模板向redis中存值
        test1RedisTemplate.opsForValue().set("name", "kong");
        test2RedisTemplate.opsForValue().set("age", "20");
    }




    @Test
    void TestJWT() {
        ResultData<Object> resultData = new ResultData<>();
        try {
            JWTUtils.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJTU08iLCJ1aWQiOiIxIiwiZXhwIjoxNjQ5ODU4OTk4LCJpYXQiOjE2NDk4MTU3OTgsInVyb2xlIjoiMSJ9.YvKKfDujo0n7fTYTYTW5F-CHEKUZ4251pnVTFaGxsvI");
            System.out.println("通过");
        } catch (TokenExpiredException e) {
            resultData.setMessage("Token已经过期!!!");
        } catch (SignatureVerificationException e) {
            resultData.setMessage("签名错误!!!");
        } catch (AlgorithmMismatchException e) {
            resultData.setMessage("加密算法不匹配!!!");
        } catch (Exception e) {
            resultData.setMessage("无效token!!!");
        }
        System.out.println(resultData);
    }

    @Test
    void TestUrl(){
        System.out.println(LegalUtils.getRource("www.baidu.com"));
        System.out.println(LegalUtils.getRource("http://qiniu.oxidaner.top/img/favicon.ico"));
    }


    @Test
    void testEmail(){
        boolean b = sendMail.sendEmailText("1110", "1963466570@qq.com");
        System.out.println(b);
    }


    @Test
    void testRedisUtil() {

        RedisUtil.set(test1RedisTemplate,"name", "123");
        System.out.println(RedisUtil.get(test1RedisTemplate,"name"));
    }


}
