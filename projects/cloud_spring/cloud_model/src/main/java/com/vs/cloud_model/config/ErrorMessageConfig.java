package com.vs.cloud_model.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

// mq消费者失败重试路由处理
@Configuration
public class ErrorMessageConfig {

    // 定义交换机
    @Bean
    @Qualifier("errorExchange")
    public DirectExchange errorExchange() {
        return new DirectExchange("error.direct");
    }

    // 定义路由队列
    @Bean
    @Qualifier("errorQueue")
    public Queue errorQueue() {
        return new Queue("error.queue");
    }

    // 绑定路由与交换机，并指定路由键
    // 指定bean绑定，排除bean歧义
    @Bean
    public Binding errorQueueBinding(@Qualifier("errorQueue") Queue errorQueue,
                                     @Qualifier("errorExchange")DirectExchange errorExchange) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    // 路由错误消息到该队列
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate template) {
        return new RepublishMessageRecoverer(template, "error.direct", "error");
    }
}
