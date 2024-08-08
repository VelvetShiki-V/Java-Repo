package com.vs.blogsystem_v1.config;

import com.vs.blogsystem_v1.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // 全局配置类
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestInterceptor requestInterceptor;

    // 添加请求访问拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);

        // 如果访问url为login，直接放行; 其余url则进行拦截
        registry
                .addInterceptor(requestInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login", "/users/registry");
    }
}
