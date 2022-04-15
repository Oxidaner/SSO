package com.sso.sso_java.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JWTUtils {
    private static String TOKEN = "token!Q@W3e4r";
    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 60 * 24); //有效时间24小时
        builder.withAudience("SSO") //发行人
                .withIssuedAt(new Date()) //发行时间
                .withExpiresAt(nowTime.getTime()); //有效时间
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        return builder.sign(Algorithm.HMAC256(TOKEN)).toString(); //加密
    }
    /**
     * 验证token
     * @param token
     * @return
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
    /**
     * 获取token中payload
     * @param token
     * @return
     */
    public static DecodedJWT getToken(String token){
        return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience = JWT.decode(token).getAudience().get(0);
        return audience;
    }
    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }

}
