package com.vs.oss.config;

import cn.dev33.satoken.sso.config.SaSsoServerConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSOConfig {
    // 为统一认证中心地址，当用户在其它 Client 端发起登录请求时，
    // 均将其重定向至认证中心，待到登录成功之后再原路返回到 Client 端
//    127.0.0.1 velvetshiki.com     父域名
//    127.0.0.1 vsblog.com          子域名
//    127.0.0.1 vs.com
//    127.0.0.1 shiki.com

    @Autowired
    private void configSso(SaSsoServerConfig ssoServer) {
        // 未登录时返回的View
        ssoServer.notLoginView = () -> {
            String msg = "当前会话在SSO-Server端尚未登录，请先访问"
                    + "<a href='/sso/doLogin?name=shiki&pwd=123456' target='_blank'> doLogin登录 </a>"
                    + "进行登录之后，刷新页面开始授权";
            return msg;
        };

        // 登录处理函数
        ssoServer.doLoginHandle = (name, pwd) -> {
            // 此处仅做模拟登录，真实环境应该查询数据进行登录
            if ("shiki".equals(name) && "123456".equals(pwd)) {
                StpUtil.login(10001);
                return SaResult.ok("登录成功！").setData(StpUtil.getTokenValue());
            }
            return SaResult.error("登录失败！");
        };

    }
}
