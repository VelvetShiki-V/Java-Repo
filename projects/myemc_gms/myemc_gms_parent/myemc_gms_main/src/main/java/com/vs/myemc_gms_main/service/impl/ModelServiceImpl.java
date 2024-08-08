package com.vs.myemc_gms_main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.common.CustomException;
import com.vs.myemc_gms_main.mapper.ModelMapper;
import com.vs.myemc_gms_main.service.ModelService;
import com.vs.pojo.Model;
import com.vs.utils.JsonUtil;
import com.vs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
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
        // 雪花算法生成node Id
        long Id = RedisUtil.uniKeyGen("gms:add:model:Id");
        // 添加唯一ID
        model.setId(String.valueOf(Id));
        // 插入数据库
        save(model);
    }
}
