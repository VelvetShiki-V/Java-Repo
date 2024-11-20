package com.vs.ssoClient;

import cn.dev33.satoken.sso.SaSsoManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SsoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoClientApplication.class, args);
        System.out.println();
        System.out.println("---------------------- Sa-Token SSO 模式二 Client 端启动成功 ----------------------");
        System.out.println("配置信息：" + SaSsoManager.getClientConfig());
        System.out.println("测试前需要根据官网文档修改hosts文件，测试账号密码：sa / 123456");
    }


}
