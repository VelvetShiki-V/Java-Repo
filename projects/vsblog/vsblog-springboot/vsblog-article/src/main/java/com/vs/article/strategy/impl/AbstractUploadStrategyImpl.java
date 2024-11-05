package com.vs.article.strategy.impl;

import com.vs.article.exception.CustomException;
import com.vs.article.strategy.FileUploadStrategy;
import com.vs.framework.utils.FileUtil;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractUploadStrategyImpl implements FileUploadStrategy {

    // 根据文件内容生成存储
    @Override
    public String uploadFile(MultipartFile file, String filepath) {
        try {
            // 获取文件名称
            String md5Prefix = FileUtil.getMd5(file.getInputStream());
            String extSuffix = FileUtil.getExtName(file.getOriginalFilename());
            String fileName = md5Prefix + extSuffix;
            if(!isExist(filepath + fileName)) {
                // 文件不存在，上传
                upload(file.getInputStream(), filepath, fileName);
            }
            // 文件存在，直接返回url
            return getFileUrl(filepath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 指定文件名称存储
    @Override
    public String uploadFile(InputStream inputStream, String filepath, String fileName) {
        try {
            if(!isExist(filepath + fileName)) {
                // 文件不存在，上传
                upload(inputStream, filepath, fileName);
            }
            // 文件存在，直接返回url
            return getFileUrl(filepath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 重写方法
    public abstract Boolean isExist(String filePath);

    public abstract void upload(InputStream inputStream, String filePath, String fileName) throws IOException;

    public abstract String getFileUrl(String filePath);
}
