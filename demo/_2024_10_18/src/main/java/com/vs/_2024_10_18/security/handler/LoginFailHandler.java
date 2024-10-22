package com.vs._2024_10_18.security.handler;

import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.model.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    // 登录失败抛出异常
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException
    {
        String errorMessage = exception.getMessage();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        Result responseResult = Result.builder()
                .code("401")
                .msg("login fail: " + errorMessage)
                .data(null)
                .build();
        writer.print(JSONUtil.toJsonStr(responseResult));
        writer.flush();
        writer.close();
    }
}
