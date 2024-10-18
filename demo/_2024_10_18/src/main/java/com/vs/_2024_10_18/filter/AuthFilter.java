package com.vs._2024_10_18.filter;

import jakarta.servlet.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("enter auth filter...");

        // jwt auth
        // 测试认证通过
        Authentication authentication = new TestingAuthenticationToken("username", "password", "ROLE");
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("exit auth filter");
    }
}
