package com.vs.gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.util.SaResult;
import com.vs.gateway.client.AuthFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// 配合远程rpc查询根据用户id获取登录权限码
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private AuthFeignClient authFeignClient;

    // 权限码设置
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list= new ArrayList<>();
        // 异步调用auth服务（必须为异步链）
        CompletableFuture<SaResult> asyncRet = CompletableFuture.supplyAsync(() ->
                authFeignClient.checkIsAdmin(Integer.valueOf(loginId.toString())));
        try {
            // 拥有管理员权限
            if((boolean) asyncRet.get().getData()) {
                list.add("user.*");
                list.add("admin.*");
            } else {
                list.add("user.*");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // redis查询
        return list;
    }

    // 角色设置
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list= new ArrayList<>();
        // 异步调用auth服务（必须为异步链）
        CompletableFuture<SaResult> asyncRet = CompletableFuture.supplyAsync(() ->
                authFeignClient.checkIsAdmin(Integer.valueOf(loginId.toString())));
        try {
            // 拥有管理员权限
            if((boolean) asyncRet.get().getData()) {
                list.add("user");
                list.add("admin");
            } else {
                list.add("user");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // redis查询
        return list;
    }
}
