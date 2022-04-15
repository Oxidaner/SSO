package com.sso.sso_java.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author A
 * @date 2022/4/15 - 04 - 15 - 9:43
 * com.sso.sso_java.vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeVO {
    private String email;
    private String newPassword;
    private String captcha;
}
