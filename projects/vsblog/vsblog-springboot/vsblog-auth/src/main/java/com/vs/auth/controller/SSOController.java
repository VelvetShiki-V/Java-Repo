package com.vs.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.sso.model.SaCheckTicketResult;
import cn.dev33.satoken.sso.processor.SaSsoClientProcessor;
import cn.dev33.satoken.sso.template.SaSsoUtil;
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
@RequestMapping("/sso")
@Tag(name = "鉴权API")
@RequiredArgsConstructor
public class SSOController {

    private final UserAuthService userAuthService;

    @Operation(summary = "邮箱登录")
    @PostMapping("/email/login")
    public ResultDTO<UserInfoDTO> accountLogin(@RequestBody UserAuthVO auth) {
        return ResultDTO.ok(userAuthService.loginEmail(auth));
    }

    @Operation(summary = "检查登录状态")
    @GetMapping("/checkLogin")
    public SaResult checkLogin() {
        StpUtil.checkLogin();
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @Operation(summary = "获取token")
    @GetMapping("/tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @Operation(summary = "登出")
    @GetMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @SaCheckLogin
    @Operation(summary = "权限访问")
    @GetMapping("/access")
    public SaResult access() {
        return SaResult.ok();
    }

    // 当前是否登录
    @GetMapping("/sso/isLogin")
    public Object isLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

    // 返回SSO认证中心登录地址
    @GetMapping("/sso/getSsoAuthUrl")
    public SaResult getSsoAuthUrl(@RequestParam("url") String clientLoginUrl) {
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(clientLoginUrl, "");
        return SaResult.data(serverAuthUrl);
    }

    // 根据ticket进行登录
    @GetMapping("/sso/doLoginByTicket")
    public SaResult doLoginByTicket(@RequestParam("ticket") String ticket) {
        SaCheckTicketResult ctr = SaSsoClientProcessor.instance.checkTicket(ticket, "/sso/doLoginByTicket");
        StpUtil.login(ctr.loginId, ctr.remainSessionTimeout);
        return SaResult.data(StpUtil.getTokenValue());
    }
}
