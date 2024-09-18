package com.vs.cloud_model.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_common.utils.RedisUtil;
import com.vs.cloud_api.client.CloudUserClient;
import com.vs.cloud_model.domain.Model;
import com.vs.cloud_model.exception.CustomException;
import com.vs.cloud_model.mapper.ModelMapper;
import com.vs.cloud_model.service.ModelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.vs.cloud_common.constants.GlobalConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    private final StringRedisTemplate template;
    private final RedissonClient redissonClient;
    private final CloudUserClient cloudUserClient;
    private final HttpServletRequest request;

    @Override
    public Result modelCreate(Model model) {
        log.info("**********数据创建请求**********");
        log.info("接收到数据信息: {}", model);
        if(StrUtil.isBlank(model.getName()) || StrUtil.isBlank(model.getOwner())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "数据名或创建者为空");
        }
        // mybatisX雪花算法生成mid
        LocalDateTime localDateTime = LocalDateTime.now();
        model.setCreateTime(localDateTime);
        model.setUpdateTime(localDateTime);
        try {
            save(model);
        } catch (Exception e) {
            throw new RuntimeException("数据创建失败");
        }
        return Result.success("数据创建成功", null);
    }

    @Override
    public Result modelQuery(String mid) {
        log.info("**********数据查询请求**********");
        // 登录用户验证
        try {
            // 转发请求头
            cloudUserClient.verifyUser(request.getHeader("Authorization"));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        // 认证成功，进行数据查询
        if(StrUtil.isBlank(mid)) {
            log.info("查询所有数据");
            List<Model> modelList = RedisUtil.queryTTLWithDB(template, QUERY_MODEL_ALL, new TypeReference<List<Model>>() {},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES, args -> list());
            if(modelList != null) return Result.success("获取到所有数据信息", modelList);
        } else {
            log.info("查询单个数据");
            Model model = RedisUtil.queryTTLWithDB(template, QUERY_MODEL_PREFIX + mid, new TypeReference<Model>() {},
                    REDIS_CACHE_MAX_TTL_MINUTES, TimeUnit.MINUTES, args -> getById(String.valueOf(args[0])), mid);
            if(model != null) return Result.success("获取到数据信息", model);
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "数据不存在");
    }

    @Override
    public Result modelUpdate(Model model) {
        log.info("**********数据更新请求**********");
        // 查询数据库
        Model updateModel = getById(model.getMid());
        if(updateModel == null) throw new CustomException(HttpStatus.NOT_FOUND, "更新数据不存在");
        log.info("数据存在，开始执行更新操作");
        boolean isUpdateSuccess = RedisUtil.taskLock(redissonClient, args -> Db.lambdaUpdate(Model.class)
                .set(Model::getName, model.getName())
                .set(Model::getOwner, model.getOwner())
                .set(Model::getStock, model.getStock())
                .set(Model::getLabels, model.getLabels())
                .set(Model::getChildren, model.getChildren())
                .set(Model::getUpdateTime, LocalDateTime.now())
                .set(Model::getProperties, model.getProperties())
                .eq(Model::getMid, model.getMid())
                .update(), UPDATE_MODEL_PREFIX + model.getMid(), model.getMid());
        if(!isUpdateSuccess) throw new RuntimeException("数据更新失败");
        else return Result.success("更新成功", null);
    }

    @Override
    public Result modelDelete(String mid) {
        log.info("**********数据删除请求**********");
        // 查询缓存
        Model deleteModel = getById(mid);
        if(deleteModel == null) throw new CustomException(HttpStatus.NOT_FOUND, "删除数据不存在");
        log.info("数据存在，开始执行删除操作");
        // 分布式锁控制删除
        boolean isRemoved = RedisUtil.taskLock(redissonClient, args -> removeById(String.valueOf(args[0])), REMOVE_MODEL_PREFIX + mid, mid);
        if(!isRemoved) throw new RuntimeException(String.format("数据mid: %s删除失败", mid));
        // 缓存同步删除
        String key = QUERY_MODEL_PREFIX + mid;
        Model query = RedisUtil.query(template, key, new TypeReference<Model>() {});
        if(query != null) {
            RedisUtil.removeKey(template, key);
            log.warn("缓存{}已同步删除", key);
        }
        return Result.success(String.format("数据mid:%s删除成功", mid), null);
    }
}
