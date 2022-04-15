package com.sso.sso_java.service.Impl;

import com.sso.sso_java.mapper.UserMapper;
import com.sso.sso_java.pojo.User;
import com.sso.sso_java.service.UserService;
import com.sso.sso_java.utils.MD5;
import com.sso.sso_java.vo.UserVO;
import com.sso.sso_java.vo.RootVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author A
 * @date 2022/1/22 - 01 - 22 - 23:41
 * com.fwf.ripple.service
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean registerNewUser(UserVO user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        List<User> users = userMapper.selectByMap(map);
        if (!users.isEmpty()) {
            //被占用
            return false;
        }
        String md5 = MD5.getMD5(user.getPassword());
        User insertUser = new User();
        insertUser.setUserAccount(user.getUserAccount());
        insertUser.setNickName(user.getNickName());
        insertUser.setEmail(user.getEmail());
        insertUser.setRole(user.getRole());
        insertUser.setPassword(md5);
        insertUser.setAvatarUrl(user.getAvatarUrl());
        insertUser.setGendar(user.getGendar());
        int insert = userMapper.insert(insertUser);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean loginUser(String account, String password) {
        HashMap<String, Object> map = new HashMap<>();
        String md5 = MD5.getMD5(password);

        map.put("password", md5);
        map.put("user_Account", account);
        List<User> users = userMapper.selectByMap(map);
        if (users.isEmpty()){
            map.clear();
            map.put("nick_Name", account);
            map.put("password", md5);
            users = userMapper.selectByMap(map);
        }
        if(users.isEmpty()){
            map.clear();
            map.put("email", account);
            map.put("password", md5);
            users = userMapper.selectByMap(map);
        }
        return !users.isEmpty();
    }

    @Override
    public Map<String, Object> selectUserByAccount(String account) {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        map.put("user_Account", account);
        List<User> users = userMapper.selectByMap(map);
        if (users.isEmpty()){
            map.clear();
            map.put("nick_Name", account);
            users = userMapper.selectByMap(map);
        }
        if(users.isEmpty()){
            map.clear();
            map.put("email", account);
            users = userMapper.selectByMap(map);
        }
        User user = users.get(0);
        data.put("id", user.getId());
        data.put("userAccount", user.getUserAccount());
        data.put("nickName", user.getNickName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("avatarUrl", user.getAvatarUrl());
        data.put("gender", user.getGendar());
        data.put("createdAt", user.getCreatedAt());
        data.put("updatedAt", user.getUpdatedAt());
        return data;
    }


    @Override
    public int deleteUser(String email) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_email", email);
        int change = userMapper.deleteByMap(map);
        if (change == 0) {
            throw new Exception("用户不存在");
        }
        return change;
    }

    @Override
    public boolean updateUser(int id, RootVO user) {
        HashMap<String, Object> map = new HashMap<>();
        String md5 = MD5.getMD5(user.getPassword());
        User updatetUser = new User();
        updatetUser.setId(user.getId());
        if (user.getUserAccount() != null) {
            updatetUser.setUserAccount(user.getUserAccount());
        }
        if (user.getNickName() != null){
            updatetUser.setNickName(user.getNickName());
        }
        if(user.getPassword() != null) {
            updatetUser.setPassword(md5);
        }
        if (user.getEmail() != null) {
            updatetUser.setEmail(user.getEmail());
        }
        if(user.getAvatarUrl() != null) {
            updatetUser.setAvatarUrl(user.getAvatarUrl());
        }
        if(user.getGender() != null) {
            updatetUser.setGendar(user.getGender());
        }
        int update = userMapper.updateById(updatetUser);
        if (update > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Map<String, Object> selectUser(String email) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        List<User> users = userMapper.selectByMap(map);
        User user = users.get(0);
        data.put("id", user.getId());
        data.put("userAccount", user.getUserAccount());
        data.put("nickName", user.getNickName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("avatarUrl", user.getAvatarUrl());
        data.put("gender", user.getGendar());
        data.put("createdAt", user.getCreatedAt());
        data.put("updatedAt", user.getUpdatedAt());
        return data;
    }
    @Override
    public Map<String, Object> selectUser(int id) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<User> users = userMapper.selectByMap(map);
        User user = users.get(0);
        data.put("id", user.getId());
        data.put("userAccount", user.getUserAccount());
        data.put("nickName", user.getNickName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("avatarUrl", user.getAvatarUrl());
        data.put("gender", user.getGendar());
        data.put("createdAt", user.getCreatedAt());
        data.put("updatedAt", user.getUpdatedAt());
        return data;
    }
    @Override
    public boolean updateUserPassword(int id, String password){
        String md5 = MD5.getMD5(password);
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(md5);
        int update = userMapper.updateById(updateUser);
        if (update > 0) {
            return true;
        }
        return false;
    }
}
