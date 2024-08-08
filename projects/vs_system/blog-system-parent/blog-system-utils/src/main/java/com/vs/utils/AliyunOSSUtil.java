package com.vs.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

// aliyun OSS文件上传工具类
@Slf4j      // 用于log
@Component  // 用于IOC容器管理对象实例
@ConfigurationProperties(prefix = "aliyun.oss")     // 用于配置文件读取和前缀注入
public class AliyunOSSUtil {
    // 配置endpoint和访问凭证
    // 注意Configuration对应使用的@Value注解为import org.springframework.beans.factory.annotation.Value; 而非lombok.Value
    // 根据ConfigurationProperties自动注入前缀，并与key一一对应自动注入
//    @Value("${aliyun.oss.endpoint}")
    private static String endpoint;
//    @Value("${aliyun.oss.accessKeyId}")
    private static String accessKeyId;
//    @Value("${aliyun.oss.accessKeySecret}")
    private static String accessKeySecret;
    // 配置上传的bucket名称
//    @Value("${aliyun.oss.bucketName}")
    private static String bucketName;
    // 配置bucket上传目录
//    @Value("${aliyun.oss.bucketDir}")
    private static String bucketDir;

    // 暴露上传文件方法
    public static String upload(MultipartFile file) {
        // 创建认证对象给OSS实例使用
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        // 配置上传
        // 2. 上传到bucket中的目录及文件名
        // 2.1 设置新文件存储名：uuid避免文件名重复
        String filename = UUID.randomUUID().toString().replace("-", "");
        // 2.2 拼接文件名后缀
        filename += file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        log.info("获取到新文件名: {}", filename);
        String objectName = bucketDir + filename;

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        log.info("OSS 创建连接成功");

        try {
            // 创建文件的输入流，用于远程传输到远端OSS
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException | FileNotFoundException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭上传通道
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 将文件url返回
        return "https://" + bucketName + "." + endpoint + "/" + objectName;
    }
}
