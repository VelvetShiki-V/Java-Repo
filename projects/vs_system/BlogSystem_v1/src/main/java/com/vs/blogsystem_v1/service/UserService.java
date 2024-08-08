package com.vs.blogsystem_v1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.pojo.PageBean;
import com.vs.pojo.User;
import com.vs.pojo.UserCategory;

import java.util.List;

public interface UserService extends IService<User> {
    // 查询用户列表
    PageBean getUsers(Integer uid, String username, Integer cid, Integer pageIndex, Integer pageSize);

    // 用户删除
    void userDelete(List<Integer> uids);

    // 用户注册
    void userRegister(User user);

    // 用户更新
    void userUpdate(User user);

    // 用户登录
    User userLogin(User user);

    // 分类查询
    List<UserCategory> getUserCategories(Integer cid, String role);

    // 分类创建
    void userCategoryCreate(UserCategory category);

    // 分类更新
    void userCategoryUpdate(UserCategory category);

    // 分类删除
    void userCategoryDelete(List<Integer> cids);
}
