package com.vs.myemc_gms_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan       // 扫描同级包
@SpringBootApplication
public class MyemcGmsMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyemcGmsMainApplication.class, args);
    }
}
