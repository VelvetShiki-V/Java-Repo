package com.vs.auth.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 指定match规则
            SaRouter
            .match("/**")
            .notMatch("/sso/email/login", "/favicon.ico", "/doc.html")
            .check(r -> StpUtil.checkLogin());

            // 根据路由划分不同微服务鉴权(路由 + 鉴权函数)
            SaRouter.match("/user/**", r -> StpUtil.checkPermission("admin"));
            SaRouter.match("/articles/**", r -> StpUtil.checkPermission("articles"));
            SaRouter.match("/admin/**", r-> StpUtil.checkPermission("admin"));
        })).addPathPatterns("/**").excludePathPatterns("/sso/email/login", "/favicon.ico", "/doc.html");
    }
}
