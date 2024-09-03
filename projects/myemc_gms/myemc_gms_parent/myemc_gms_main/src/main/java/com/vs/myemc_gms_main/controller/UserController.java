package com.vs.myemc_gms_main.controller;
import com.vs.myemc_gms_main.service.UserService;
import com.vs.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vs.pojo.User;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-30
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/verify/login")
    public Result userLogin(@RequestBody User user) {
        return userService.loginService(user);
    }

    @PostMapping("/registry")
    public Result userCreate(@RequestBody User user) { return userService.userCreate(user); }

    @GetMapping
    public Result getUsers(Integer uid) { return userService.userQuery(uid); }

    @PutMapping
    public Result userUpdate(@RequestBody User user) { return userService.userUpdate(user); }

    @DeleteMapping
    public Result userDelete(@PathVariable Integer uid) { return userService.userDelete(uid); }
}
