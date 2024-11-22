package com.vs.auth.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.dev33.satoken.util.SaResult;
import com.vs.auth.entity.UserInfo;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.auth.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuthServerController {

    private final UserInfoService userInfoService;

    // OAuth2-Server 端：处理所有 OAuth2 相关请求
    @RequestMapping("/*")
    public Object request() {
        System.out.println("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return SaOAuth2ServerProcessor.instance.dister();
    }

    // 获取 userinfo 信息：昵称、头像、性别等等
    @RequestMapping("/userinfo")
    public SaResult userinfo() {
        return SaResult.ok().setData(userInfoService.userinfo());
    }

    // 流程：前端先在认证中心登录，获取code码，再用code码获取token和userinfo
    // code码是一次性的，当使用过一次或重新执行授权操作就会作废

    // 先登录: /oauth2/authorize (未登录会被跳转到/oauth2/doLogin界面)
    // http://velvetshiki.com:8003/oauth2/authorize?response_type=code
    // &client_id=666666&redirect_uri=https://www.baidu.com&scope=openid,userinfo
    // 必须传入response_type, client_id, redirect_uri, 而scope则为是否静默或显示授权
    // 登录完成后如果scope有值，会被跳转到/oauth2/doConfirm界面，用户需要手动点击授权才能跳转到redirect_uri获取code码
    // 前端在redirect_uri写location.href=res.redirect_uri就能跳转回原页面

    // 再获取token: /oauth2/token
    // http://velvetshiki.com:8003/oauth2/token
    // ?code=P7FlVGnNuKA5yJy2aMp3IdtQj0HHUSIzZUyoRccyYpcSQV3r7LTmtfo2gmEA
    // &client_id=666666&client_secret=velvetshiki
    // &grant_type=authorization_code&redirect_uri=https://www.baidu.com
    // 必须传入所有参数

    // 置换token（每次权限操作都调用来刷新token）: /oauth2/refresh
    // http://velvetshiki.com:8003/oauth2/refresh?grant_type=refresh_token
    // &client_id=666666&client_secret=velvetshiki
    // &refresh_token=W2okvRDlCe6tvvAd8ya23RbtDdK77V6LuBjGiS7TSqcpOq5mY4bHkShb9DnU
    // 填入授权参数，前端请求id，秘钥和获取到的refresh-token即可置换新的token

    // 获取账号信息: /oauth2/userinfo
    // 需要显示授权的userinfo权限才允许获取用户信息，且服务端需要自定义信息提供逻辑
    // http://velvetshiki.com:8003/oauth2/userinfo?
    // access_token=PmjqpOAEH3OhDx27rXKtv04Y1x9cC8Zon9mA5BGRA11PP1CEenpMxy4H2K8P
}
