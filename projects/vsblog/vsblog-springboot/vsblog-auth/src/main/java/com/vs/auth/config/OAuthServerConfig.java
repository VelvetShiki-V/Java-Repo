package com.vs.auth.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.consts.GrantType;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.dev33.satoken.oauth2.strategy.SaOAuth2Strategy;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import com.vs.auth.model.vo.UserAuthVO;
import com.vs.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OAuthServerConfig {


    private final UserAuthService userAuthService;


    @Autowired
    public void configurer(SaOAuth2ServerConfig oAuth2Server) {
        // 添加client前端信息（允许在配置文件中设置）
        oAuth2Server.addClient(
                new SaClientModel()
                        .setClientId("666666")        // 信任的第三方应用请求id
                        .setClientSecret("velvetshiki")     // client 秘钥
                        .addAllowRedirectUris("http://localhost:8003/oauth2/")      // 允许授权url
                        .addContractScopes("openid", "userid", "userinfo")      // 允许签约权限
                        .addAllowGrantTypes(
                                // 允许授权模式
                                GrantType.authorization_code,
                                GrantType.implicit,  // 隐式式
                                GrantType.refresh_token,  // 刷新令牌
                                GrantType.password,  // 密码式
                                GrantType.client_credentials  // 客户端模式
                        )
        );

        // 未登录view(生产模式替换为index.html)
        oAuth2Server.notLoginView = () -> "当前会话在OAuth-Server端尚未登录，请先访问"
                + "<a href='/oauth2/doLogin?name=shiki@qq.com&pwd=123456' target='_blank'> doLogin登录 </a>"
                + "进行登录之后，刷新页面开始授权";

        // access-token与satoken互通（一个token访问所有接口，但损失了不同client不同权限的意义）
        // 刷新token会失效
        SaOAuth2Strategy.instance.createAccessToken = (clientId, LoginId, scopes) -> StpUtil.getOrCreateLoginSession(LoginId);

        // 登录处理函数
        oAuth2Server.doLoginHandle = (name, pwd) -> {
            UserAuthVO user = new UserAuthVO(name, pwd);
            userAuthService.loginEmail(user);
            return SaResult.ok("登录成功！");
        };

        // 授权时view(生产模式替换为index.html)
        oAuth2Server.confirmView = (clientId, scopes) -> {
            String scopeStr = SaFoxUtil.convertListToString(scopes);
            String yesCode = "fetch('/oauth2/doConfirm?client_id=" + clientId
                    + "&scope=" + scopeStr + "', {method: 'POST'})" +
                    ".then(res => res.json()).then(res => location.reload())";
            String res = "<p>应用 " + clientId + " 请求授权：" + scopeStr + "，是否同意？</p>"
                    + "<p>" +
                    "        <button onclick=\"" + yesCode + "\">同意</button>" +
                    "        <button onclick='history.back()'>拒绝</button>" +
                    "</p>";
            return res;
        };
    }
}
