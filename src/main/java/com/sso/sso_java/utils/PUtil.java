package com.sso.sso_java.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author A
 * @date 2022/1/23 - 01 - 23 - 2:59
 * com.fwf.ripple.util
 */
public class PUtil {
    private final static String FORMATS = "yyyy-MM-dd HH:mm:ss";
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(FORMATS, Locale.CHINA);

    /**
     * 获得当前时间，由时间戳转化而来
     * 程序中new Date操作一律由此方法代替
     * @return
     */
    public static String getTime(){
        Long dataStr = System.currentTimeMillis();
        String data = SIMPLE_DATE_FORMAT.format(dataStr);
        return data;
    }

    public static String getTime(Long timeMillis){
        String data = SIMPLE_DATE_FORMAT.format(timeMillis);
        return data;
    }


    /**
     * 生成验证码
     * @return
     */
    public static String getCaptcha(){
        //四位随机数验证码
        String captcha = String.valueOf((int)((Math.random()*9+1)*1000));
        return captcha;
    }

}
