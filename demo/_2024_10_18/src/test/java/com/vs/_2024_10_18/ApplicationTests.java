package com.vs._2024_10_18;

import com.vs._2024_10_18.security.jwt.RsaUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringBootTest
class ApplicationTests {

    ApplicationTests() throws FileNotFoundException {
    }

    // 绝对路径
//    String pubPath = "D:\\Code\\Github\\Java-Repo\\demo\\_2024_10_18\\src\\main\\resources\\rsa.pub";
//    String priPath = "D:\\Code\\Github\\Java-Repo\\demo\\_2024_10_18\\src\\main\\resources\\rsa.pri";

    // 相对路径
    // classpath:前缀表示从src/main/resources/目录下加载资源。
    String pubPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/rsa.pub").getPath();
    String priPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/rsa.pri").getPath();

    @Test
    void genPubnPri() throws NoSuchAlgorithmException {
        RsaUtil.generateKey(pubPath, priPath, "shiki", 2048);
    }

    @Test
    void loadKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("get pub key: " + RsaUtil.loadPublicKey(pubPath));
        System.out.println("get pri key: " + RsaUtil.loadPrivateKey(priPath));
    }
}
