package com.vs.cloud_common.config;

import com.vs.cloud_common.interceptor.AuthInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class InterceptorConfig implements WebMvcConfigurer {
    // 配置拦截器生效（且仅在mvc微服务中生效，网关不生效）
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }
}
