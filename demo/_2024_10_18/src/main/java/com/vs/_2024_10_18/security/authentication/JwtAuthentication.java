package com.vs._2024_10_18.security.authentication;

import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private String jwt;     // 前端请求token值
    private LoginUserPayload payload;       // 数据库获取用户信息

    public JwtAuthentication() {
        super(null);
    }

    // spring security设计授权成功后，credential如密码信息需要被清空
    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : jwt;
    }

    // spring security设计principal授权成功后，返回当前登录用户的信息
    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? payload : jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public LoginUserPayload getPayload() {
        return payload;
    }

    public void setPayload(LoginUserPayload payload) {
        this.payload = payload;
    }
}
