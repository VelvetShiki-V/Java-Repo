package com.vs.cloud_api.client;

import com.vs.cloud_common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("cloud-user")
public interface CloudUserClient {
    // 通过请求参数携带token传递
/*    @GetMapping("/user/verify")
    Result verifyUser(@RequestHeader("Authorization") String authorization);*/

    // 通过请求拦截器传递
    @GetMapping("/user/verify")
    Result verifyUser();
}
