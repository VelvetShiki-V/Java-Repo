package com.vs.springdemo1.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.vs.springdemo1.pojo.*;

import java.time.LocalDateTime;

// 创建请求类
@RestController
public class RequestController {

    @RequestMapping("/login")
    @ResponseBody
    public Result loginRequest(HttpServletRequest request) {
        // 解析查询参数
        String name = request.getParameter("name");
        Integer password = Integer.parseInt(request.getParameter("pwd"));
        System.out.println(name + password);
        return new Result(200, "login success", "some data");
    }

    // springboot自动解析
    @RequestMapping("/splogin")
    // 创建请求类
    public Result loginRequest(String name, Integer pwd) {
        // 自动解析并存入形参中
        System.out.println(name + pwd);
        return new Result(200, "splogin success", null);
    }

    // 使用对象接收实体参数并解析
    @RequestMapping("/userRequest")
    public Result userRequest(User user) {
        // 自动解析并存入形参中
        System.out.println(user);
        return Result.success("request success", user);
    }

    // 日期解析
    @RequestMapping("/dateParam")
    public Result dateRequest(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time) {
        System.out.println(time);
        return Result.success("time request success", time);
    }

    // json解析
    @RequestMapping("/register")
    public Result registerRequest(@RequestBody User user) {
        System.out.println("获取到了对象: " + user);
        return Result.success("register success", user);
    }

    // 路径参数解析
    @RequestMapping("/path/{id}")
    // @PathVariable注解用于解析动态路径参数
    public Result idRequest(@PathVariable Integer id) {
        System.out.println("获取到了/path/" + id);
        return new Result(200, "路径获取成功/path/" + id, null);
    }
}
