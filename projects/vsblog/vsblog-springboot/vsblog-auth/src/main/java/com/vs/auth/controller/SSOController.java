package com.vs.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.auth.model.vo.UserAuthVO;
import com.vs.auth.service.UserAuthService;
import com.vs.framework.model.dto.ResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "鉴权API")
@RequiredArgsConstructor
public class SSOController {

    private final UserAuthService userAuthService;

    @Operation(summary = "邮箱登录")
    @PostMapping("/login")
    public ResultDTO<UserInfoDTO> accountLogin(@RequestBody UserAuthVO auth) {
        return ResultDTO.ok(userAuthService.loginEmail(auth));
    }

    @Operation(summary = "检查登录状态")
    @GetMapping("/checkLogin")
    public SaResult checkLogin() {
        StpUtil.checkLogin();
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @Operation(summary = "是否为管理员")
    @GetMapping("/isAdmin")
    public SaResult checkIsAdmin(@RequestParam("loginId") Integer loginId) {
        return SaResult.data(userAuthService.checkIsAdmin(loginId));
    }

    @Operation(summary = "获取token")
    @GetMapping("/tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data("satoken=" + StpUtil.getTokenInfo());
    }

    @Operation(summary = "登出")
    @GetMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("登出成功");
    }
}
