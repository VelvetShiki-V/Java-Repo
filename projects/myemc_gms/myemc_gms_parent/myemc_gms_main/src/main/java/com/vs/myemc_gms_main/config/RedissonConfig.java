package com.vs.myemc_gms_main.config;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
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
    // @Bean
    // public RedissonClient redissonClient() {
    //     Config config = new Config();
    //     config.useSingleServer()
    //             .setAddress(address)
    //             .setPassword(password)
    //             .setConnectionPoolSize(connectionPoolSize)
    //             .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
    //     return Redisson.create(config);
    // }
}

