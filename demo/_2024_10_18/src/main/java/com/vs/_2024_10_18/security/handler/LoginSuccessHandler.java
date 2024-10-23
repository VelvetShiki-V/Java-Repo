package com.vs._2024_10_18.security.handler;

import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.model.Result;
import com.vs._2024_10_18.security.jwt.JwtUtil;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import com.vs._2024_10_18.security.jwt.RsaUtil;
import com.vs._2024_10_18.utils.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    // 用于设置缓存
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 生成token返给前端
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 登录成功将payload提取作为jwt参数传入
        Object principal = authentication.getPrincipal();
        if(principal == null || !(principal instanceof LoginUserPayload)) {
            throw new RuntimeException("登录异常，principal类型错误或为空");
        }
        // 生成token
        LoginUserPayload payload = (LoginUserPayload) principal;
        Map<String, Object> responseData = new HashMap<>();
        // 从文件中获取私钥用于token签名
        String privateKeyPath = ResourceUtils.getFile(ResourceUtils
                .CLASSPATH_URL_PREFIX + "static/rsa.pri").getPath();
        logger.debug("get private key path: " + privateKeyPath);
        PrivateKey privateKey;
        try {
            privateKey = RsaUtil.loadPrivateKey(privateKeyPath);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        logger.debug("get existed private key: " + privateKey);
        String token = JwtUtil.generateJWT(payload, privateKey, 60);
        responseData.put("token", token);
        // 存入redis缓存，避免数据库重复查询用户
        RedisUtil.set(stringRedisTemplate, RedisUtil.USER_LOGIN_PREFIX + payload.getUsername(),
                payload, 60L, TimeUnit.MINUTES);

        // 格式化Result响应
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
