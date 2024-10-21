package com.vs._2024_10_18.security.authentication;

import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthentication extends AbstractAuthenticationToken {

    private String username;
    private String password;
    private LoginUserPayload payload;

    public UserAuthentication() {
        super(null);
    }

    // 获取登录用户密码
    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : password;
    }

    // 获取登录用户名
    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? payload : username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginUserPayload getPayload() {
        return payload;
    }

    public void setPayload(LoginUserPayload payload) {
        this.payload = payload;
    }
}
