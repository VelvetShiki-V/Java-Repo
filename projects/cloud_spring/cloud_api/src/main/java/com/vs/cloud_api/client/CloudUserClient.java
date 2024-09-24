package com.vs.cloud_api.client;

import com.vs.cloud_api.fallback.CloudUserClientFallbackFactory;
import com.vs.cloud_common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// openFeignClient请求微服务名称，以及服务降级回调工厂注册
@FeignClient(value = "cloud-user", fallbackFactory = CloudUserClientFallbackFactory.class)
public interface CloudUserClient {
    // 通过请求拦截器传递头部信息
    @GetMapping("/user/verify")
    Result verifyUser();
}
