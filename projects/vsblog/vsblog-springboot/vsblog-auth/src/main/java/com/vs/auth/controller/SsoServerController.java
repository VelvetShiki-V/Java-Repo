package com.vs.auth.controller;

import cn.dev33.satoken.sso.processor.SaSsoClientProcessor;
import cn.dev33.satoken.sso.processor.SaSsoServerProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sso")
public class SsoServerController {
    /**
     * SSO-Server端：处理所有SSO相关请求
     */
    @GetMapping("/auth")
    public Object ssoAuth() {
        return SaSsoServerProcessor.instance.ssoAuth();
    }

    @GetMapping("/doLogin")
    public Object ssoServerLogin() {
        return SaSsoServerProcessor.instance.ssoDoLogin();
    }

    // SSO-Server：校验ticket 获取账号id
    @GetMapping("/sso/checkTicket")
    public Object ssoCheckTicket() {
        return SaSsoServerProcessor.instance.ssoCheckTicket();
    }

    @GetMapping("/signout")
    public Object ssoLogout() {
        return SaSsoClientProcessor.instance.ssoLogout();
    }
}
