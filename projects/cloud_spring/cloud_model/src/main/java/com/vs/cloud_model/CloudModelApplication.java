package com.vs.cloud_model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CloudModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudModelApplication.class, args);
    }
}
