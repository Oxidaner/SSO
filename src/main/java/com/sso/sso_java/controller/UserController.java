package com.sso.sso_java.controller;

import com.sso.sso_java.Interceptor.PassToken;
import com.sso.sso_java.pojo.ResultData;
import com.sso.sso_java.service.UserService;
import com.sso.sso_java.utils.JWTUtils;
import com.sso.sso_java.utils.LegalUtils;
import com.sso.sso_java.utils.RedisUtil;
import com.sso.sso_java.vo.PasswordChangeVO;
import com.sso.sso_java.vo.RootVO;
import com.sso.sso_java.vo.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A
 * @date 2022/4/12 - 04 - 12 - 18:45
 * com.sso.sso_java.controller
 */
@Controller
@ResponseBody
@RequestMapping("/api/user")
public class UserController {

    @Resource(name = "redisTemplate1")
    private RedisTemplate RedisTemplate1;

    @Resource(name = "redisTemplate2")
    private RedisTemplate RedisTemplate2;

    @Resource
    private UserService userService;


    //注册
    @PostMapping("/register")
    public ResultData register(@RequestBody UserVO user) {
        if (!LegalUtils.isEmailAddress(user.getEmail())) {
            return ResultData.fail(null, "邮箱不合法");
        }
//        if (!LegalUtils.getRource(user.getAvatarUrl())) {
//            return ResultData.fail(null, "头像url路径不合法");
//        }
        Object captcha = RedisUtil.get(RedisTemplate1,user.getEmail());
        if ("".equals(captcha) || !user.getCaptcha().equals(captcha)) {
            return ResultData.fail(null, "验证码错误");
        }
        boolean status = userService.registerNewUser(user);
        if (!status) {
            return ResultData.fail(null, "邮箱已被占用");
        }
        Map<String, Object> resultMap = userService.selectUser(user.getEmail());
        return ResultData.success(resultMap, "注册成功");
    }

    //登录
    @PassToken
    @PostMapping("/login")
    public ResultData login(@RequestParam(name = "loginAccount") String account, @RequestParam(name = "password") String password) {
        boolean status = userService.loginUser(account, password);
        if (!status) {
            return ResultData.fail(null, "登录失败，账户或密码错误");
        }
        String token = "";
        Map<String, Object> data = null;
        try {
            data = userService.selectUserByAccount(account);
            Map<String, String> map = new HashMap<>();//用来存放payload
            map.put("uid", String.valueOf(data.get("id")));
            map.put("urole", (String) data.get("role"));
            token = JWTUtils.getToken(map);
            if(RedisUtil.get(RedisTemplate2,String.valueOf(data.get("id")))!=null){
                RedisUtil.del(RedisTemplate2,String.valueOf(data.get("id")));
            }
            RedisUtil.set(RedisTemplate1,String.valueOf(data.get("id")),token,24*60*60); //白名单
            data.put("token", token);

        } catch (Exception e) {
            ResultData.fail(null, "登录失败," + e.getMessage());
        }
        return ResultData.success(data, "登录成功");
    }

    //修改账号 -- common
    @PostMapping("/edit")
    public ResultData accountModifyC(@RequestBody RootVO user, HttpServletRequest req) {
        if (!req.getAttribute("uid").equals(String.valueOf(user.getId()))) {
            return ResultData.fail(null, "无效token");
        }
        Map<String, Object> dataMap = null;
        try {
            boolean b = userService.updateUser(user.getId(), user);
            if (b) {
                dataMap = userService.selectUser(user.getId());
            } else {
                return ResultData.fail(null, "修改用户信息失败");
            }
        } catch (Exception e) {
            return ResultData.fail(null, "修改用户信息失败," + e.getMessage());
        }
        return ResultData.success(dataMap, "修改用户信息成功");
    }

    //修改账号 -- super
    @PostMapping("/edit-admin")
    public ResultData accountModifyS(@RequestBody RootVO user) {
        Map<String, Object> datamap = userService.selectUser(user.getId());
        if ("2".equals(datamap.get("role"))) {
            return ResultData.fail(null, "无法修改该用户");
        }
        Map<String, Object> dataMap = null;
        try {
            boolean b = userService.updateUser(user.getId(), user);
            if (!b) {
                return ResultData.fail(null, "修改用户信息失败");
            }
            dataMap = userService.selectUser(user.getId());
        } catch (Exception e) {
            return ResultData.fail(null, "修改用户信息失败," + e.getMessage());
        }
        return ResultData.success(dataMap, "修改用户信息成功");
    }

    //检查登录状态
    @PostMapping("/is-logined")
    public ResultData checkLoginStatus(HttpServletRequest request) {
        Map<String, Object> datamap = null;
        try {
            datamap = userService.selectUser(Integer.parseInt(String.valueOf(request.getAttribute("uid"))));
        } catch (Exception e) {
            return ResultData.success(datamap ,"用户登录信息获取失败,"+ e.getMessage());
        }
        if (RedisUtil.get(RedisTemplate2, String.valueOf(request.getAttribute("uid"))) != null){
            return ResultData.success(datamap ,"用户未登录");
        }
        return ResultData.success(datamap, "用户已登录");
    }

    //退出登录
    @PostMapping("/logout")
    public ResultData logout(HttpServletRequest request) {

        try {
            RedisUtil.set(RedisTemplate2, String.valueOf(request.getAttribute("uid")), request.getAttribute("token"), (Long) request.getAttribute("expiration"));
        } catch (Exception e) {
            return ResultData.success(null ,"退出登录失败,"+ e.getMessage());
        }
        return ResultData.success(null, "用户已成功退出登录");
    }

    //获取当前账号信息
    @GetMapping("/retrieve")
    public ResultData getInfoemation(HttpServletRequest request) {
        Map<String, Object> datamap = null;
        try {
            datamap = userService.selectUser(Integer.parseInt(String.valueOf(request.getAttribute("uid"))));
        }catch (Exception e){
            return ResultData.success(datamap, "查询用户信息失败"+ e.getMessage());
        }

        if(datamap == null){
            return ResultData.success(datamap, "未找到用户信息");
        }
        return ResultData.success(datamap, "查询用户信息成功");
    }

    //修改密码
    @PostMapping("/forget-password")
    public ResultData passwordModify(@RequestBody PasswordChangeVO message) {
        Map<String, Object> dataMap = null;
        try {
            Object captcha = RedisUtil.get(RedisTemplate2,message.getEmail());
            System.out.println(captcha);
            if ("".equals(captcha) || !message.getCaptcha().equals(captcha)) {
                return ResultData.fail(null, "验证码错误");
            }

            Map<String, Object> usermessage = userService.selectUser(message.getEmail());
            boolean b = userService.updateUserPassword((Integer) usermessage.get("id"),message.getNewPassword());
            if (!b) {
                return ResultData.fail(null, "修改用户信息失败");
            }
        } catch (Exception e) {
            return ResultData.fail(null, "修改用户信息失败," + e.getMessage());
        }
        return ResultData.success(null, "修改用户密码成功");
    }
}
