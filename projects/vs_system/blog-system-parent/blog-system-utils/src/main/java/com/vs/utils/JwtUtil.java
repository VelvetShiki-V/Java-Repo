package com.vs.utils;

import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;


@Slf4j
public class JwtUtil {
    // 设置JWT私钥和过期时间
    private static final String privateKey = "VelvetShiki";
    private static final Long expireDuration = 43200000L;

    // 生成JWT
    public static String jwtGen(Map<String, Object> payload) {
        String jwt = null;
        try {
            jwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, privateKey)
                    .setClaims(payload)
//                    .setExpiration(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expireDuration))
                    .compact();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        // TODO 将jwt存入redis并设计exp过期时间
        log.info("jwt gen result: \n{}", jwt);
        log.info("jwt will expire in 12h\n");
        return jwt;
    }

    // 解析JWT
    public static Claims jwtParse(String jwt) {
        Claims payload = null;
        try {
            payload = Jwts.parser()
                    .setSigningKey(privateKey)
                    .parseClaimsJws(jwt)
                    .getBody();
            // jwt与存储在服务器缓存中的jwt进行比对，当签名，负载，日期时限，格式等不合法时自动识别并返回null，否则解析出Claims返回
            // TODO 将用户请求携带的jwt与redis中值进行比对，相同则放行，不同则抛异常
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        log.info("jwt parse result: {}", payload);
        return payload;
    }
}
