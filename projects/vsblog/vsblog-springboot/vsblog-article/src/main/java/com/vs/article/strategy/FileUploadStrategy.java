package com.vs.article.strategy;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadStrategy {

    String uploadFile(MultipartFile file, String filepath);

}
