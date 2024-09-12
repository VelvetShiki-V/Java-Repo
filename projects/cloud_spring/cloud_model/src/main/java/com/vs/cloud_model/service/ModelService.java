package com.vs.cloud_model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.domain.Model;

public interface ModelService extends IService<Model> {
    Result modelCreate(Model model);

    Result modelQuery(String mid);

    Result modelUpdate(Model model);

    Result modelDelete(String mid);
}
