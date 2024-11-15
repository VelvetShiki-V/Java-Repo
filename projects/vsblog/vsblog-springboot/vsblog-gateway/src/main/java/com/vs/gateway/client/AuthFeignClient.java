package com.vs.gateway.client;

import cn.dev33.satoken.util.SaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "vsblog-auth")
public interface AuthFeignClient {

    // 同步rpc调用路径需要写全
    @GetMapping("/auth/isAdmin")
    SaResult checkIsAdmin(@RequestParam("loginId") Integer loginId);
}
