package com.vs.cloud_model;

import com.vs.cloud_api.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

// 定义feignClient扫描包并配置日志输出
//@EnableFeignClients(basePackages = "com.vs.cloud_api.client", defaultConfiguration = FeignConfig.class)
@EnableFeignClients(basePackages = "com.vs.cloud_api.client")
@ComponentScan(basePackages = {"com.vs.cloud_common.config"})
@SpringBootApplication
public class CloudModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudModelApplication.class, args);
    }
}
