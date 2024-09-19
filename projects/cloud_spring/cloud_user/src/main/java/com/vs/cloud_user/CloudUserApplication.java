package com.vs.cloud_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.vs.cloud_common.config"})
@SpringBootApplication
public class CloudUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUserApplication.class, args);
    }
}
