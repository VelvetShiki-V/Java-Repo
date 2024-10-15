package com.vs.cloud_common.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 配置java对象与队列消息转换器，并配置唯一ID保证业务幂等性
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class MessageConverterConfig {
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter jmc = new Jackson2JsonMessageConverter();
        // 消息唯一ID
        jmc.setCreateMessageIds(true);
        return jmc;
    }
}
