package com.vs._2024_10_18.security.config;

import com.vs._2024_10_18.security.authentication.UserAuthenticationProvider;
import com.vs._2024_10_18.security.exception.SecurityAuthException;
import com.vs._2024_10_18.security.filter.UserAuthFilter;
import com.vs._2024_10_18.security.handler.LoginFailHandler;
import com.vs._2024_10_18.security.handler.LoginSuccessHandler;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 配置密码加密
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Autowired
    private ApplicationContext applicationContext;

    // 异常抛出
    private final AuthenticationEntryPoint authException = new SecurityAuthException.AuthException();
    private final AccessDeniedHandler accessException = new SecurityAuthException.AccessException();
    private final Filter globalSecurityException = new SecurityAuthException.SecurityException();

    // filter通用配置
    // 禁用服务端渲染鉴权过滤器(包含表单登录登出拦截，会话拦截，跨域拦截，匿名身份等)
    private void commonFilterSetting(HttpSecurity http) throws Exception {
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
    }

    // 配置认证拦截链
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {

        commonFilterSetting(http);

        // 依赖注入获取失败成功处理器
        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);

        // 指定需要进行登录校验的路径
        http.securityMatcher("/login/").authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        // security基础过滤器(所有请求默认都需要鉴权)
        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/free/**", "/doc.html")   // 指定路径url无需鉴权
//                .permitAll()
                .anyRequest()
                .authenticated());

        // 自定义过滤器(鉴权过滤器需要放在内置身份认证过滤器之前)
        // 新增自定义用户名密码校验过滤器(设置登录路径，校验处理器和成功失败处理器)
        UserAuthFilter userAuthFilter = new UserAuthFilter(
                // 必须用post请求
                new AntPathRequestMatcher("/login", HttpMethod.POST.name()),
                new ProviderManager(List.of(applicationContext.getBean(UserAuthenticationProvider.class))),
                loginSuccessHandler, loginFailHandler);
        http.addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 返回新的定制过滤器链
        return http.build();
    }

    // 配置鉴权路径拦截链
    @Bean
    public SecurityFilterChain authAPIFilterChain(HttpSecurity http) throws Exception {
        commonFilterSetting(http);
        http.securityMatcher("/auth/*").authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        // jwt parse
//        http.addFilterBefore();
        return http.build();
    }

    // 配置开放路径拦截链
    @Bean
    public SecurityFilterChain freeAPIFilterChain(HttpSecurity http) throws Exception {
        commonFilterSetting(http);
        http.securityMatcher("/free/*").authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
}
