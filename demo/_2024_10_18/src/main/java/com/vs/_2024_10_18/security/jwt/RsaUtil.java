package com.vs._2024_10_18.security.jwt;

import cn.hutool.core.io.FileUtil;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtil {
    private  static final int DEFAULT_KEY_SIZE = 2048;

    // 生成秘钥对文件
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String sectryKey, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(sectryKey.getBytes());
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写入文件
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
        FileUtil.writeBytes(publicKeyBytes, publicKeyFilename);
        // 获取私钥并写入文件
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
        FileUtil.writeBytes(privateKeyBytes, privateKeyFilename);
    }

    // 加载Base64文件密钥对
    public static PrivateKey loadPrivateKey(String keyPath) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 读取privateKey.pem文件加载私钥
        byte[] bytes = FileUtil.readBytes(keyPath);
        // base64解码后转换为私钥对象
        byte[] keyBytes = Base64.getDecoder().decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(String keyPath) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 读取privateKey.pem文件加载公钥
        byte[] bytes = FileUtil.readBytes(keyPath);
        // base64解码后转换为公钥对象
        bytes = Base64.getDecoder().decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        // 通过key工厂生成公钥对象
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    // 加载pem格式文件密钥对
    public static PublicKey loadPublicKeyFromPem(String keyPathFromPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 读取.pem文件
        String key = new String(FileUtil.readBytes(keyPathFromPem));
        // 去掉pem文件的头部和尾部
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""); // 去掉所有空白字符
        // base64解码后转换为公钥对象
        byte[] bytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        // 通过key工厂生成公钥对象
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    // 加载pem格式文件密钥对
    public static PrivateKey loadPrivateKeyFromPem(String keyPathFromPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 读取.pem文件
        String key = new String(FileUtil.readBytes(keyPathFromPem));
        // 去掉pem文件的头部和尾部
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        // base64解码后转换为私钥对象
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
