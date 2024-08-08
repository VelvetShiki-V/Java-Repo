package com.vs.blogsystem_v1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.pojo.User;
import com.vs.pojo.UserCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 查询用户(可根据uid, username进行分页查询)
    List<User> getUsers(Integer uid, String username, Integer cid, Integer pageIndex, Integer pageSize);

    // 删除用户
    void userDelete(List<Integer> uids);

    // 用户注册
    @Insert("insert into user (username, password, tel, role, addr, profile, create_time, update_time) values " +
            "(#{username}, #{password}, #{tel}, #{role}, #{addr}, #{profile}, #{createTime}, #{updateTime})")
    void userRegister(User user);

    // 用户更新
    void userUpdate(User user);

    // 用户登录
    @Select("select * from user where username = #{username} and password = #{password}")
    User userLogin(User user);

    // 分类查询
    List<UserCategory> getUserCategories(Integer cid, String role);

    // 角色创建
    @Insert("insert into user_category (role) values (#{role})")
    void userCategoryCreate(String role);

    // 角色修改
    void userCategoryUpdate(UserCategory category);

    // 角色删除
    void userCategoryDelete(List<Integer> cids);
}
