package com.vs.article.strategy.impl;

import com.vs.article.strategy.FileUploadStrategy;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadStrategyImpl implements FileUploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String filepath) {
        return null;
    }

}
