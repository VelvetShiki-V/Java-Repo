package com.vs.auth.config;

import cn.dev33.satoken.sso.config.SaSsoServerConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.vs.auth.model.vo.UserAuthVO;
import com.vs.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SsoServerConfig {

    private final UserAuthService userAuthService;

    @Autowired
    private void configSso(SaSsoServerConfig ssoServer) {
        // 未登录时返回的View
        ssoServer.notLoginView = () -> {
            String msg = "当前会话在SSO-Server端尚未登录，请先访问"
                    + "<a href='/sso/doLogin?name=shiki@qq.com&pwd=123456' target='_blank'> doLogin登录 </a>"
                    + "进行登录之后，刷新页面开始授权";
            return msg;
        };

        // 登录处理函数
        ssoServer.doLoginHandle = (name, pwd) -> {
            UserAuthVO user = new UserAuthVO(name, pwd);
            userAuthService.loginEmail(user);
            return SaResult.ok("登录成功！").setData(StpUtil.getTokenValue());
        };

    }
}
