//package com.vs.article.config;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class SaTokenConfig implements WebMvcConfigurer {
//    // 注册拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验
//        registry.addInterceptor(new SaInterceptor(handler -> {
//                    SaRouter
//                            .match("/**")
//                            .notMatch("/articles/**", "/favicon.ico", "/articles/v3/api-docs")
//                            .check(r -> StpUtil.checkLogin());
////                    SaRouter.match("/articles/**", r -> StpUtil.checkPermission("articles"));
////                    SaRouter.match("/adminArticles/**", r -> StpUtil.checkPermission("admin"));
//                }))
//                .addPathPatterns("/**")
//                .excludePathPatterns("/articles/**", "/favicon.ico", "/articles/v3/api-docs");
//    }
//}
