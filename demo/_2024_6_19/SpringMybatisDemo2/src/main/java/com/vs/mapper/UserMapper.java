package com.vs.mapper;

import com.vs.pojo.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

/*@Mapper
public interface UserMapper {
    @Select("select * from tll_server_db.user")
    public List<User> getUserList();

    @Select("select * from tll_server_db.user where username = #{username}")
    public User getUser(String username);

    @Delete("delete from tll_server_db.user where uid = #{uid}")
    public void deleteUser(Integer uid);

    // 插入同时获取主键uid
    @Options(keyProperty = "uid", useGeneratedKeys = true)
    @Insert("insert into tll_server_db.user (username, password, tel, prime) values " +
            "(#{username}, #{password}, #{tel}, #{prime})")
    public void insertUser(User user);

    // 更新同时获取主键uid
    @Update("update tll_server_db.user " +
            "set username = #{username}, password = #{password}, " +
            "tel = #{tel}, prime = #{prime} where uid = #{uid}")
    public void updateUser(User user);
}*/

// xml
@Mapper
public interface UserMapper {
//    // 查询全部数据
//    public List<User> getUserList();
//
//    // 查询单条数据
//    public User getUser(String username, String prime);

    // 动态select
    public List<User> getUsers(Integer uid, String username, String tel, String prime);

    // 删除单条数据
    public void deleteUsers(Integer[] uids);

    // 插入条目同时获取主键uid
    public void insertUser(User user);

    // 更新数据
    public void updateUsers(User user);
}