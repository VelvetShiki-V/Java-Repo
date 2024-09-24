package com.vs.cloud_api.config;

import cn.hutool.core.util.StrUtil;
import com.vs.cloud_api.fallback.CloudUserClientFallbackFactory;
import com.vs.cloud_common.utils.UserThreadLocalUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    // openFeign请求拦截器
    // 从微服务生成新请求到其他微服务之间需要传递的用户信息附加到请求头上（信息来源于线程内部的本地存储threadLocal）
    @Bean
    public RequestInterceptor userRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                log.info("\n-----------------openFeign请求拦截-----------------");
                log.warn("将要生成新请求至目标权限路径: {}", requestTemplate.path());
                log.warn("需要确保业务线程上下文存储了正确和完整的用户信息");
                String uid = UserThreadLocalUtil.getUserInfo().getUid();
                String name = UserThreadLocalUtil.getUserInfo().getName();
                log.info("获取到用户上下文 uid: {}, name: {}", uid, name);
                if(StrUtil.isNotBlank(uid) && StrUtil.isNotBlank(name)) {
                    requestTemplate.header("uid", uid);
                    requestTemplate.header("name", name);
                } else {
                    log.error("用户上下文信息uid, name不存在，请检查openFeign配置和common interceptor拦截器配置");
                }
            }
        };
    }

    // 使服务降级回调工厂注册为bean生效
    @Bean
    public CloudUserClientFallbackFactory cloudUserClientFallbackFactory() {
        return new CloudUserClientFallbackFactory();
    }
}
