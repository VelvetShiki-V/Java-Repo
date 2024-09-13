package com.vs.cloud_model.client;

import com.vs.cloud_common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("cloud-user")
public interface CloudUserClient {
    @GetMapping("/user/verify")
    Result verifyUser(@RequestHeader("Authorization") String authorization);
}
