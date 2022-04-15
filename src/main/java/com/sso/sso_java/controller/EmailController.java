package com.sso.sso_java.controller;

import com.sso.sso_java.pojo.ResultData;
import com.sso.sso_java.service.SendEmail;
import com.sso.sso_java.service.UserService;
import com.sso.sso_java.utils.LegalUtils;
import com.sso.sso_java.utils.PUtil;
import com.sso.sso_java.utils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A
 * @date 2022/4/12 - 04 - 12 - 18:50
 * com.sso.sso_java.controller
 */
@Controller
@ResponseBody
@RequestMapping(value = "/api/email", method = {RequestMethod.POST})
public class EmailController {

    @Resource
    private UserService userService;
    @Resource
    private SendEmail sendMail;
    @Resource(name = "redisTemplate1")
    private RedisTemplate RedisTemplate1;

    @Resource(name = "redisTemplate2")
    private RedisTemplate RedisTemplate2;

    /**
     * 发送验证码
     *
     * @param email
     * @return
     */
    @PostMapping("/send-captcha")
    public ResultData sendCaptcha(String email) {
        if (!LegalUtils.isEmailAddress(email)) {
            return ResultData.fail(null, "邮箱不合法");
        }
        Map<String, Object> date = new HashMap<>();
        String captcha = PUtil.getCaptcha();
        Long createTimeMills = System.currentTimeMillis();
        try {
            //向redis里存入数据和设置缓存时间
            RedisUtil.set(RedisTemplate1, email, captcha, 60 * 3);
            String text = "您的验证码为" + captcha + "请在3分钟内进行验证,否则验证码失效.";
            boolean flag = sendMail.sendEmailText(text, email);
            if (!flag) {
                return ResultData.fail(null, "用户已注册");
            }
        } catch (Exception e) {
            return ResultData.fail(null, "验证码发送失败,请校验邮箱准确性");
        }
        date.put("captcha", captcha);
        date.put("createdAt", PUtil.getTime(createTimeMills));
        date.put("updatedAt", PUtil.getTime(createTimeMills + 180000L));
        return ResultData.success(date, "验证码发送成功");
    }

    /**
     * 发送邮箱验证码 -- 忘记密码
     *
     * @param email
     * @return
     */
    @PostMapping("/forget-password-send")
    public ResultData sendEmailCaptcha(String email) {
        if (!LegalUtils.isEmailAddress(email)) {
            return ResultData.fail(null, "邮箱不合法");
        }
        Map<String, Object> date = new HashMap<>();
        String captcha = PUtil.getCaptcha();
        Long createTimeMills = System.currentTimeMillis();
        try {
            if (userService.selectUser(email) != null) {
                //向redis里存入数据和设置缓存时间
                RedisUtil.set(RedisTemplate2, email, captcha, 60 * 3);
                String text = "您的验证码为" + captcha + "请在3分钟内激活邮箱,否则验证码失效.";
                sendMail.sendEmailText_Anyway(text, email);
            } else {
                return ResultData.fail(null, "该邮箱尚未注册");
            }
        } catch (Exception e) {
            return ResultData.fail(null, "验证码发送失败,请校验邮箱准确性");
        }
        date.put("captcha", captcha);
        date.put("createdAt", PUtil.getTime(createTimeMills));
        date.put("updatedAt", PUtil.getTime(createTimeMills + 180000L));
        return ResultData.success(date, "发送激活邮件成功");
    }
}
