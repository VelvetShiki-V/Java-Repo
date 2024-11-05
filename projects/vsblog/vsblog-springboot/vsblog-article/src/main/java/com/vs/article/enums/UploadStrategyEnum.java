package com.vs.article.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadStrategyEnum {

    MINIO("minio", "minioUploadStrategyImpl"),

    OSS("oss", "ossUploadStrategyImpl");

    private final String mode;

    private final String strategy;

    // 根据配置文件传入的mode自动选择策略实现类执行对应算法
    public static String strategyAdapator(String mode) {
        for(UploadStrategyEnum value: UploadStrategyEnum.values()) {
            if(value.getMode().equals(mode)) return value.getStrategy();
        }
        return null;
    }
}
