package com.vs.cloud_gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    // 配置文件中定义的路径信息注入到网关路径信息中进行过滤
    private List<String> excludePaths;
}
