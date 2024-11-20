package com.vs.ssoServer;

import cn.dev33.satoken.sso.SaSsoManager;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SsoServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
        System.out.println();
        System.out.println("---------------------- Sa-Token SSO 统一认证中心启动成功 ----------------------");
        System.out.println("配置信息：" + SaSsoManager.getServerConfig());
        System.out.println();

    }

}
