package com.sso.sso_java.service;


import com.sso.sso_java.mapper.UserMapper;
import com.sso.sso_java.pojo.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Component
public class SendEmail {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private UserMapper userMapper;

    public boolean sendEmailText(String text,String email){
        boolean exist = isExist(email);
        if (exist){
            return false;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("SSO");
        message.setText(text);
        message.setTo(email);
        message.setFrom("le18622412361@163.com");
        mailSender.send(message);
        return true;
    }

    public boolean sendEmailText_Anyway(String text,String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("SSO");
        message.setText(text);
        message.setTo(email);
        message.setFrom("le18622412361@163.com");
        mailSender.send(message);
        return true;
    }

    private boolean isExist(String email){
        HashMap<String, Object> map = new HashMap<>();
        map.put("email",email);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
        if (users.isEmpty()){
            return false;
        }
        return true;
    }

}
