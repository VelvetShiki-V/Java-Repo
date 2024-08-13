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
import static com.vs.common.GlobalConstants.JWT_EXPIRE_DURATION_SECONDS;
import static com.vs.common.GlobalConstants.JWT_PREFIX;

@Slf4j
public class JwtUtil {
    // 设置私钥和过期时间
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // TODO: 硬编码配置化


    // 生成JWT
    public static String jwtGen(Map<String, Object> payload) throws RuntimeException {
        log.info("jwt生成，获取payload: {}", payload);
        String jwt = Jwts.builder()
                .setSubject("user")
                .setIssuer("myemc_gms")
                .setIssuedAt(new Date())
                .setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRE_DURATION_SECONDS))
                .signWith(SECRET_KEY)
                .compact();
        // 存入redis
        String tokenKey = JWT_PREFIX + payload.get("uid");
        RedisUtil.setWithTTL(tokenKey, jwt, JWT_EXPIRE_DURATION_SECONDS, TimeUnit.MILLISECONDS);
        return jwt;
    }

    // 解析JWT(双重验证token)
    public static void jwtParseRefresh(String jwt) {
        Claims payload;
        try {
            payload = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
            MalformedJwtException | SignatureException |
            IllegalArgumentException e) {
            throw new CustomException.InvalidTokenException("token签名不一致");
        }
        // 与redis缓存token比较
//        Integer uid = (Integer) payload.get("uid");
        // TODO: 缓存穿透
//        String token = RedisUtil.queryWithTTL(JWT_PREFIX + uid, uid, User,);
//        if(!jwt.equals(token)) throw new CustomException.InvalidTokenException("token缓存过期");
        // 刷新token有效期
        RedisUtil.setWithTTL(JWT_PREFIX + payload.get("uid"), jwt, JWT_EXPIRE_DURATION_SECONDS, TimeUnit.MILLISECONDS);
    }
}
