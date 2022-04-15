package com.sso.sso_java.service;

import com.sso.sso_java.vo.UserVO;
import com.sso.sso_java.vo.RootVO;

import java.util.List;
import java.util.Map;

/**
 * @author A
 * @date 2022/4/12 - 04 - 12 - 23:17
 * com.sso.sso_java.service
 */

public interface UserService {
    /**
     *注册一名新用户
     */
    boolean registerNewUser(UserVO user);
    /**
     * 用户登录
     * */
    boolean loginUser(String email,String password);

    /**
     * 删除用户
     * */
    int deleteUser(String email) throws Exception;

    /**
     *修改用户信息
     */
    boolean updateUser(int id , RootVO user);
    /**
     *修改用户密码
     */
    boolean updateUserPassword(int id ,String password);

    /**
     * 列出所有用户信息
     * */
//    List<Map<String,Object>> listUser();

    /**
     * 查询用户信息
     * */
    Map<String, Object> selectUser(String email);
    Map<String, Object> selectUser(int id);
    Map<String, Object> selectUserByAccount(String account) ;


}
