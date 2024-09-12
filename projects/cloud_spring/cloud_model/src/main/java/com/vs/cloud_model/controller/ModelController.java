package com.vs.cloud_model.controller;

import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.domain.Model;
import com.vs.cloud_model.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    public Result createModel(@RequestBody Model model) { return modelService.modelCreate(model); }

    @GetMapping
    public Result queryModel(@RequestParam("mid") String mid) { return modelService.modelQuery(mid); }

    @PutMapping
    public Result updateModel(@RequestBody Model model) { return modelService.modelUpdate(model); }

    @DeleteMapping("/{mid}")
    public Result deleteModel(@PathVariable("mid") String mid) { return modelService.modelDelete(mid); }
}
