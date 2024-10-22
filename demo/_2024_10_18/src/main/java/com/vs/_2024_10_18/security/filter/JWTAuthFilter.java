package com.vs._2024_10_18.security.filter;

import com.vs._2024_10_18.exception.CustomException;
import com.vs._2024_10_18.security.authentication.JwtAuthentication;
import com.vs._2024_10_18.security.jwt.JwtUtil;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 该filter不会拦截请求，验证成功后会放行
public class JWTAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasLength(token)) {
            // 需要使用自定义异常被spring security global exception handler捕捉并以Result返回
            throw new CustomException("401", "不存在合法token", HttpStatus.UNAUTHORIZED);
        }
        if(!token.startsWith("Bearer ")) {
            throw new CustomException("401", "token格式错误", HttpStatus.UNAUTHORIZED);
        } else token = token.substring(7);
        try {
            // 验证token合法性
            LoginUserPayload payload = JwtUtil.parseNverifyJWT(token, LoginUserPayload.class);
            // 存入authentication
            JwtAuthentication authentication = new JwtAuthentication();
            authentication.setJwt(token);
            authentication.setPayload(payload);
            authentication.setAuthenticated(true);
            // 将认证信息存入上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("401", "token解析失败", HttpStatus.UNAUTHORIZED);
        }
        // 放行至权限接口
        filterChain.doFilter(request, response);
    }
}
