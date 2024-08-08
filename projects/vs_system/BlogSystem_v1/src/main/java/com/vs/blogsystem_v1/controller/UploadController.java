package com.vs.blogsystem_v1.controller;

import com.vs.utils.FileTransferLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.vs.pojo.*;
import com.vs.blogsystem_v1.service.FileService;
//import com.vs.utils.AliyunOSSUtil;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@ConfigurationProperties(prefix = "files.avatar")     // 用于配置文件读取和前缀注入
public class UploadController {
    @Autowired
    private FileService fileService;

    @Value("${files.avatar.storePath}")
    private String storePath;
    @Value("${files.avatar.accessPath}")
    private String accessPath;

    @PostMapping("upload")
    public Result fileUpload(@RequestParam("file") MultipartFile file) throws IOException {      // 加上RequestParam为必传参数
        if (file == null || file.isEmpty()) return Result.error("请选择文件上传");
        else {
            log.info("file uploads: {}", file);

            // 方式一：OSS上传（参数：新文件名 + 原上传文件名路径）
//            String url = AliyunOSSUtil.upload(file);

            // 方式二：本地上传
            String url = FileTransferLocal.save(file, storePath, accessPath);
            return new Result(1, "上传成功", url);
        }
    }

    @GetMapping("/photo")
    public Result getPhotos(@RequestParam Integer uid) {
        List<Photo> list = fileService.getUserPhotos(uid);
        return Result.success(list);
    }

    @PostMapping("/photo")
    public Result uploadPhotos(@RequestParam("file") MultipartFile file, @RequestParam Integer uid) throws IOException {
        if (file == null || file.isEmpty()) return Result.error("请选择图片上传");
        else {
            // 1. 上传图片至服务器存储
            String url = FileTransferLocal.save(file, storePath, accessPath);
            // 2. 将图片url存储至用户数据库photo
            fileService.uploadPhotos(url, uid);
            return new Result(1, "上传成功", url);
        }
    }
}