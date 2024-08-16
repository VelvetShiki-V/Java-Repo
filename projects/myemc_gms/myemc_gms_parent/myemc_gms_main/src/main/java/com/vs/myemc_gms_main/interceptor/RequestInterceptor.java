package com.vs.myemc_gms_main.interceptor;

import com.vs.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("-----------------request请求拦截-----------------");
        log.info("接收到用户请求url: {}, method: {}", request.getRequestURL().toString(), request.getMethod());
        // 获取token并刷新
        String jwt = request.getHeader("Authorization");
        if(!StringUtils.hasLength(jwt)) {
            log.info("非登录用户");
        } else {
            // 登录用户，刷新token保持登录态
            log.info("登录用户 jwt: {}", jwt);
            JwtUtil.jwtParseRefresh(jwt);
            log.info("token解析成功");
        }
        return true;
    }
}
