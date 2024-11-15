package com.vs.gateway.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignMessageConfig {
    @Bean
    public HttpMessageConverters customConverters() {
        // openFeign需要调用消息转换器
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }
}
