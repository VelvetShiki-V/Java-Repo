package com.vs._2024_10_18.security.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.vs._2024_10_18.entity.User;
import com.vs._2024_10_18.security.authentication.UserAuthentication;
import com.vs._2024_10_18.service.UserService;
import com.vs._2024_10_18.security.jwt.LoginUserPayload;
import com.vs._2024_10_18.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 用于身份验证处理器
@Component
@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 用户数据提供与用户信息匹配
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // authentication对象从数据库中获取用户名和密码信息用于校验
        String username = authentication.getName();     // 用于数据库查找
        String password = authentication.getCredentials().toString();
        User user;
        // 数据库查询逻辑
        // ...
        // 如果用户已登录，即从缓存中查到用户信息，直接从缓存获取解析并包装为payload，用于后续token生成即可
        String userCache = RedisUtil.query(stringRedisTemplate,
                RedisUtil.USER_LOGIN_PREFIX + username, new TypeReference<String>() {});
        if(RedisUtil.CACHE_NULL.equals(userCache) || userCache == null) {
            // 缓存用户无，执行数据库查询
            user = userService.getUserFromDB();
            if(user == null) {
                log.error("数据库用户不存在");
                throw new BadCredentialsException("用户不存在");
            }
            // 密码会自动加密与数据库中加密的密码进行匹配
            if(passwordEncoder.matches(password, user.getPassword())) {
                log.debug("数据库用户存在");
            } else {
                // 鉴权失败，密码错误
                log.error("密码错误");
                throw new BadCredentialsException("密码错误");
            }
        } else {
            // 缓存查到，取出赋值给authenticaiton
            log.info("缓存查到用户: {}", userCache);
            user = JSONUtil.toBean(userCache, User.class);
        }
        // 提取用户信息用于token payload装载
        UserAuthentication tokenAuthentication = new UserAuthentication();
        // 提取user信息给payload，准备载入jwt中
        LoginUserPayload payload = new LoginUserPayload();
        BeanUtil.copyProperties(user, payload, CopyOptions.create().setIgnoreNullValue(true));
        tokenAuthentication.setPayload(payload);
        tokenAuthentication.setAuthenticated(true);
        // 移交至success handler处理返回给前端
        return tokenAuthentication;
    }

    // 用于确定该AuthenticationProvider是否能够处理给定类型的Authentication对象
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserAuthentication.class);
    }
}
