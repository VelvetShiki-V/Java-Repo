package com.vs._2024_10_18.config;

import com.vs._2024_10_18.entity.User;
import com.vs._2024_10_18.exception.SecurityAuthException;
import com.vs._2024_10_18.filter.AuthFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 异常抛出
    private final AuthenticationEntryPoint authException = new SecurityAuthException.AuthException();
    private final AccessDeniedHandler accessException = new SecurityAuthException.AccessException();
    private final Filter globalSecurityException = new SecurityAuthException.SecurityException();

    // 配置认证拦截链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 禁用服务端渲染鉴权过滤器(包含表单登录登出拦截，会话拦截，跨域拦截，匿名身份等)
        http.formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .requestCache(cache -> cache.requestCache(new NullRequestCache()))
            .anonymous(AbstractHttpConfigurer::disable);

        // 鉴权失败异常处理(统一格式响应)
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                // 鉴权失败异常
                .authenticationEntryPoint(authException)
                // 权限不足异常
                .accessDeniedHandler(accessException)
        );

        // security基础过滤器(所有请求默认都需要鉴权)
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        // 自定义过滤器(鉴权过滤器需要放在内置身份认证过滤器之前)
        http.addFilterBefore(new AuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 配置密码加密
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    // 配置鉴权API

    // 用户数据提供与用户信息匹配
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                // authentication对象从数据库中获取用户名和密码信息用于校验
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                // 数据库查询逻辑
                // ...
                // 模拟数据库中加密的密码
                String encodedPassword = passwordEncoder().encode("123");
                User user = new User("1", "admin", "shiki", encodedPassword, "", "", LocalDateTime.now(), LocalDateTime.now());
                // 密码会自动加密与数据库中加密的密码进行匹配
                if(passwordEncoder().matches(password, user.getPassword())) {
                    System.out.println("用户登陆成功");
                    return new UsernamePasswordAuthenticationToken(username, password)
                } else {
                    // 鉴权失败，密码错误
                    System.out.println("密码错误");
                    throw new BadCredentialsException("密码错误");
                }
            }

            // 用于确定该AuthenticationProvider是否能够处理给定类型的Authentication对象
            @Override
            public boolean supports(Class<?> authentication) {
                return false;
            }
        };
    }
}
