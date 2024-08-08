package com.vs.utils;
import cn.hutool.extra.tokenizer.TokenizerException;
import com.vs.common.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtUtil {
    // 设置私钥和过期时间
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // TODO: 硬编码配置化
    private static final Long EXPIRE_DURATION = 1800000L;    // 30min exp
    private static final String KEY_PREFIX = "gms:login:uid:";

    // 生成JWT
    public static String jwtGen(Map<String, Object> payload) throws RuntimeException {
        log.info("jwt生成，获取payload: {}", payload);
        String jwt = Jwts.builder()
                .setSubject("user")
                .setIssuer("myemc_gms")
                .setIssuedAt(new Date())
                .setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SECRET_KEY)
                .compact();
        // 存入redis
        String tokenKey = KEY_PREFIX + payload.get("uid");
        RedisUtil.setValue(tokenKey, jwt, EXPIRE_DURATION, TimeUnit.MILLISECONDS);
        return jwt;
    }

    // 解析JWT(双重验证token)
    public static void jwtParseRefresh(String jwt) {
        Claims payload = null;
        try {
            payload = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
            MalformedJwtException | SignatureException |
            IllegalArgumentException e) {
            throw new CustomException.InvalidTokenException("token签名不一致");
        }
        // 与redis缓存token比较
        String token = RedisUtil.getValue(KEY_PREFIX + payload.get("uid"));
        if(!jwt.equals(token)) throw new CustomException.InvalidTokenException("token缓存过期");
        // 刷新token有效期
        RedisUtil.setValue(KEY_PREFIX + payload.get("uid"), jwt, EXPIRE_DURATION, TimeUnit.MILLISECONDS);
    }
}
