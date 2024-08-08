package com.vs.blogsystem_v1.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.blogsystem_v1.mapper.PhotoMapper;
import com.vs.blogsystem_v1.service.FileService;
import com.vs.pojo.Photo;
import com.vs.pojo.Urls;
import com.vs.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
// 继承封装好的service实现类，需要提供依赖注入的mapper类型和pojo类型
public class FileServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements FileService {
    @Autowired
    private PhotoMapper photoMapper;

    // 多表联查
    @Override
    public List<Photo> getUserPhotos(Integer uid) {
        // 先查用户id
        User user = Db.lambdaQuery(User.class).eq(User::getUid, uid).one();
        if(user == null) {
            log.error("用户查询失败");
            throw new Error("用户不存在");
        }
        else {
            log.info("用户存在: {}", user);
            // 再查所属photos
            List<Photo> list = Db.lambdaQuery(Photo.class).eq(Photo::getUid, uid).list();
            return list;
        }
    }

    @Override
    public void uploadPhotos(String url, Integer uid) {
        // 1. 从存储了VARCHAR字段取出json串数据: {"urls":["www.vs/images/xxx.jpg"]}
        Photo p = Db.lambdaQuery(Photo.class).eq(Photo::getUid, uid).one();
        // 2. 将json串转为json对象，并获取对象val值
        JSONObject obj = JSON.parseObject(p.getPhotoUrl());
        Object old_urls = null;
        for(String key: obj.keySet()) {
            // 获取到的值是一个数组
            old_urls = obj.get(key);
        }
        List<String> list = null;
        // 3. 将json数组转为java数组，用于修改改数组元素
        if(old_urls instanceof JSONArray) {
            list = ((JSONArray) old_urls).toJavaList(String.class);
        }
        // 4. 新增元素
        list.add(url);
        // 5. 将数组序列化到Urls对象中, 并持久化
        String new_urls = JSONObject.toJSONString(Urls.of(list));
        Db.lambdaUpdate(Photo.class)
                .set(Photo::getPhotoUrl, new_urls)
                .eq(Photo::getUid, uid)
                .update();
    }
}
