package com.vs.cloud_user.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.cloud_common.domain.UserInfo;
import com.vs.cloud_common.utils.JwtUtil;
import com.vs.cloud_common.utils.RedisUtil;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_common.utils.SnowFlakeIdUtil;
import com.vs.cloud_common.utils.UserThreadLocalUtil;
import com.vs.cloud_user.domain.User;
import com.vs.cloud_common.exception.CustomException;
import com.vs.cloud_user.mapper.UserMapper;
import com.vs.cloud_user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.vs.cloud_common.constants.GlobalConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final StringRedisTemplate template;
    private final RedissonClient client;
    private final RabbitTemplate rabbitTemplate;

    // 用户登录
    @Override
    public Result loginService(User user) {
        log.info("**********用户登录请求**********");
        log.info("接收到登录用户信息: {}", user);
        // 登录信息判断
        if(StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getPassword()))
            throw new CustomException(HttpStatus.UNAUTHORIZED, "用户名或密码为空");
        // 缓存查询
        String key = JWT_PREFIX + user.getName();
        String token = RedisUtil.query(template, key, new TypeReference<String>() {});
        log.info("缓存检索用户{}: {}", key, token);
        if(token != null && !CACHE_NULL.equals(token)) {
            RedisUtil.refreshTTL(template, key, REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES);
            return Result.success("用户已登录", token);
        } else if (CACHE_NULL.equals(token))
            throw new CustomException(HttpStatus.FORBIDDEN, "非法用户重复登录");
        // 数据库查询
        User loginUser = Db.lambdaQuery(User.class).eq(User::getName, user.getName()).one();
        log.info("数据库检索用户: {}", loginUser);
        if(loginUser == null) {
            RedisUtil.set(template, key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            throw new CustomException(HttpStatus.FORBIDDEN, "登录用户不存在");
        }
        if(!loginUser.getRole().equals("admin")) {
            RedisUtil.set(template, key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            throw new CustomException(HttpStatus.FORBIDDEN, "非管理员用户禁止登录");
        }
        if(!loginUser.getPassword().equals(user.getPassword()))
            throw new CustomException(HttpStatus.UNAUTHORIZED, "密码错误");
        // 用户存在, 生成jwt返回
        log.info("管理员认证成功, 开始发放令牌");
        Map<String, Object> loginUserMap = new HashMap<>();
        loginUserMap.put("uid", loginUser.getUid());
        loginUserMap.put("name", loginUser.getName());
        token = JwtUtil.jwtGen(template, loginUserMap);
        log.info("\n生成jwt: {}", token);
        // 登录信息 + token存入缓存
        RedisUtil.set(template, key, token, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
        return Result.success("登录成功", token);
    }

    // 验证登录
    @Override
    public Result loginVerify() {
        // 测试慢请求
//        ThreadUtil.sleep(1000);
        UserInfo info = UserThreadLocalUtil.getUserInfo();
        log.info("threadLocal获取到用户信息: {}", info);
        if(info == null || (StrUtil.isBlank(info.getName()) && StrUtil.isBlank(info.getUid())))
            throw new CustomException(HttpStatus.UNAUTHORIZED, "token无效，用户校验失败");
        return Result.success("token有效, 用户已登录", null);
    }

    // 用户查询
    @Override
    public Result userQuery(String uid) {
        log.info("**********用户信息查询请求**********");
        if(StrUtil.isBlank(uid)) {
            log.info("查询所有用户");
            List<User> userList = RedisUtil.queryTTLWithDB(template, QUERY_USER_ALL, new TypeReference<List<User>>() {},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES, args -> list());
            if(userList != null) return Result.success("获取到所有用户信息", userList);
        } else {
            log.info("查询单个用户");
            User user = RedisUtil.queryTTLWithDB(template, QUERY_USER_PREFIX + uid, new TypeReference<User>() {},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES, args -> getById(String.valueOf(args[0])), uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "用户不存在");
    }

    // 用户创建
    @Override
    public Result userCreate(User user) {
        log.info("**********用户创建请求**********");
        log.info("接收到用户信息: {}", user);
        if(StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "用户名或密码为空");
        }
        // mybatisX雪花算法生成uid
        if(StrUtil.isBlank(user.getUid())) {
            String SFId = SnowFlakeIdUtil.generateUid();
            user.setUid(SFId);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setCreateTime(localDateTime);
        user.setUpdateTime(localDateTime);
        try {
            save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户创建失败" + e.getMessage());
        }
        // 缓存同步删除
        cacheRemoveSync(QUERY_USER_ALL, new TypeReference<List<User>>() {});
        // addon业务：mq通知数据同步创建
        try {
            log.info("mq准备发送异步通信数据给modelService");
            rabbitTemplate.convertAndSend("cloud.topic",
                    "k1", new UserInfo(user.getUid(), user.getName()));
        } catch(Exception e) {
            throw new RuntimeException("异步数据创建失败");
        }
        return Result.success("用户创建成功", null);
    }

    // 用户删除
    @Override
    public Result userDelete(String uid) {
        log.info("**********用户删除请求**********");
        // 查询缓存
        User deleteUser = getById(uid);
        if(deleteUser == null) throw new CustomException(HttpStatus.NOT_FOUND, "删除用户不存在");
        log.info("用户存在，开始执行删除操作");
        // 分布式锁控制删除
        boolean isRemoved = RedisUtil.taskLock(client,
                args -> removeById(String.valueOf(args[0])), REMOVE_USER_PREFIX + uid, uid);
        if(!isRemoved)
            throw new RuntimeException(String.format("用户uid: %s删除失败", uid));
        // 缓存同步删除
        cacheRemoveSync(QUERY_USER_PREFIX + uid, new TypeReference<User>() {});
        cacheRemoveSync(QUERY_USER_ALL, new TypeReference<List<User>>() {});
        return Result.success(String.format("user:uid:%s删除成功", uid), null);
    }

    // 用户信息更新
    @Override
    public Result userUpdate(User user) {
        log.info("**********用户信息更新请求**********");
        // 查询数据库
        User updateUser = getById(user.getUid());
        if(updateUser == null) throw new CustomException(HttpStatus.NOT_FOUND, "更新用户不存在");
        log.info("用户存在，开始执行更新操作");
        boolean isUpdateSuccess = RedisUtil.taskLock(client, args -> Db.lambdaUpdate(User.class)
                .set(User::getName, user.getName())
                .set(User::getRole, user.getRole())
                .set(User::getPassword, user.getPassword())
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getUid, user.getUid())
                .update(), UPDATE_USER_PREFIX + user.getUid(), user.getUid());
        if(!isUpdateSuccess) throw new RuntimeException("用户更新失败");
        // 缓存同步删除
        cacheRemoveSync(QUERY_USER_PREFIX + user.getUid(), new TypeReference<User>() {});
        cacheRemoveSync(QUERY_USER_ALL, new TypeReference<List<User>>() {});
        return Result.success("更新成功", null);
    }

    // 缓存同步策略
    private <R> void cacheRemoveSync(String key, TypeReference<R> typeRef) {
        R ret = RedisUtil.query(template, key, typeRef);
        if(ret != null) {
            RedisUtil.removeKey(template, key);
            log.warn("缓存{}已同步删除", key);
        }
    }
}
