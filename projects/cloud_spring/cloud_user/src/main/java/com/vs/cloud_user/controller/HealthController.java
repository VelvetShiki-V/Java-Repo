package com.vs.cloud_user.controller;

import com.vs.cloud_user.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// -Djdk.lang.Process.launchMechanism=vfork
@RestController
public class HealthController {
    @GetMapping("/health")
    public Result checkHealth() {
        return Result.success("服务正常", null);
    }
}
