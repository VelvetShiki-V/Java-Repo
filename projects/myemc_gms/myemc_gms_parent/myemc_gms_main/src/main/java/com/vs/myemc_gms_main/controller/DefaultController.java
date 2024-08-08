package com.vs.myemc_gms_main.controller;

import com.vs.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping("/health")
    public Result checkHealth() {
        return Result.success("服务正常", null);
    }
}