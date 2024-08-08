package com.vs.springdemo1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 请求处理类
@RestController
public class HelloController {
    // 设置路由
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("Hello Spring");
        return "Hello Spring";
    }
}
