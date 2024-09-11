package com.vs.cloud_user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.cloud_common.utils.JwtUtil;
import com.vs.cloud_common.utils.RedisUtil;
import com.vs.cloud_user.domain.Result;
import com.vs.cloud_user.exception.CustomException;
import com.vs.cloud_user.domain.User;
import com.vs.cloud_user.mapper.UserMapper;
import com.vs.cloud_user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final StringRedisTemplate tempalte;

    // 用户登录
    @Override
    public Result loginService(User user) {
        log.info("**********用户登录请求**********");
        log.info("接收到登录用户信息: {}", user);
        if(StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getPassword())) {
            throw new CustomException.BadRequestException("用户名或密码为空");
        }
        User loginUser = Db.lambdaQuery(User.class).eq(User::getName, user.getName()).one();
        if(loginUser == null) {
            throw new CustomException.AccessDeniedException("登录用户不存在");
        }
        if(!loginUser.getRole().equals("admin")) {
            throw new CustomException.AccessDeniedException("非管理员用户禁止登录");
        }
        if(!loginUser.getPassword().equals(user.getPassword())) {
            throw new CustomException.AccessDeniedException("密码错误");
        }
        // 用户存在, 生成jwt返回
        log.info("数据库查询用户存在，开始发放令牌");
        Map<String, Object> loginUserMap = new HashMap<>();
        loginUserMap.put("name", loginUser.getName());
        String token = JwtUtil.jwtGen(loginUserMap);
        log.info("\n生成jwt: {}", token);
        // 登录信息存入缓存
        RedisUtil.set(tempalte, key, token, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
        return Result.success("登录成功", loginUser.getUid());

        // 查缓存，如果存在就返回，否则查数据库
/*        String key = JWT_PREFIX + user.getName();
        String ret = (String) RedisUtil.query(key, new TypeReference<String>(){});
        if(StrUtil.isNotBlank(ret)) {
            // 刷新登录时间
            RedisUtil.refreshTTL(key, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
            return Result.success("用户已登录", ret);
        } else {
            // 如果缓存存在则为非法空用户
            if(Objects.equals(ret, CACHE_NULL)) throw new CustomException.AccessDeniedException("非法用户重复登录");
            // 否则查询数据库
            UserController loginUser = Db.lambdaQuery(UserController.class).eq(UserController::getName, user.getName()).one();
            if(loginUser == null) {
                // 缓存穿透预防
                log.warn("数据库查无此人{}", user.getName());
                RedisUtil.set(key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
                throw new CustomException.AccessDeniedException("登录用户不存在");
            } else {
                // 用户存在，对比密码
                if (!loginUser.getPassword().equals(user.getPassword())) throw new CustomException.AccessDeniedException("登录密码错误");
                else {
                    // 用户存在, 生成jwt返回
                    log.info("数据库查询用户存在，开始发放令牌");
                    Map<String, Object> loginUserMap = new HashMap<>();
                    loginUserMap.put("name", loginUser.getName());
                    String token = JwtUtil.jwtGen(loginUserMap);
                    log.info("\n生成jwt: {}", token);
                    // 登录信息存入缓存
                    RedisUtil.set(key, token, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
                    return Result.success("登录成功", token);
                }
            }*/
        }


    // 用户查询
    @Override
    public Result userQuery(String uid) {
        log.info("**********用户信息查询请求**********");
        if(StrUtil.isBlank(uid)) {
            log.info("查询所有用户");
            List<User> userList = list();
            if(userList != null) return Result.success("获取到所有用户信息", userList);
        } else {
            log.info("查询单个用户");
            User user = getById(uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }
/*        if(uid == null) {
            log.info("查询所有用户");
            List<UserController> list = RedisUtil.queryTTLWithDB(QUERY_USER_ALL, new TypeReference<>() {
                    },
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    (args) -> list());
            if(list != null) return Result.success("获取所有用户信息", list);
        } else {
            log.info("查询单个用户");
            // 查缓存，再查数据库
            UserController user = RedisUtil.queryTTLWithDB(QUERY_USER_PREFIX + uid, new TypeReference<>() {
                    },
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    args -> getById(String.valueOf(args[0])) , uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }*/
        throw new CustomException.DataNotFoundException("用户不存在");
    }

    // 用户创建
    @Override
    public Result userCreate(User user) {
        log.info("**********用户创建请求**********");
        log.info("接收到用户信息: {}", user);
        if(StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getPassword())) {
            throw new CustomException.BadRequestException("用户名或密码为空");
        }
        // mybatisX雪花算法生成uid
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setCreateTime(localDateTime);
        user.setUpdateTime(localDateTime);
        save(user);

        // 缓存穿透
/*        if(RedisUtil.keyAlive(CREATE_USER_PREFIX + user.getName()) == keyStatus.EMPTY)
            throw new CustomException.AccessDeniedException(String.format("用户%s重复提交注册表单，请求拒绝", user.getName()));
         LocalDateTime localDateTime = LocalDateTime.now();
         user.setCreateTime(localDateTime);
         user.setUpdateTime(localDateTime);
        try {
            save(user);
        } catch (Exception e) {
            // FIXME: 异常捕获SQLIntegrityConstraintViolationException
            RedisUtil.set(CREATE_USER_PREFIX + user.getName(), CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            throw new CustomException.AccessDeniedException("用户创建失败: " + e.getCause());
        }
        // 缓存处理防止重复提交
        RedisUtil.set(CREATE_USER_PREFIX + user.getName(), CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);*/
        return Result.success("用户创建成功", null);
    }

    // 用户删除
    @Override
    public Result userDelete(String uid) {
        log.info("**********用户删除请求**********");
        if(uid == null) throw new CustomException.AccessDeniedException("uid为空，无法删除");
        if(!removeById(uid)) {
            return Result.error("删除失败");
        }
        // 缓存查询
/*        String rmKey = REMOVE_USER_PREFIX + uid;
        keyStatus status = RedisUtil.keyAlive(REMOVE_USER_PREFIX + uid);
        if(status == keyStatus.EMPTY) throw new CustomException.AccessDeniedException("用户重复删除，数据层访问已拒绝");
        // 数据库查询
        boolean isRemoved = RedisUtil.taskLock((args) -> removeById((String) args[0]), REMOVE_USER_PREFIX + uid, uid);
        if(!isRemoved) throw new CustomException.DataNotFoundException(String.format("用户uid: %d不存在，删除失败", uid));
        // TODO: add key缓存同步删除
        // 查询缓存同步删除
        String key = QUERY_USER_PREFIX + uid;
        UserController query = (UserController) RedisUtil.query(key, new TypeReference<UserController>() {});
        if(query != null) {
            RedisUtil.removeKey(key);
            log.warn("缓存{}已同步删除", key);
        }
        // 添加rmId防止缓存穿透
        log.info("缓存穿透策略已更新 {}", rmKey);
        RedisUtil.set(rmKey, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);*/
        return Result.success(String.format("user:uid:%s删除成功", uid), null);
    }

    // 用户信息更新
    @Override
    public Result userUpdate(User user) {
        log.info("**********用户信息更新请求**********");
        // 查询数据库
        User updateUser = getById(user.getUid());
        if(updateUser == null) throw new CustomException.AccessDeniedException("更新用户不存在");
        Db.lambdaUpdate(User.class)
                .set(User::getName, user.getName())
                .set(User::getRole, user.getRole())
                .set(User::getPassword, user.getPassword())
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getUid, user.getUid())
                .update();
        return Result.success("更新成功", null);
    }
}
