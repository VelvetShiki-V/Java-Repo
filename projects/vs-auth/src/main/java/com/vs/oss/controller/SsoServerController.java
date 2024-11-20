package com.vs.oss.controller;

import cn.dev33.satoken.sso.processor.SaSsoServerProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sso")
public class SsoServerController {

    /**
     * SSO-Server端：处理所有SSO相关请求 (下面的章节我们会详细列出开放的接口)
     */
    @RequestMapping("/*")
    public Object ssoRequest() {
        return SaSsoServerProcessor.instance.dister();
    }
}
