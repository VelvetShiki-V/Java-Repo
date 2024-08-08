package com.vs.myemc_gms_main.interceptor;

import com.vs.common.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("-----------------access权限访问拦截-----------------");
        // 拦截用户，执行token校验
        if(!StringUtils.hasLength(request.getHeader("Authorization"))) {
            throw new CustomException.InvalidTokenException("没有合法token");
        }
        log.info("token合法, 权限访问放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("\n\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" +
                "\n{} - {}业务执行完毕, afterCompletion..." +
                "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n\n", request.getMethod(), request.getRequestURL().toString());
    }
}
