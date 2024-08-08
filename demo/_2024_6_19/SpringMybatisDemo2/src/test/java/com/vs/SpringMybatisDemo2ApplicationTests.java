package com.vs;

import com.vs.mapper.UserMapper;
import com.vs.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
class SpringMybatisDemo2ApplicationTests {

    @Autowired
    private UserMapper userMapper;

//    @Test
//    public void testSelect() {
//        List<User> userList = userMapper.getUserList();
//        for(User user: userList) System.out.println(user);
//    }

//    @Test
//    public void testDelete() {
//        // 可使用数组，也可使用集合
//        Integer[] arr = {10000, 10001, 10058};
//        userMapper.deleteUsers(arr);
//    }

//    @Test
//    public void testInsert() {
//        User user = new User("bavlov", "123123", "2222", "1");
//        userMapper.insertUser(user);
//        System.out.println(user.getUid());
//    }

//    @Test
//    public void testUpdate() {
//        // 先获取用户数据
//        User user = userMapper.getUsers(null, "ZCX", null, null).get(0);
//        System.out.println("获取到待更新数据" + user);
//        // 更新数据
//        user.setUsername("ZCXX");
//        user.setPassword("12334541");
//        user.setTel("153999333");
//        user.setPrime("0");
//        userMapper.updateUser(user);
//        System.out.println("获取到主键uid: " + user.getUid());
//    }

    // 动态select查询
    @Test
    public void testSelect() {
        userMapper.getUsers(null, null, "153", null).stream().forEach(user -> System.out.println(user));
    }

}
