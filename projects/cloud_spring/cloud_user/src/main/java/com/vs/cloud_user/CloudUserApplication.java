package com.vs.cloud_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// 包扫描不仅要配置其他包全类名，还有启动类所在包本身的全类名
@ComponentScan(basePackages = {"com.vs.cloud_common", "com.vs.cloud_user"})
@SpringBootApplication
public class CloudUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUserApplication.class, args);
    }
}
