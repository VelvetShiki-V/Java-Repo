package com.vs.auth.controller;

import cn.dev33.satoken.sso.SaSsoManager;
import cn.dev33.satoken.sso.model.SaCheckTicketResult;
import cn.dev33.satoken.sso.processor.SaSsoClientProcessor;
import cn.dev33.satoken.sso.processor.SaSsoServerProcessor;
import cn.dev33.satoken.sso.template.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sso")
public class SsoClientController {
    @GetMapping("/")
    public String index() {
        String goBackUrl = SaSsoManager.getClientConfig().splicingSloUrl();
        return "<h2>Sa-Token SSO-Client 应用端</h2>" +
                "<p>当前会话是否登录：" + StpUtil.isLogin() + "</p>" +
                "<p><a href=\"javascript:location.href='/sso/login?back=' + encodeURIComponent(location.href);\">登录</a> " +
                "<a href=\"javascript:location.href='" + goBackUrl + "?back=' + encodeURIComponent(location.href);\">注销</a> </p>";
    }

    // 返回SSO认证中心登录地址
    @GetMapping("/getSsoAuthUrl")
    public SaResult getSsoAuthUrl(@RequestParam("clientLoginUrl") String clientLoginUrl) {
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(clientLoginUrl, "");
        return SaResult.data(serverAuthUrl);
    }

    // SSO
    /*
     * SSO-Client端：处理所有SSO相关请求
     * http://{host}:{port}/sso/login          -- Client端登录地址，接受参数：back=登录后的跳转地址
     * http://{host}:{port}/sso/logout         -- Client端单点注销地址（isSlo=true时打开），接受参数：back=注销后的跳转地址
     * http://{host}:{port}/sso/logoutCall     -- Client端单点注销回调地址（isSlo=true时打开），此接口为框架回调，开发者无需关心
     */
    @GetMapping("/login")
    public Object ssoLogin() {
        return SaSsoClientProcessor.instance.ssoLogin();
    }

    @GetMapping("/logout")
    public Object ssoLogout() {
        return SaSsoClientProcessor.instance.ssoLogout();
    }

    @GetMapping("/logoutCall")
    public Object ssoLogoutCall() {
        return SaSsoClientProcessor.instance.ssoLogoutCall();
    }

    // 根据ticket进行登录（使用一次即失效）
    @GetMapping("/doLoginByTicket")
    public SaResult doLoginByTicket(@RequestParam("ticket") String ticket) {
        SaCheckTicketResult ctr = SaSsoClientProcessor.instance.checkTicket(ticket, "/sso/doLoginByTicket");
        StpUtil.login(ctr.loginId, ctr.remainSessionTimeout);
        return SaResult.data(StpUtil.getTokenValue());
    }
}
