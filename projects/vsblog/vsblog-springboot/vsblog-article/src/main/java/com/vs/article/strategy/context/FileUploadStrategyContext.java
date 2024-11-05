package com.vs.article.strategy.context;

import com.vs.article.enums.UploadStrategyEnum;
import com.vs.article.strategy.FileUploadStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    @Value("${profile.upload.mode}")
    private String mode;

    private final Map<String, FileUploadStrategy> fileUploadStrategyMap;

    // 根据对象存储模式选择上传策略，进行文件上传
    // 随机名称存储
    public String executeUploadStrategy(MultipartFile file, String path) {
        return fileUploadStrategyMap.get(UploadStrategyEnum.strategyAdapator(mode)).uploadFile(file, path);
    }

    // 指定名称存储
    public String executeUploadStrategy(InputStream inputStream, String filePath, String filename) {
        return fileUploadStrategyMap.get(UploadStrategyEnum.strategyAdapator(mode)).uploadFile(inputStream, filePath, filename);
    }
}
