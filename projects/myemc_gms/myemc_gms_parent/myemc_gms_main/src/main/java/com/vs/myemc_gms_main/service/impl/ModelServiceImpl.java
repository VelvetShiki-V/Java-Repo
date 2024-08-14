package com.vs.myemc_gms_main.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.myemc_gms_main.mapper.ModelMapper;
import com.vs.myemc_gms_main.service.ModelService;
import com.vs.pojo.Model;
import com.vs.pojo.Result;
import com.vs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static com.vs.common.GlobalConstants.*;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    @Override
    public Result getModelList(String Id) {
        log.info("**********数据查询请求**********");
        return Result.success();
        // 本地解析
//        return JsonUtil.fileParser(JsonUtil.staticFilePath, Model.class);

        // redis - mysql查询
//        if(!StringUtils.hasLength(Id)) return Result.success("所有数据获取成功", list());
//        else {
//            // 指定数据Id查询
//            Model data = RedisUtil.queryTTLWithDB(QUERY_MODEL_PREFIX + Id, Id, Model.class, this::getById, REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES);
//            if(data != null) return Result.success("数据获取成功", List.of(data));
//            else throw new CustomException.DataNotFoundException("数据不存在, Id: " + Id);
//        }
    }

    @Override
    public Result createModelNode(Model model) {
        log.info("**********节点数据创建请求**********");
        // 雪花算法生成node Id，设置并插入数据库
        long Id = RedisUtil.taskLock(args -> RedisUtil.uniKeyGen((String) args[0]), CREATE_MODEL_PREFIX + LocalDateTime.now(), KEY_ADD_COUNT);
        model.setId(String.valueOf(Id));
        save(model);
        return Result.success("节点创建成功", Id);
    }

    @Override
    public Result removeModelNode(String Id) {
        log.info("**********节点数据删除请求**********");
        // redisson分布式可重入锁, 锁ID必须一致，才能确保多进程共享同一把锁
        boolean isRemoved = RedisUtil.taskLock(args -> removeById((String) args[0]), REMOVE_MODEL_PREFIX + Id, Id);
        if(!isRemoved) return Result.error(0, "数据删除失败，锁获取失败");
        return Result.success(String.format("数据%s删除成功", Id), null);

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
}
