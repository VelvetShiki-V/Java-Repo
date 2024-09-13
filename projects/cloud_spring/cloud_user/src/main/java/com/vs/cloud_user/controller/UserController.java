package com.vs.cloud_user.controller;

import com.vs.cloud_user.domain.User;
import com.vs.cloud_user.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.vs.cloud_common.domain.Result;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result userLogin(@RequestBody User user) {
        return userService.loginService(user);
    }

    @PostMapping("/registry")
    public Result userCreate(@RequestBody User user) { return userService.userCreate(user); }

    @GetMapping("/verify")
    public Result userVerify() { return userService.loginVerify(); }

    @GetMapping
    public Result getUsers(@RequestParam("uid") String uid) { return userService.userQuery(uid); }

    @PutMapping
    public Result userUpdate(@RequestBody User user) { return userService.userUpdate(user); }

    @DeleteMapping("/{uid}")
    public Result userDelete(@PathVariable("uid") String uid) { return userService.userDelete(uid); }
}
