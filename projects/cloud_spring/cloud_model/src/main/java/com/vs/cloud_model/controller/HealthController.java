package com.vs.cloud_model.controller;

import com.vs.cloud_common.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public Result checkHealth() { return Result.success("cloud_module服务正常", null); }
}
