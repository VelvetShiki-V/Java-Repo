package com.vs.cloud_common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonConfig {
    // 配置文件中读取(***需要构造器，getter, setter***)
    // 多实例场景下，如果多个 RedissonClient 实例使用相同的连接池配置，可能会导致连接池资源的竞争或其他并发问题。
    private String address;
    private String password;
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;

    // 成为容器管理bean对象，给外界提供依赖注入
    @Bean
    public RedissonClient redissonClient() {
        log.info("-------------redisson将进行自动装配-------------");
        log.info("读取address: {}", address);
        log.info("读取password: {}", password);
        log.info("读取connectionPoolSize: {}", connectionPoolSize);
        log.info("读取connectionMinimumIdleSize: {}", connectionMinimumIdleSize);
        log.info("-------------开始初始化redissonClient-------------");

        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        log.info("-------------redissonClient初始化完成-------------");
        return Redisson.create(config);
    }
}
