package com.vs._2024_10_18.security.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.vs._2024_10_18.exception.CustomException;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 该filter不会拦截请求，验证成功后会放行
public class JWTAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        logger.debug("cut into jwt auth filter");
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasLength(token)) {
//            throw new CustomException("401", "不存在合法token", HttpStatus.UNAUTHORIZED);
            throw new BadCredentialsException("不存在合法token");
        }
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            // 从token解析出payload存入authentication
            JWT payload = JWTUtil.parseToken(token);
            // TODO: JWT 解析
        } catch (Exception e) {

        }
    }
}
