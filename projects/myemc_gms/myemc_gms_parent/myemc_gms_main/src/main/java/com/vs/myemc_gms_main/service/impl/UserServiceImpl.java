package com.vs.myemc_gms_main.service.impl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.common.CustomException;
import com.vs.myemc_gms_main.mapper.UserMapper;
import com.vs.myemc_gms_main.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.vs.pojo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-30
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String loginService(User user) {
        log.info("**********用户登录请求**********");
        // TODO: 缓存穿透
        log.info("接收到登录用户信息: {}", user);
        User loginUser = Db.lambdaQuery(User.class).eq(User::getUsername, user.getUsername()).one();
        if(loginUser == null) throw new CustomException.AccessDeniedException("登录用户不存在");
        else {
            // 用户存在，对比密码
            if (!loginUser.getPassword().equals(user.getPassword())) throw new CustomException.AccessDeniedException("登录密码错误");
            else {
                // 用户存在, 生成jwt返回
                Map<String, Object> loginUserMap = new HashMap<>();
                loginUserMap.put("uid", loginUser.getUid());
                loginUserMap.put("username", loginUser.getUsername());
                String token = JwtUtil.jwtGen(loginUserMap);
                log.info("\n生成jwt: {}", token);
                return token;
            }
        }
    }
}
