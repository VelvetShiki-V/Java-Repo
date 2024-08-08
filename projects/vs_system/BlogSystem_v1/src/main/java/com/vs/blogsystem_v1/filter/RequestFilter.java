package com.vs.blogsystem_v1.filter;

import com.alibaba.fastjson.JSONObject;
import com.vs.pojo.Result;
import com.vs.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

// 拦截类组件要在启动类加上ServletComponentScan注解以启用拦截功能
// 配置拦截url
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("init filter success");
    }

    @Override
    // HttpServletRequest是HttpServletRequest的子类，父类不能直接调用子类方法
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1. 拦截
        log.info("request拦截成功");

        // 2. 获取请求url
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String requestUrl = req.getRequestURL().toString();
        log.info("用户请求url: {}", requestUrl);

        // 3. 判断是否是登录请求，除登录以外的所有请求都拦截，登录则直接放行
        if(requestUrl.contains("login")) {
            // 3.1 login request, 直接放行
            log.info("login请求，直接放行");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // 3.2 非login，验证token
            String jwt = req.getHeader("token");
            if(!StringUtils.hasLength(jwt)) {
                // 非法访问，返回错误Result json
                log.error("非法请求, 没有合法token: INVALID_TOKEN");
                // 将返回的Result对象转为json串
                String ret = JSONObject.toJSONString(Result.error("INVALID_TOKEN"));
                res.getWriter().write(ret);
            } else {
                // 4. 携带了token，开始解析jwt
                log.info("守卫请求, 开始验证jwt");
                Claims payload = JwtUtil.jwtParse(jwt);
                if(payload == null) {
                    // 4.1 解析失败，token验证失败
                    log.error("token验证失败: INVALID_TOKEN");
                    String ret = JSONObject.toJSONString(Result.error("INVALID_TOKEN"));
                    res.getWriter().write(ret);
                } else {
                    // 4.2 解析jwt成功，放行
                    log.info("请求并解析jwt成功, payload: {}, 允许放行", payload);
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }
        // 5. 业务逻辑处理完毕后，响应拦截
        log.info("response拦截成功, 业务处理完毕\n\n");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("destroy filter success");
    }
}
