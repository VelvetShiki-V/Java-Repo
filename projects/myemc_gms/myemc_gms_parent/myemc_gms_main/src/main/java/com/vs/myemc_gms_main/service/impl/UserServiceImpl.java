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
import com.vs.pojo.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import static com.vs.common.GlobalConstants.*;

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
        String key = JWT_PREFIX + user.getUsername();
        String ret = RedisUtil.queryString(key);
        if(StrUtil.isNotBlank(ret)) {
            // 刷新登录时间
            RedisUtil.refreshTTL(key, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
            return Result.success("用户已登录", ret);
        } else {
            // 如果缓存存在则为非法空用户
            if(Objects.equals(ret, CACHE_NULL)) throw new CustomException.AccessDeniedException("非法用户重复登录");
            // 否则查询数据库
            User loginUser = Db.lambdaQuery(User.class).eq(User::getUsername, user.getUsername()).one();
            if(loginUser == null) {
                // 缓存穿透预防
                RedisUtil.setObject(key, CACHE_NULL, CACHE_NULL_TTL, TimeUnit.MINUTES);
                throw new CustomException.AccessDeniedException("登录用户不存在");
            } else {
                // 用户存在，对比密码
                if (!loginUser.getPassword().equals(user.getPassword())) throw new CustomException.AccessDeniedException("登录密码错误");
                else {
                    // 用户存在, 生成jwt返回
                    log.info("数据库查询用户存在，开始发放令牌");
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
        log.info("**********用户信息查询请求**********");
        if(uid == null) {
            log.info("查询所有用户");
            List<User> list = (List<User>) RedisUtil.queryTTLWithDB(QUERY_USER_ALL, List.class,
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    (args) -> list());
            if(list != null) return Result.success("获取所有用户信息", list);
        } else {
            log.info("查询单个用户");
            // 查缓存，再查数据库
            User user = (User) RedisUtil.queryTTLWithDB(QUERY_USER_PREFIX + uid, User.class,
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    args -> getById(String.valueOf(args[0])) , uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }
        return Result.error(404, "用户不存在");
    }
}
