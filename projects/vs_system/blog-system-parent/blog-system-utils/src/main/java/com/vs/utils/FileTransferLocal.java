package com.vs.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileTransferLocal {
    public static String save(MultipartFile file, String storePath, String accessPath) throws IOException {
        String filename = UUID.randomUUID().toString().replace("-", "");
        // 拼接文件名后缀
        filename += file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        log.info("获取到文件名: {}", filename);
        // output sample: fba067fa27714e43b37bc8ca92dee38a.jpg
        // 二进制文件转储路径
        file.transferTo(new File(storePath + filename));
        String url = accessPath + filename;
//      String url = "http://www.velvetshiki.cn/images/" + filename;
        log.info("转储路径: {}", storePath + filename);
        log.info("文件上传成功: {}", url);
        return url;
    }
}
