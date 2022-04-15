package com.sso.sso_java.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author A
 * @date 2022/4/13 - 04 - 13 - 9:30
 * com.sso.sso_java.vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootVO {
    private int id;
    private String userAccount;
    private String nickName;
    private String email;
    private String password;
    private String avatarUrl;
    private String gender;
}
