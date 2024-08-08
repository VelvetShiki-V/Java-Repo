package com.vs.blogsystem_v1.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.pojo.Photo;

import java.util.List;

public interface FileService extends IService<Photo> {
    List<Photo> getUserPhotos(Integer uid);

    void uploadPhotos(String url, Integer uid);
}
