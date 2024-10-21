package com.vs._2024_10_18.security.filter;

import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.security.authentication.UserAuthentication;
import jakarta.servlet.ServletException;
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

    private static final Logger logger = LoggerFactory.getLogger(UserAuthFilter.class);

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
            throws AuthenticationException, IOException, ServletException
    {
        logger.debug("use UserAuthFilter");

        // 提取请求数据
        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> requestMap = JSONUtil.toBean(requestJson, Map.class);
        String username = requestMap.get("username").toString();
        String password = requestMap.get("password").toString();

        // 将请求携带用户信息封装成security(AbstractAuthenticationToken对象)需要的用户数据
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(username);
        userAuthentication.setPassword(password);
        userAuthentication.setAuthenticated(false);     // 还未进行登录验证，所以设置为false，验证成功后再设置为true

        System.out.println("获取到用户信息map: " + userAuthentication);

        // 开始验证身份，进入AuthenticationProvider进行数据库信息比对
        return getAuthenticationManager().authenticate(userAuthentication);
    }
}
