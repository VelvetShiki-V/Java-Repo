package com.vs.article.strategy.impl;

import io.minio.MinioClient;
import com.vs.article.config.properties.MinioConfigProperties;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service("minioUploadStrategyImpl")
@RequiredArgsConstructor
public class MinioUploadStrategyImpl extends AbstractUploadStrategyImpl{

    private final MinioConfigProperties minioProperties;

    private final MinioClient minioClient;


    // 查询对象是否存在
    @Override
    public Boolean isExist(String filePath) {
        Boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    // 上传文件
    @SneakyThrows
    @Override
    public void upload(InputStream inputStream, String filePath, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(filePath + fileName)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    // 获取文件url
    @Override
    public String getFileUrl(String filePath) {
        return minioProperties.getUrl() + filePath;
    }
}
