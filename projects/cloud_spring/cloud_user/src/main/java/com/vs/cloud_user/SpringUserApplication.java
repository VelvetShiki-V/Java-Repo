package com.vs.cloud_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.vs.cloud_user", "com.vs.cloud_common"})
public class SpringUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringUserApplication.class, args);
    }

}
