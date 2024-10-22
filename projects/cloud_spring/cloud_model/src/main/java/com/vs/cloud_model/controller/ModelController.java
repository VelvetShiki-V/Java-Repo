package com.vs.cloud_model.controller;

import cn.hutool.core.bean.BeanUtil;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.domain.Model;
import com.vs.cloud_model.domain.ModelDoc;
import com.vs.cloud_model.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
@Tag(name = "model API", description = "model数据相关接口")
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    @Operation(summary = "数据创建", description = "需要传入mode参数")
    @Parameter(name = "model", description = "model数据", required = true)
    public Result createModel(@RequestBody Model model) { return modelService.modelCreate(model); }

    @GetMapping
    @Operation(summary = "数据查询", description = "传入mid以查询对应数据")
    @Parameter(name = "mid", description = "传入string mid", required = true)
    public Result queryModel(@RequestParam("mid") String mid) { return modelService.modelQuery(mid); }

    @PutMapping
    public Result updateModel(@RequestBody Model model) { return modelService.modelUpdate(model); }

    @DeleteMapping("/{mid}")
    public Result deleteModel(@PathVariable("mid") String mid) { return modelService.modelDelete(mid); }

    @GetMapping("/es")
    public void searchModel() {
        // 对象转json
//        ModelDoc modelDoc = BeanUtil.copyProperties(modelService.getById("123"), ModelDoc.class);
    }
}
