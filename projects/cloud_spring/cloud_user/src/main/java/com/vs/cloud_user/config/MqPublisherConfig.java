package com.vs.cloud_user.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqPublisherConfig {
    private final RabbitTemplate rabbitTemplate;

    // 在全局初始化后注册发送者回调事件
    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.warn("触发mq回调事件");
            log.info("Exchange: {}", returnedMessage.getExchange());
            log.info("RoutingKey: {}", returnedMessage.getRoutingKey());
            log.info("Message: {}", returnedMessage.getMessage());
            log.info("ReplyCode: {}", returnedMessage.getReplyCode());
            log.info("ReplyText: {}", returnedMessage.getReplyText());
        });
    }
}
