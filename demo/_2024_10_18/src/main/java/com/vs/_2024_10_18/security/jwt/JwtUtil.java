package com.vs._2024_10_18.security.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


// 使用hutool工具包包装的jwt工具类（RSA加密）
@Slf4j
public class JwtUtil {
    // 设置秘钥和签名
    // private static final String privateKey = "velvetshiki123456";        // 秘钥明文（不推荐）
    // 秘钥生成keytool -genkeypair -alias demo_keypair -keyalg RSA -keypass zcx123 -keystore demo.keystore -storepass zcx123

    // 使用私钥签名，传入用户数据payload，设置过期时间，生成jwt返回
    public static String generateJWT(Object userPayload, PrivateKey privateKey, int expireMinutes) {
        // 对象转payload map
        Map<String, Object> loginUserMap = new HashMap<>();
        BeanUtil.beanToMap(userPayload, loginUserMap, CopyOptions.create().setIgnoreNullValue(true));
        String jwt = Jwts.builder()
                .setClaims(loginUserMap)
                .setId(new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes())))
                .setExpiration(DateUtil.offsetMinute(DateUtil.date(), expireMinutes))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        log.info("token gen: {}", jwt);
        return jwt;
    }

    // 使用公钥验证传入的jwt签名有效性，再将payload转为特定bean返回
    public static <T> T verifyJWT(String jwt, PublicKey publicKey, Class<T> targetType) {
        // 验证签名
        Claims claims;
        try {
             claims = Jwts.parserBuilder()
                     .setSigningKey(publicKey)
                     .build()
                     .parseClaimsJws(jwt)
                     .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("token解析失败" + e.getMessage());
        }
        T ret = BeanUtil.copyProperties(claims, targetType);
        log.info("转换成功: {}", ret);
        return ret;
    }
}