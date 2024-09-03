package com.vs.utils;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.vs.common.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.vs.common.GlobalConstants.*;

@Slf4j
public class JwtUtil {
    // @Scheduled(fixedRate = SECRET_KEY_EXPIRE_SECONDS)
    private static SecretKey secretKeyRefresh() {
        // 算法随机生成, 并将生成的二进制秘钥序列化
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        RedisUtil.set(SECRET_KEY_PREFIX, encodedKey, SECRET_KEY_EXPIRE_MILLISECONDS, TimeUnit.MILLISECONDS);
        log.info("秘钥已过期，开始生成新秘钥: {}", encodedKey);
        return key;
    }

    private static Key keyFetchRemote() {
        // 远程获取SECRET_KEY, 并将加密的秘钥反序列化
        String encodedKey = (String) RedisUtil.query(SECRET_KEY_PREFIX, new TypeReference<String>() {});
        if(StrUtil.isBlank(encodedKey)) return secretKeyRefresh();
        else return new SecretKeySpec(Base64.getDecoder().decode(encodedKey), SignatureAlgorithm.HS256.getJcaName());
    }

    // 生成JWT
    public static String jwtGen(Map<String, Object> payload) throws RuntimeException {
        log.info("jwt生成，获取payload: {}", payload);
        String jwt = Jwts.builder()
                .setClaims(payload)
                .signWith(keyFetchRemote())
                .compact();
        // 存入redis
        String tokenKey = JWT_PREFIX + payload.get("name");
        log.info("获取到tokenKey: {}", tokenKey);
        // RedisUtil.set(tokenKey, jwt, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
        return jwt;
    }

    // 解析JWT(双重验证token)
    public static void jwtParseRefresh(String jwt) {
        Claims payload;
        try {
            payload = Jwts.parserBuilder()
                        .setSigningKey(keyFetchRemote())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
            MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new CustomException.InvalidTokenException("token解析失败, 请重新登录: " + e.getMessage());
        }
        // 与redis缓存token比较
        String username = payload.get("name").toString();
        String token = (String) RedisUtil.query(JWT_PREFIX + username, new TypeReference<String>() {});
        if(!jwt.equals(token)) throw new CustomException.InvalidTokenException("token缓存过期，请重新登陆");
        // 刷新token有效期
        RedisUtil.set(JWT_PREFIX + username, jwt, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
    }
}
