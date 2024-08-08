package com.vs.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import java.net.UnknownHostException;

@Configuration
public class SerializeConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        // 创建redisTemplate实例
        RedisTemplate<String, Object> instance = new RedisTemplate<>();
        // 设置实例的连接工厂
        instance.setConnectionFactory(redisConnectionFactory);
        // 设置实例序列化工具
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        // 对实例处理的key val 序列化和反序列化
        instance.setKeySerializer(RedisSerializer.string());
        instance.setHashKeySerializer(RedisSerializer.string());
        instance.setValueSerializer(serializer);
        instance.setHashValueSerializer(serializer);
        // 将实例返回
        return instance;
    }
}
