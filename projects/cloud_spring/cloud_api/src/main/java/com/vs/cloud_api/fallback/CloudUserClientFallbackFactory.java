package com.vs.cloud_api.fallback;

import com.vs.cloud_api.client.CloudUserClient;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

// 回调工厂，当feign微服务调用失败时触发的服务降级回调
@Slf4j
public class CloudUserClientFallbackFactory implements FallbackFactory<CloudUserClient> {
    @Override
    public CloudUserClient create(Throwable cause) {
        return new CloudUserClient() {
            @Override
            public Result verifyUser() {
                log.error("CloudUserClient fallback触发，服务降级生效, cause: {}", cause);
                return null;
            }
        };
    }
}
