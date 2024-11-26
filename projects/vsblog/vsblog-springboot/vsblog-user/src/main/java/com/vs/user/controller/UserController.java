package com.vs.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.vs.framework.model.dto.ResultDTO;
import com.vs.user.model.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    // 用戶角色CRUD
    @GetMapping
    public ResultDTO<UserInfoDTO> getLoginUser() {
        return ResultDTO.ok();
    }

    @PutMapping
    public ResultDTO<?> updateUserInfo() {
        return ResultDTO.ok();
    }

    @PostMapping
    public ResultDTO<?> addUser() {
        return ResultDTO.ok();
    }

    @DeleteMapping
    public ResultDTO<?> deleteUser() {
        return ResultDTO.ok();
    }
}
