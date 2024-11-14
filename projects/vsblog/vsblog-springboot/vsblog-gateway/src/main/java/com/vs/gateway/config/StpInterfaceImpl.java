package com.vs.gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StpInterfaceImpl implements StpInterface {

    // 权限码设置
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        list.add("user.get");
        list.add("articles.get");
        list.add("admin.*");
        return list;
    }

    // 角色设置
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list= new ArrayList<>();
        list.add("admin");
        list.add("user");
        return list;
    }
}
