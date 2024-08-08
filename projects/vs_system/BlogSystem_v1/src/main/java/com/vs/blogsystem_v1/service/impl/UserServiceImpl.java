package com.vs.blogsystem_v1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.pojo.PageBean;
import com.vs.pojo.UserCategory;
import com.vs.utils.TimeFormatUtil;
import lombok.extern.slf4j.Slf4j;
import com.vs.blogsystem_v1.service.UserService;
import com.vs.blogsystem_v1.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.vs.pojo.User;

// 用户数据业务处理服务类
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    // 查询用户列表
    @Override
    public PageBean getUsers(Integer uid, String username, Integer cid, Integer pageIndex, Integer pageSize) {
        // 调用mapper代理实例进行数据查询
        log.info("**********用户列表查询请求**********");
        Integer index = (pageIndex - 1) * pageSize;
        List<User> list;
        list = userMapper.getUsers(uid, username, cid, index, pageSize);
        Integer total = userMapper.getUsers(null, null, null, 0, 1000).size();
        // 返回PageBean对象
        return new PageBean(total, list);
    }

    // 用户删除
    @Override
    public void userDelete(List<Integer> uids) {
        log.info("**********用户注销请求**********");
        log.info("请求删除用户组: {}", uids);
        // FIXME sql: <==    Updates: 0
        userMapper.userDelete(uids);
    }

    // 用户注册
    @Override
    public void userRegister(User user) {
        log.info("**********用户注册请求**********");
        log.info("接收到用户数据: " + user);
        // 数据包装
        LocalDateTime now = TimeFormatUtil.timeFormat(LocalDateTime.now());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        // 根据cid创建分类
        if(user.getRole() != null) {
            String role = userMapper.getUserCategories(Integer.parseInt(user.getRole()), null).get(0).getRole();
            user.setRole(role);
        } else user.setRole("guest");
        log.info("包装后用户数据: " + user);
        userMapper.userRegister(user);
    }

    // 用户更新
    @Override
    public void userUpdate(User user) {
        log.info("**********用户信息更新请求**********");
        log.info("接收到待更新用户数据: " + user);
        user.setUpdateTime(TimeFormatUtil.timeFormat(LocalDateTime.now()));
        // 根据cid创建分类
        if(user.getRole() != null) {
            String role = userMapper.getUserCategories(Integer.parseInt(user.getRole()), null).get(0).getRole();
            user.setRole(role);
        } else user.setRole("guest");
        log.info("更新后用户数据: " + user);
        // FIXME sql: <==    Updates: 0
        userMapper.userUpdate(user);
    }

    // 用户登录
    @Override
    public User userLogin(User user) {
        log.info("**********用户登录请求**********");
        log.info("接收到用户登录数据: {}", user);
        return userMapper.userLogin(user);
    }

    // 角色查询
    @Override
    public List<UserCategory> getUserCategories(Integer cid, String role) {
        log.info("**********用户分类查询请求**********");
        return userMapper.getUserCategories(cid, role);
    }

    // 角色创建
    @Override
    public void userCategoryCreate(UserCategory category) {
        log.info("**********用户角色创建请求**********");
        log.info("接收到角色数据: {}", category.getRole());
        userMapper.userCategoryCreate(category.getRole());
    }

    // 角色修改
    @Override
    public void userCategoryUpdate(UserCategory category) {
        log.info("**********用户角色更新请求**********");
        log.info("接收到角色数据: {}", category);
        userMapper.userCategoryUpdate(category);
    }

    // 角色删除
    @Override
    public void userCategoryDelete(List<Integer> cids) {
        log.info("**********用户角色删除请求**********");
        log.info("请求删除角色组: {}", cids);
        userMapper.userCategoryDelete(cids);
    }
}
