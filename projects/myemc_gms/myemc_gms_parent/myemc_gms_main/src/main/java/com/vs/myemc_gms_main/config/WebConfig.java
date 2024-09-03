package com.vs.myemc_gms_main.config;

import com.vs.myemc_gms_main.interceptor.AccessInterceptor;
import com.vs.myemc_gms_main.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestInterceptor requestInterceptor;

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有请求拦截器
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**")
                .order(0);

        // 权限请求拦截器
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/auth/verify/login", "/auth/registry")
                .order(1);
    }
}
