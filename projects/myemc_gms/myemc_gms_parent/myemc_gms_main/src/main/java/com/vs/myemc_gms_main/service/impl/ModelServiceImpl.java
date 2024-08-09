package com.vs.myemc_gms_main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.common.CustomException;
import com.vs.myemc_gms_main.mapper.ModelMapper;
import com.vs.myemc_gms_main.service.ModelService;
import com.vs.pojo.Model;
import com.vs.pojo.Result;
import com.vs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    // TODO: 参数配置化
    @SuppressWarnings({"unused"})
    private final long REDIS_DBLOCK_EXPIRE = 30L;
    private final String SYSTEM_REDIS_KEY_PREFIX = "myemc_gms:";

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<Model> getModelList(String Id) {
        log.info("**********数据查询请求**********");
        // 本地解析
//        return JsonUtil.fileParser(JsonUtil.staticFilePath, Model.class);

        // redis - mysql查询
        if(!StringUtils.hasLength(Id)) return Db.lambdaQuery(Model.class).list();
        else {
            List<Model> list = Db.lambdaQuery(Model.class).eq(Model::getId, Id).list();
            if(!list.isEmpty()) return list;
            else throw new CustomException.DataNotFoundException("数据不存在, Id: " + Id);
        }
    }

    @Override
    public void createModelNode(Model model) {
        log.info("**********节点数据创建请求**********");
        // 雪花算法生成node Id
        String keyPrefix = SYSTEM_REDIS_KEY_PREFIX + "add:model:day:";
        long Id = RedisUtil.uniKeyGen(keyPrefix);
        // 添加唯一ID
        model.setId(String.valueOf(Id));
        // 插入数据库
        save(model);
        log.info("节点创建成功, Id: {}, 已计入add:model统计量", Id);
    }

    @Override
    public Result removeModelNode(String Id) {
        log.info("**********节点数据删除请求**********");
        // redisson分布式可重入锁
        // 锁ID必须一致，才能确保多进程共享同一把锁
        RLock lock = redissonClient.getLock(SYSTEM_REDIS_KEY_PREFIX + "rm:model:Id:" + Id);
        boolean isLocked = lock.tryLock();
        if(!isLocked) return Result.error(0, "数据删除失败，锁获取失败");
        else {
            // 锁获取成功，删除数据
            if (!removeById(Id)) {
                // 删除失败
                lock.unlock();
                log.info("RLock已释放");
                throw new CustomException.DataNotFoundException("数据不存在");
            }
        }
        lock.unlock();
        log.info("RLock已释放");
        return Result.success("数据删除成功", null);


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
