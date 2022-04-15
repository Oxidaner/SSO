package com.sso.sso_java.Interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sso.sso_java.pojo.ResultData;
import com.sso.sso_java.service.UserService;
import com.sso.sso_java.utils.JWTUtils;
import com.sso.sso_java.utils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource(name = "redisTemplate1")
    private RedisTemplate RedisTemplate1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //默认全部检查
        else {
            System.out.println("被jwt拦截需要验证");
            ResultData<Object> resultData = new ResultData<>();

            // 验证 token
            try {
                JWTUtils.verify(token);

                //获取载荷内容
                String uid = JWTUtils.getClaimByName(token, "uid").asString();
                String urole = JWTUtils.getClaimByName(token, "urole").asString();
                if(urole.equals("0")||urole.isEmpty()){
                    return false;
                }
                request.setAttribute("token",token);
                request.setAttribute("uid", uid);
                request.setAttribute("urole", urole);
                request.setAttribute("expiration", RedisUtil.getExpire(RedisTemplate1,token));

                return true;
            } catch (TokenExpiredException e) {
                resultData.setMessage("Token已经过期!!!");
            } catch (SignatureVerificationException e) {
                resultData.setMessage("签名错误!!!");
            } catch (AlgorithmMismatchException e) {
                resultData.setMessage("加密算法不匹配!!!");
            } catch (Exception e) {
                resultData.setMessage("无效token!!!");
            }

            resultData.setCode(0);
            String json = new ObjectMapper().writeValueAsString(resultData);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
        }
        return false;
    }
}
