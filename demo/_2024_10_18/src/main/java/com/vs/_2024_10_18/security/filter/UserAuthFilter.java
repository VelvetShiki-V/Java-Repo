package com.vs._2024_10_18.security.filter;

import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.security.authentication.UserAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

// 该filter验证完成或失败后直接响应给前台
public class UserAuthFilter extends AbstractAuthenticationProcessingFilter {

    // logback，不依赖于lombok注解
    private static final Logger logger = LoggerFactory.getLogger(UserAuthFilter.class);

    // 构造器由config传入：匹配的登录路径/login, 验证管理器，登录成功和失败处理器（统一Result格式返回）
    public UserAuthFilter(
            AntPathRequestMatcher matcher,
            AuthenticationManager manager,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler)
    {
        super(matcher);
        setAuthenticationManager(manager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException {
        // 提取post body请求数据
        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> requestMap = JSONUtil.toBean(requestJson, Map.class);
        // 规定请求体格式为json风格的用户名和密码，可根据需求更改为邮箱或手机验证码
        String username = requestMap.get("username").toString();
        String password = requestMap.get("password").toString();

        // 将请求携带用户信息封装成security(AbstractAuthenticationToken对象)需要的用户数据对象
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(username);
        userAuthentication.setPassword(password);
        userAuthentication.setAuthenticated(false);     // 还未进行登录验证，所以设置为false，验证成功后再设置为true
        // 开始验证身份，进入AuthenticationProvider进行数据库信息比对
        return getAuthenticationManager().authenticate(userAuthentication);
    }
}
