package com.sso.sso_java.utils;

import org.springframework.util.DigestUtils;


/**
 * @author A
 * @date 2022/1/23 - 01 - 23 - 1:07
 * com.fwf.ripple.util
 */
public class MD5 {
    //盐，用于混交md5
    private static String salt = "asdwqAsd12_qS";

    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
