package com.vs._2024_10_24.service;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    public void connect() {
        System.out.println(minioClient);
    }
}
