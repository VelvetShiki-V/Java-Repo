package com.vs._2024_10_18.security.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.*;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

// 使用hutool工具包包装的工具类
@Slf4j
public class JwtUtil {
    // 设置秘钥和签名
    // private static final String privateKey = "velvetshiki123456";        // 秘钥明文（不推荐）
    // 秘钥生成keytool -genkeypair -alias demo_keypair -keyalg RSA -keypass zcx123 -keystore demo.keystore -storepass zcx123

    // 密钥对
    private static final PrivateKey privateKey;
    private static final PublicKey publicKey;

    static {
        try {
            // 根据秘钥文件PEM初始化密钥对
            // FIXME: path to be fixed
            privateKey = loadPrivateKey("./path");
            publicKey = loadPublicKey("./path");
            log.info("获取到公钥: {}", publicKey);
            log.info("获取到私钥: {}", privateKey);
        } catch (Exception e) {
            throw new RuntimeException("公钥秘钥解析失败" + e);
        }
    }

    // 对称加密算法签名
    // private static final JWTSigner signer = JWTSignerUtil.hs256(privateKey.getBytes());
    // 非对称加密算法签名
    private static final JWTSigner signer = JWTSignerUtil.rs256(privateKey);


    public static String generateJWT(Map<String, Object> payload) {
        // 默认headers填充"alg": "HS256"和"typ": "JWT"
//        String token = JWT.create().setIssuedAt(DateUtil.date()).setSigner(signer).sign();
        return JWTUtil.createToken(payload, signer);
    }

    public static <T> T parseNverifyJWT(String jwtToken, Class<T> type) {
        try {
            JWT jwt = JWTUtil.parseToken(jwtToken);
            log.debug("解析出jwt 算法: {}, 签名: {}, payload: {}", jwt.getAlgorithm(), jwt.getSigner(), jwt.getPayload());
            log.debug("解析出headers: {}", jwt.getHeaders());
            // 验证合法性
            JWTValidator.of(jwt).validateAlgorithm(signer);     // 签名合法性
            JWTValidator.of(jwt).validateDate(DateUtil.date()); // 日期合法性
            JWTUtil.verify(jwtToken, signer);
            log.debug("token验证合法");
            // 解析payload并封装为指定bean
            JWTPayload payload = jwt.getPayload();
            Map<String, Object> payloadMap = jwt.getPayload().getClaimsJson();
            T ret = BeanUtil.toBean(payloadMap, type, CopyOptions.create().setIgnoreNullValue(true));
            log.debug("转换完成ret: {}", ret);
            return ret;
        } catch (JWTException e) {
            throw new IllegalArgumentException("无效token: ", e);
        }
    }

    // 加载密钥对
    public static PrivateKey loadPrivateKey(String keyPath) {
        // 读取privateKey.pem文件加载私钥
        String privateKeyPem = FileUtil.readUtf8String(keyPath);
        // base64解码后转换为私钥对象
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyUtil.generatePrivateKey("rs256", spec);
    }

    public static PublicKey loadPublicKey(String keyPath) {
        // 读取privateKey.pem文件加载公钥
        String publicKeyPem = FileUtil.readUtf8String(keyPath);
        // base64解码后转换为公钥对象
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        // 通过key工厂生成公钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePublic(spec);
        return KeyUtil.generatePublicKey("rs256", spec);
    }
}