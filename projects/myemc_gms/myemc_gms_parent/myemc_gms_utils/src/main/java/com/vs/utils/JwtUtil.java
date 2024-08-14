package com.vs.utils;
import com.vs.common.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.vs.common.GlobalConstants.JWT_EXPIRE_DURATION_MINUTES;
import static com.vs.common.GlobalConstants.JWT_PREFIX;

@Slf4j
public class JwtUtil {
    // 设置私钥和过期时间
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 生成JWT
    public static String jwtGen(Map<String, Object> payload) throws RuntimeException {
        log.info("jwt生成，获取payload: {}", payload);
        String jwt = Jwts.builder()
                .setSubject("user")
                .setIssuer("myemc_gms")
                .setIssuedAt(new Date())
                .setClaims(payload)
                .signWith(SECRET_KEY)
                .compact();
        // 存入redis
        String tokenKey = JWT_PREFIX + payload.get("uid");
        RedisUtil.setWithTTL(tokenKey, jwt, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
        return jwt;
    }

    // 解析JWT(双重验证token)
    public static void jwtParseRefresh(String jwt) {
        Claims payload;
        try {
            payload = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
            MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new CustomException.InvalidTokenException("token解析失败" + e.getMessage());
        }
        // 与redis缓存token比较
        Integer uid = (Integer) payload.get("uid");
        String token = RedisUtil.query(JWT_PREFIX + uid);
        log.info("接收到jwt: {}", jwt);
        log.info("jwt解析uid: {}", uid);
        log.info("对比redis token: {}", token);
        if(!jwt.equals(token)) throw new CustomException.InvalidTokenException("token缓存过期，请重新登陆");
        // 刷新token有效期
        RedisUtil.setWithTTL(JWT_PREFIX + uid, jwt, JWT_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
    }
}
