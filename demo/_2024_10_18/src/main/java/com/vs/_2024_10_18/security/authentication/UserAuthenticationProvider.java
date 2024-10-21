package com.vs._2024_10_18.security.authentication;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.vs._2024_10_18.entity.User;
import com.vs._2024_10_18.service.UserService;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // 用户数据提供与用户信息匹配
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // authentication对象从数据库中获取用户名和密码信息用于校验
        String username = authentication.getName();     // 用于数据库查找
        String password = authentication.getCredentials().toString();
        // 数据库查询逻辑
        // ...
        User user = userService.getUserFromDB();
        if(user == null) throw new BadCredentialsException("用户不存在");
        // 密码会自动加密与数据库中加密的密码进行匹配
        if(passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("用户存在");
            // 提取用户信息用于token payload装载
            UserAuthentication token = new UserAuthentication();
            // 提取user信息给payload载入jwt中
            LoginUserPayload payload = new LoginUserPayload();
            BeanUtil.copyProperties(user, payload, CopyOptions.create().setIgnoreNullValue(true));
            token.setPayload(payload);
            token.setAuthenticated(true);
            // 移交至success handler处理返回给前端
            return token;
        } else {
            // 鉴权失败，密码错误
            System.out.println("密码错误");
            throw new BadCredentialsException("密码错误");
        }
    }

    // 用于确定该AuthenticationProvider是否能够处理给定类型的Authentication对象
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserAuthentication.class);
    }
}
