package com.vs.blogsystem_v1.service;

//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.blogsystem_v1.mapper.PhotoMapper;
import com.vs.pojo.Photo;
import com.vs.pojo.Urls;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class FileServiceTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private PhotoMapper photoMapper;

    @Test
    public void test() {
//        // 增
//        Photo photo = new Photo(null, 3, "VS", "www.eee.com/images", LocalDateTime.now());
//        fileService.save(photo);
//
//        // 删
//        fileService.removeById(7);
//
//        // 改
//        Photo photo2 = new Photo(6, 1, "shiki", "www.baidu", LocalDateTime.now());
//        fileService.updateById(photo2);
//
//        // 查
//        List<Photo> photos = fileService.listByIds(List.of(2, 3));
//        photos.forEach(System.out::println);
    }

    @Test
    public void lambdaTest() {
        // 条件查询
//        String name = "shi";
//        Integer pid = 2;
//        Integer uid = 1;
        String url = "http://www.velvetshiki.cn/images/15.jpg";

        // 通过LambdaQueryWrapper复合查询
//        LambdaQueryWrapper<Photo> wrapper = new LambdaQueryWrapper<Photo>()
//                .select(Photo::getUsername, Photo::getUid)
//                .like(Photo::getUsername, name)
//                .or(cond -> cond.eq(Photo::getPid, pid));
//
//        List<Photo> list = photoMapper.selectList(wrapper);
//        list.forEach(System.out::println);

        // 通过chain
        LambdaQueryChainWrapper<Photo> wrapper = ChainWrappers.lambdaQueryChain(Photo.class);
        List<Photo> list = wrapper
//                .like(Photo::getUsername, name)
//                .eq(Photo::getPid, pid)
//                .eq(Photo::getUid, uid)
                .like(Photo::getPhotoUrl, url)
                .list();
        list.forEach(System.out::println);


        // 通过lambdaQuery查询
//        Wrappers.lambdaQuery()
//                .like(Photo::getUsername, name)
//                .eq(Photo::getPid, pid)
//                .eq(Photo::getUid, uid)
//                .like(Photo::getPhotoUrl, url)
//                .count();
//        lambdaUpdate().set(Photo::getUsername, "shiki").update();
    }

//    public Photo buildPhoto() {
//        return new Photo(null, 1, "shiki", "www.batch.com", LocalDateTime.now());
//    }

//    @Test
//    public void batchInsert() {
//        // 执行100次，每次批量插入1000条数据
//        // 对mysql jdbc进行批处理配置: rewriteBatchedStatements=true
//        Long begin = System.currentTimeMillis();
//        for(int i = 0; i < 100; i++) {
//            List<Photo> list = new ArrayList<>(1000);
//            for(int j = 0; j < 1000; j++) {
//                list.add(buildPhoto());
//            }
//            fileService.saveBatch(list);
//            list.clear();
//        }
//        Long end = System.currentTimeMillis();
//        System.out.println("执行耗时: " + (end - begin));
//        // output: 10万条数据，执行耗时: 11380ms
//    }

//    @Test
//    public void test2() {
//        for(int i = 0 ;i < 5; ++i) fileService.save(new Photo(null, 1, "shiki", "www.test2.com", LocalDateTime.now()));
////         逻辑删除
//    }

    @Test
    public void jsonTest() {
        // 更新照片墙-------success
//        fileService.uploadPhotos();

//        Db.lambdaUpdate(Photo.class).set(Photo::getPhotoUrl,
//                        Urls.of(List.of("www.vs/images/1.jpg",
//                                        "www.vs/images/2.png",
//                                        "www.vs/images/3.png",
//                                        "www.vs/images/4.png",
//                                        "www.vs/images/5.png")))
//                .eq(Photo::getUid, 1).update();

        // 更新用户名和UID-------success
//        Db.lambdaUpdate(Photo.class)
//                .set(Photo::getUsername, "velvet")
//                .set(Photo::getUid, 2)
//                .eq(Photo::getUid, 1)
//                .update();


        // 更新指定用户URL
        List<String> list = List.of("www.vs/images/14.jpg", "www.vs/images/25.jpg", "www.vs/images/33.jpg");
        String str = JSONObject.toJSONString(Urls.of(list));        // 转换为json串
//        String str = list.toString();
        Db.lambdaUpdate(Photo.class)
                .set(Photo::getPhotoUrl, str)
                .eq(Photo::getUid, 3)
                .update();


        // 更新全部photoURL-------success
//        Db.lambdaUpdate(Photo.class).update(Photo.of(null, 1, "shiki",
//                Urls.of(List.of("www.vs/images/1.jpg", "www.vs/images/2.png")), LocalDateTime.now()));
    }

    @Test
    public void jsonGet() {
//        Photo p = Db.lambdaQuery(Photo.class).eq(Photo::getUid, 3).one();
//        System.out.println(p.getPhotoUrl());
//        Object obj = JSON.toJSON(p.getPhotoUrl());
//        System.out.println(obj);
//        QueryWrapper<Photo> wrapper = new QueryWrapper<>();
//        wrapper.eq("uid", 1);
//        List<Photo> list = photoMapper.selectList(wrapper);
//        list.forEach(System.out::println);

        fileService.uploadPhotos("www.nice.com.test.cn", 1);
    }
}