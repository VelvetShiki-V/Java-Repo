package com.vs._2024_10_18.security.exception;

import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.exception.CustomException;
import com.vs._2024_10_18.model.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class SecurityAuthException {
    // 鉴权失败异常
    public static class AuthException implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print(JSONUtil.toJsonStr(new Result(Result.ERROR_CODE, "认证失败", null)));
            writer.flush();
            writer.close();
        }
    }

    // 权限不足拦截
    public static class AccessException implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter writer = response.getWriter();
            writer.print(JSONUtil.toJsonStr(new Result(Result.ERROR_CODE, "access denied", null)));
            writer.flush();
            writer.close();
        }
    }

    // security调用链内部未知异常
    public static class SecurityException extends OncePerRequestFilter {
        public static final Logger logger = LoggerFactory.getLogger(SecurityException.class);

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            try {
                filterChain.doFilter(request, response);
            } catch (CustomException e) {
                // 构建响应体
                Result result = Result.builder()
                        .code(e.getCode())
                        .msg(e.getMessage())
                        .build();
                // 序列化
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(e.getStatus().value());
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtil.toJsonStr(result));
                writer.flush();
                writer.close();
            } catch (AuthenticationException | AccessDeniedException e) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter writer = response.getWriter();
                writer.print(JSONUtil.toJsonStr(Result.builder().msg(e.getMessage())));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                // 未知异常
                logger.error(e.getMessage(), e);
                Result result = new Result(Result.ERROR_CODE, "security未知错误", null);
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter writer = response.getWriter();
                writer.print(JSONUtil.toJsonStr(result));
                writer.flush();
                writer.close();
            }
        }
    }
}
