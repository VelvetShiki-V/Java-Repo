package com.vs.cloud_model.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 定义普通交换机与队列，但不直接绑定消费者，与死信交换机绑定，延迟传递消息
public class DelayMessageConfig {
    @Bean
    @Qualifier("delayExchange")     // 指定bean绑定，排除bean歧义
    public DirectExchange delayExchange() {
        return new DirectExchange("delay.direct");
    }

    @Bean
    @Qualifier("delayQueue")
    public Queue delayQueue() {
        // 延迟队列绑定死信交换机
        return QueueBuilder.durable("delay.queue").deadLetterExchange("dlx.direct").build();
    }

    @Bean
    public Binding delayBinding(@Qualifier("delayQueue") Queue delayQueue,
                                @Qualifier("delayExchange") DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with("dead");
    }
}
