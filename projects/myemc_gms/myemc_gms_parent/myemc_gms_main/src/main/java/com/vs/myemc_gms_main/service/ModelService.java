package com.vs.myemc_gms_main.service;
import com.vs.pojo.Model;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.pojo.Result;

public interface ModelService extends IService<Model>{
    Result getModelList(String Id);

    Result createModelNode(Model model);

    Result removeModelNode(String Id);

    Result updateModelNode(Model model);
}
