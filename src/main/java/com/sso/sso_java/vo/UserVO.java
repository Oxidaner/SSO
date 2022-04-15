package com.sso.sso_java.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author A
 * @date 2022/4/12 - 04 - 12 - 23:10
 * com.sso.sso_java.vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String userAccount;
    private String nickName;
    private String email;
    private String captcha;
    private String role; //0 default 1 admin 2 super
    private String password;
    private String avatarUrl;
    private String gendar;//0 unknow 1 male 2 female

}
