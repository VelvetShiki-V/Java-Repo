package com.vs.myemc_gms_main.service.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.common.CustomException;
import com.vs.myemc_gms_main.mapper.UserMapper;
import com.vs.myemc_gms_main.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.pojo.Result;
import com.vs.utils.JwtUtil;
import com.vs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.vs.common.GlobalConstants.*;

import com.vs.pojo.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public Result loginService(User user) {
        log.info("**********用户登录请求**********");
        log.info("接收到登录用户信息: {}", user);
        // 查缓存，如果存在就返回，否则查数据库
        String ret = RedisUtil.query(JWT_PREFIX + user.getUid());
        if(StrUtil.isNotBlank(ret)) {
            return Result.success("用户已登录", ret);
        } else {
            // 数据库查询
            User loginUser = Db.lambdaQuery(User.class).eq(User::getUsername, user.getUsername()).one();
            if(loginUser == null) throw new CustomException.AccessDeniedException("登录用户不存在");
            else {
                // 用户存在，对比密码
                if (!loginUser.getPassword().equals(user.getPassword())) throw new CustomException.AccessDeniedException("登录密码错误");
                else {
                    // 用户存在, 生成jwt返回
                    log.info("用户存在，开始发放令牌");
                    Map<String, Object> loginUserMap = new HashMap<>();
                    loginUserMap.put("uid", loginUser.getUid());
                    loginUserMap.put("username", loginUser.getUsername());
                    String token = JwtUtil.jwtGen(loginUserMap);
                    log.info("\n生成jwt: {}", token);
                    return Result.success("登录成功", token);
                }
            }
        }
    }

    @Override
    public Result userQuery(Integer uid) {
        if(uid == null) {
            return Result.success("获取所有用户信息", list());
        } else {
            // 获取单个用户信息
            // 查缓存，再查数据库
            User user = RedisUtil.queryTTLWithDB(String.valueOf(uid), User.class, REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    args -> getById(String.valueOf(args[0])) , uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }
        return Result.error(404, "用户不存在");
    }
}
