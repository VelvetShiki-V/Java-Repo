package com.vs._2024_10_18.security.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.model.Result;
import com.vs._2024_10_18.security.jwt.JwtUtil;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

//    @Autowired
//    private ApplicationEventPublisher publisher;

    public LoginSuccessHandler() {
        this.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
                // 无服务端渲染无需重定向
            }
        });
    }

    // 生成token返给前端
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException
    {
        // 登录成功将payload提取作为jwt参数传入
        Object principal = authentication.getPrincipal();
        if(principal == null || !(principal instanceof LoginUserPayload)) {
            throw new RuntimeException("登陆失败，principal类型错误或为空");
        }
        LoginUserPayload payload = (LoginUserPayload) principal;

        // 生成token
        Map<String, Object> responseData = new HashMap<>();
        Map<String, Object> payloadMap = new HashMap<>();
        BeanUtil.beanToMap(payload, payloadMap, CopyOptions.create().setIgnoreNullValue(true));
        String token = JwtUtil.generateJWT(payloadMap);
        responseData.put("token", token);

        // 响应
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        Result responseResult = Result.builder()
                .code("200")
                .msg("登录成功")
                .data(responseData)
                .build();
        writer.print(JSONUtil.toJsonStr(responseResult));
        writer.flush();
        writer.close();
    }
}
