package com.sso.sso_java.utils;

/**
 * @author A
 * @date 2022/4/13 - 04 - 13 - 0:15
 * com.sso.sso_java.utils
 */
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LegalUtils {

    public static boolean isEmailAddress(String address) {
        // 是否合法
        boolean flag = false;
        if (address.isEmpty()) {
            return false;
        }
        try {
            String[] addressArr = address.split(",");
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = null;
            for (String str : addressArr) {
                matcher = regex.matcher(str);
                flag = matcher.matches();
                if (!flag) {
                    return false;
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean getRource(String source) {
        try {
            URL url = new URL(source);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            if (source.equalsIgnoreCase(uc.getURL().toString()))
                in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}


