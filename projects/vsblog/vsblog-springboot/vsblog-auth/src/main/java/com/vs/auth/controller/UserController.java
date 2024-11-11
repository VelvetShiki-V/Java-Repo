package com.vs.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.framework.model.dto.ResultDTO;
import com.vs.auth.model.vo.UserAuthVO;
import com.vs.auth.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户鉴权API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthService userAuthService;

    @Operation(summary = "邮箱登录")
    @PostMapping("/email/login")
    public ResultDTO<UserInfoDTO> accountLogin(@RequestBody UserAuthVO auth) {
        return ResultDTO.ok(userAuthService.loginEmail(auth));
    }

    @Operation(summary = "检查登录状态")
    @GetMapping("/checkLogin")
    public ResultDTO<?> checkLogin() {
        StpUtil.checkLogin();
        return ResultDTO.ok(StpUtil.getTokenValue());
    }
}
