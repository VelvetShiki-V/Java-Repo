package com.vs.myemc_gms_main.service.impl;
import cn.hutool.core.lang.TypeReference;
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

    // 用户登录
    @Override
    public Result loginService(User user) {
        log.info("**********用户登录请求**********");
        log.info("接收到登录用户信息: {}", user);
        // 查缓存，如果存在就返回，否则查数据库
        String key = JWT_PREFIX + user.getName();
        String ret = (String) RedisUtil.query(key, new TypeReference<String>(){});
        if(StrUtil.isNotBlank(ret)) {
            // 刷新登录时间
            RedisUtil.refreshTTL(key, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
            return Result.success("用户已登录", ret);
        } else {
            // 如果缓存存在则为非法空用户
            if(Objects.equals(ret, CACHE_NULL)) throw new CustomException.AccessDeniedException("非法用户重复登录");
            // 否则查询数据库
            User loginUser = Db.lambdaQuery(User.class).eq(User::getName, user.getName()).one();
            if(loginUser == null) {
                // 缓存穿透预防
                RedisUtil.set(key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
                throw new CustomException.AccessDeniedException("登录用户不存在");
            } else {
                // 用户存在，对比密码
                if (!loginUser.getPassword().equals(user.getPassword())) throw new CustomException.AccessDeniedException("登录密码错误");
                else {
                    // 用户存在, 生成jwt返回
                    log.info("数据库查询用户存在，开始发放令牌");
                    Map<String, Object> loginUserMap = new HashMap<>();
                    // loginUserMap.put("uid", loginUser.getUid());
                    loginUserMap.put("name", loginUser.getName());
                    String token = JwtUtil.jwtGen(loginUserMap);
                    log.info("\n生成jwt: {}", token);
                    return Result.success("登录成功", token);
                }
            }
        }
    }

    // 用户查询
    @Override
    public Result userQuery(Integer uid) {
        log.info("**********用户信息查询请求**********");
        if(uid == null) {
            log.info("查询所有用户");
            List<User> list = RedisUtil.queryTTLWithDB(QUERY_USER_ALL, new TypeReference<>() {
                    },
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    (args) -> list());
            if(list != null) return Result.success("获取所有用户信息", list);
        } else {
            log.info("查询单个用户");
            // 查缓存，再查数据库
            User user = RedisUtil.queryTTLWithDB(QUERY_USER_PREFIX + uid, new TypeReference<>() {
                    },
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    args -> getById(String.valueOf(args[0])) , uid);
            if(user != null) return Result.success("获取到用户信息", user);
        }
        return Result.error(404, "用户不存在");
    }

    // 用户创建
    @Override
    public Result userCreate(User user) {
        log.info("**********用户创建请求**********");
        log.info("接收到用户信息: {}", user);
        // 缓存穿透
        if(RedisUtil.keyAlive(CREATE_USER_PREFIX + user.getName()) == keyStatus.EMPTY)
            throw new CustomException.AccessDeniedException(String.format("用户%s重复提交注册表单，请求拒绝", user.getName()));
        // LocalDateTime localDateTime = LocalDateTime.now();
        // user.setCreateTime(localDateTime);
        // user.setUpdateTime(localDateTime);
        try {
            save(user);
        } catch (Exception e) {
            // FIXME: 异常捕获SQLIntegrityConstraintViolationException
            RedisUtil.set(CREATE_USER_PREFIX + user.getName(), CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            throw new CustomException.AccessDeniedException("用户创建失败: " + e.getCause());
        }
        // 缓存处理防止重复提交
        RedisUtil.set(CREATE_USER_PREFIX + user.getName(), CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
        return Result.success("用户创建成功", null);
    }

    // 用户删除
    @Override
    public Result userDelete(Integer uid) {
        log.info("**********用户删除请求**********");
        if(uid == null) throw new CustomException.AccessDeniedException("uid为空，无法删除");
        // 缓存查询
        String rmKey = REMOVE_USER_PREFIX + uid;
        keyStatus status = RedisUtil.keyAlive(REMOVE_USER_PREFIX + uid);
        if(status == keyStatus.EMPTY) throw new CustomException.AccessDeniedException("用户重复删除，数据层访问已拒绝");
        // 数据库查询
        boolean isRemoved = RedisUtil.taskLock((args) -> removeById((String) args[0]), REMOVE_USER_PREFIX + uid, uid);
        if(!isRemoved) throw new CustomException.DataNotFoundException(String.format("用户uid: %d不存在，删除失败", uid));
        // TODO: add key缓存同步删除
        // 查询缓存同步删除
        String key = QUERY_USER_PREFIX + uid;
        User query = (User) RedisUtil.query(key, new TypeReference<User>() {});
        if(query != null) {
            RedisUtil.removeKey(key);
            log.warn("缓存{}已同步删除", key);
        }
        // 添加rmId防止缓存穿透
        log.info("缓存穿透策略已更新 {}", rmKey);
        RedisUtil.set(rmKey, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
        return Result.success(String.format("user:uid:%d删除成功", uid), null);
    }

    // 用户信息更新
    @Override
    public Result userUpdate(User user) {
        log.info("**********用户信息更新请求**********");
        // 查询数据库
        // TODO: update()
//        RedisUtil.taskLock(args -> updateById((String) args[0]), UPDATE_USER_PREFIX + user.getUid(), user.getUid());
        // user.setUpdateTime(LocalDateTime.now());
        return null;
    }
}
