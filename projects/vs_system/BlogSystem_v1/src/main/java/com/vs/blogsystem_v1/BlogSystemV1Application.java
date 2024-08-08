package com.vs.blogsystem_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// 拦截类组件要在启动类加上ServletComponentScan注解以启用拦截功能
@ServletComponentScan
// 启动类
@SpringBootApplication
public class BlogSystemV1Application {
    public static void main(String[] args) {
        SpringApplication.run(BlogSystemV1Application.class, args);
    }
}
