package com.vs.article.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileUploadStrategy {

    // 指定路径随机名称存储
    String uploadFile(MultipartFile file, String filepath);

    // 根据文件名存储
    String uploadFile(InputStream inputStream, String filename, String filepath);

}
