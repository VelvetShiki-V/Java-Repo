package com.vs.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayAuthFilterConfig {
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/auth/**",
                        "/articles/**",
                        "/sso/**",
                        "/oauth2/**",
                        "/user/userInfo",
                        "/categories/**",
                        "/tags/**")
                .setAuth(obj -> {
                    // redis查询
                    SaRouter.match("/**")
                            .notMatch("*.html", "*.css", "*.js", "*.ico")
                            .check( r -> StpUtil.checkLogin() );
                    // 用户权限拦截
                    SaRouter.match("/admin/**", r -> {
                        // 权限验证
                        System.out.println("拥有权限码: ");
                        StpUtil.getPermissionList(StpUtil.getLoginId()).stream().forEach(System.out::println);
                        System.out.println("是否拥有admin权限: " + StpUtil.hasPermission("admin.*"));
                        StpUtil.checkPermission("admin.*");
                        // 角色验证
                        System.out.println("拥有角色: ");
                        StpUtil.getRoleList(StpUtil.getLoginId()).stream().forEach(System.out::println);
                        System.out.println("是否拥有admin角色: " + StpUtil.hasRole("admin"));
                        StpUtil.checkRole("admin");
                    });
                    SaRouter.match("/user/**", r -> {
                        StpUtil.checkRole("admin");
                        StpUtil.checkPermission("admin.*");
                    });
                }).setError(e -> SaResult.error(e.getMessage()));
    }
}
