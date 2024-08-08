package com.vs.blogsystem_v1.controller;

import com.vs.blogsystem_v1.aop.Log;
import com.vs.blogsystem_v1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.vs.pojo.*;
import com.vs.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 用户controller
@Lazy       // 延迟到第一次使用时再初始化
@Scope("prototype")     // 对bean对象设置为非单例
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    // 依赖注入
    @Autowired
    private UserService userService;

    // 读取用户(查询参数为: uid, username, pageIndex, pageSize)
    // 获取用户数据列表, 限定请求方式为GET, 等价于@RequestMapping(value = "/users", method = RequestMethod.GET)
    @GetMapping
    public Result getUsers(Integer uid, String username, Integer cid,
                           @RequestParam(defaultValue = "1") Integer pageIndex,
                           @RequestParam(defaultValue = "1000") Integer pageSize) {
        // 移交业务至service层
        PageBean pageUsers = userService.getUsers(uid, username, cid, pageIndex, pageSize);
        if(pageUsers == null || pageUsers.getTotal() == 0) return Result.error("查无此人");
        else return Result.success(pageUsers);
    }

    // 用户登录
    @PostMapping("/login")
    public Result userLogin(@RequestBody User user) {
        User userRet = userService.userLogin(user);
        if(userRet != null) {
            // 登陆成功后，根据用户信息生成JWT令牌并返回
            log.info("用户登录成功: {}", userRet);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("uid", userRet.getUid());
            userMap.put("username", userRet.getUsername());
//            userMap.put("password", userRet.getPassword());
            String jwt = JwtUtil.jwtGen(userMap);
            // 返回jwt
            return Result.success(jwt);
        }
        else {
            log.error("用户登录失败: " + user);
            return Result.error("登录失败, 用户名或密码错误");
        }
    }

    // 用户注册
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @PostMapping("/registry")
    public Result userRegister(@RequestBody User user) {
        userService.userRegister(user);
        return Result.success();
    }

    // 用户更新
    //    @TimeRecord  自定义注解，用于AOP切面管理
    @Log
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @PutMapping
    public Result userUpdate(@RequestBody User user) {
        userService.userUpdate(user);
        return Result.success();
    }

    // 删除用户
    @Log
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DeleteMapping("/{uids}")
    public Result userDelete(@PathVariable List<Integer> uids) {
        userService.userDelete(uids);
        return Result.success();
    }

    // 用户分类查询
    @GetMapping("/category")
    public Result getUserCategories(Integer cid, String role) {
        List<UserCategory> list = userService.getUserCategories(cid, role);
        if(list.isEmpty()) {
            log.info("没有用户分组");
            return Result.error("没有用户分组");
        } else return Result.success(list);
    }

    // 分类新增
    @Log
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @PostMapping("/category")
    public Result userCategoryCreate(@RequestBody UserCategory category) {
        userService.userCategoryCreate(category);
        return Result.success();
    }

    // 分类修改
    @Log
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @PutMapping("/category")
    public Result userCategoryUpdate(@RequestBody UserCategory category) {
        userService.userCategoryUpdate(category);
        return Result.success();
    }

    // 分类删除
    @Log
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DeleteMapping("/category/{cids}")
    public Result userCategoryDelete(@PathVariable List<Integer> cids) {
        userService.userCategoryDelete(cids);
        return Result.success();
    }
}
