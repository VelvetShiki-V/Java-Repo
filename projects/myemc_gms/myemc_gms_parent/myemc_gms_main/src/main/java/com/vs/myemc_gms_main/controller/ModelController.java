package com.vs.myemc_gms_main.controller;

import com.vs.common.CustomException;
import com.vs.myemc_gms_main.service.ModelService;
import com.vs.pojo.Model;
import com.vs.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping
    public Result getModelList(String Id) {
        return Result.success("解析成功", modelService.getModelList(Id));
    }

    @PostMapping
    public Result createModel(@RequestBody Model model) {
        modelService.createModelNode(model);
        return Result.success("数据添加成功", null);
    }

    @DeleteMapping("/{Id}")
    public Result removeModel(@PathVariable String Id) {
        // 悲观锁
        synchronized (Id.intern()) {
            if(modelService.removeById(Id)) return Result.success("数据删除成功", null);
            else throw new CustomException.DataNotFoundException("数据不存在");
        }
    }
}
