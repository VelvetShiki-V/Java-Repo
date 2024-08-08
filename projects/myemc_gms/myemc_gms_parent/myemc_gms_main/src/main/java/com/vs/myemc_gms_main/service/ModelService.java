package com.vs.myemc_gms_main.service;

import com.vs.pojo.Model;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ModelService extends IService<Model>{
    List<Model> getModelList(String Id);

    void createModelNode(Model model);
}
