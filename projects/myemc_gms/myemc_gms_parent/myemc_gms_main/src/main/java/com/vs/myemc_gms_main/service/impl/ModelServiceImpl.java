package com.vs.myemc_gms_main.service.impl;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.common.CustomException;
import com.vs.myemc_gms_main.mapper.ModelMapper;
import com.vs.myemc_gms_main.service.ModelService;
import com.vs.pojo.Model;
import com.vs.pojo.Result;
import com.vs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.vs.common.GlobalConstants.*;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    @Autowired
    public ModelServiceImpl(RedissonClient redissonClient) {
        RedisUtil.setRedissonClient(redissonClient);
    }

    // 数据查询
    @Override
    public Result getModelList(String Id) {
        log.info("**********数据查询请求**********");
        if(StrUtil.isNotBlank(Id)) {
            log.info("查询单条数据");
            Model data = RedisUtil.queryTTLWithDB(QUERY_MODEL_PREFIX + Id, new TypeReference<>(){},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    (args) -> getById((String) args[0]), Id);
            if(data != null) return Result.success("获取单条数据成功", List.of(data));
        } else {
            log.info("查询所有数据");
            List<Model> list = RedisUtil.queryTTLWithDB(QUERY_MODEL_ALL, new TypeReference<>(){},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES,
                    (args) -> list());
            if(list != null) return Result.success("获取所有数据成功", list);
        }
        return Result.error(404, "数据不存在");

        // 本地解析
//        return JsonUtil.fileParser(JsonUtil.staticFilePath, Model.class);
    }

    // 数据创建
    @Override
    public Result createModelNode(Model model) {
        log.info("**********节点数据创建请求**********");
        // 雪花算法生成node Id，设置并插入数据库
        long Id = RedisUtil.taskLock(args -> RedisUtil.uniKeyGen((String) args[0]),
                CREATE_MODEL_PREFIX + LocalDateTime.now(), KEY_ADD_COUNT);
        model.setId(String.valueOf(Id));
        save(model);
        return Result.success("节点创建成功", Id);
    }

    // 数据删除
    @Override
    public Result removeModelNode(String Id) {
        log.info("**********节点数据删除请求**********");
        // 查询rmId防止缓存穿透
        String rmKey = REMOVE_MODEL_PREFIX + Id;
        keyStatus status = RedisUtil.keyAlive(rmKey);
        if(status == keyStatus.EMPTY) throw new CustomException.AccessDeniedException("数据重复删除，数据层访问已拒绝");
        // redisson分布式可重入锁, 锁ID必须一致，才能确保多进程共享同一把锁
        boolean isRemoved = RedisUtil.taskLock(args -> removeById((String) args[0]), REMOVE_MODEL_PREFIX + Id, Id);
        if(!isRemoved) return Result.error(0, String.format("model:Id:%s删除失败，数据不存在", Id));
        // 查询缓存并同步删除
        String key = QUERY_MODEL_PREFIX + Id;
        Model query = (Model) RedisUtil.query(key, new TypeReference<Model>() {});
        if(query != null) {
            RedisUtil.removeKey(key);
            log.warn("缓存{}已同步删除", key);
        }
        // 添加rmId防止缓存穿透
        log.info("缓存穿透策略已更新 {}", rmKey);
        RedisUtil.set(rmKey, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
        return Result.success(String.format("model:Id:%s删除成功", Id), null);

        // 悲观锁
/*        synchronized (Id.intern()) {
            if(modelService.removeById(Id)) return Result.success("数据删除成功", null);
            else throw new CustomException.DataNotFoundException("数据不存在");
        }*/

        // 分布式锁
/*        long threadId = Thread.currentThread().getId();
        String key = RedisUtil.dbLockKeyGen(SYSTEM_REDIS_KEY_PREFIX + "rm:model:Id:" + Id);
        if(RedisUtil.distributedTryLock(key, REDIS_DBLOCK_EXPIRE, TimeUnit.SECONDS)) {
            // 获取锁成功，执行删除逻辑
            log.info("线程: {}, 锁获取成功", threadId);
            if (!removeById(Id)) {
                // 删除失败
                RedisUtil.distributedUnlock(key);
                log.info("dbLock已释放");
                throw new CustomException.DataNotFoundException("数据不存在");
            }
        } else {
            // 获取锁失败，等待...
            log.error("线程: {}, 锁获取失败", threadId);
            throw new RuntimeException(String.format("线程: %s, 锁获取失败", threadId));
//            removeModelNode(Id);
        }
        // 释放锁
        RedisUtil.distributedUnlock(key);
        log.info("dbLock已释放");*/
    }

    // 数据更新
    @Override
    public Result updateModelNode(Model model) {
        return null;
    }

}
