package com.sso.sso_java.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author A
 * @date 2022/4/10 - 04 - 10 - 22:58
 * com.sso.sso_java.pojo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String userAccount;
    private String nickName;
    private String email;
    private String role; //0 default 1 admin 2 super
    private String password;
    private String avatarUrl;
    private String gendar;//0 unknow 1 male 2 female
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}
