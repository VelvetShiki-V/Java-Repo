package com.vs.cloud_common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.vs.cloud_common.utils.UserThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private String uid = null;
    private String name = null;

    // 公共拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.warn("\n-----------------公共拦截器, 用户信息与操作已记录-----------------");
        // 拦截器存储请求头部用户信息
        uid = request.getHeader("uid");
        name = request.getHeader("name");
        log.info("解析到用户请求头uid: {}, name: {}, uri: {}, method: {}", uid, name, request.getRequestURL().toString(), request.getMethod());
        if(StrUtil.isNotBlank(uid)) {
            UserThreadLocalUtil.setUserUid(Long.valueOf(uid));
            log.info("用户上下文信息已设置，权限路径已放行");
        } else log.info("识别为非登录用户访问非权限路径，已放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" +
                "\nuid: {}, name: {}, uri: {}, method: {}业务执行完毕" +
                "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n", uid, name, request.getRequestURL().toString(), request.getMethod());
    }
}
