package com.vs._2024_10_18.security.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.vs._2024_10_18.entity.User;
import com.vs._2024_10_18.security.authentication.UserAuthentication;
import com.vs._2024_10_18.service.UserService;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
        if(user == null) {
            log.error("数据库用户不存在");
            throw new BadCredentialsException("用户不存在");
        }
        // 密码会自动加密与数据库中加密的密码进行匹配
        if(passwordEncoder.matches(password, user.getPassword())) {
            log.debug("用户存在");
            // 提取用户信息用于token payload装载
            UserAuthentication tokenAuthentication = new UserAuthentication();
            // 提取user信息给payload载入jwt中
            LoginUserPayload payload = new LoginUserPayload();
            BeanUtil.copyProperties(user, payload, CopyOptions.create().setIgnoreNullValue(true));
            tokenAuthentication.setPayload(payload);
            tokenAuthentication.setAuthenticated(true);
            // 移交至success handler处理返回给前端
            return tokenAuthentication;
        } else {
            // 鉴权失败，密码错误
            log.error("密码错误");
            throw new BadCredentialsException("密码错误");
        }
    }

    // 用于确定该AuthenticationProvider是否能够处理给定类型的Authentication对象
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserAuthentication.class);
    }
}
