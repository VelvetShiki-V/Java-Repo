package com.vs.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayTokenConfig {
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/auth/login", "/favicon.ico", "/doc.html")
                .setAuth(obj -> {
                    // 调用自服务查询是否登录
                    SaRouter.match("/**", r -> System.out.println("获取到token: " + StpUtil.getTokenValue()));
                    SaRouter.match("/**", r -> System.out.println("获取到loginId: " + StpUtil.getLoginId()));
                    SaRouter.match("/**", "/auth/login", r -> StpUtil.checkLogin());
                }).setError(e -> SaResult.error(e.getMessage()));
    }
}
