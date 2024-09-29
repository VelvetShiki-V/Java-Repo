package com.vs.customer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FanoutConfig {

    // 交换机创建
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return ExchangeBuilder.fanoutExchange("cloud.fanout").build();
//        // 或写为
////        return new FanoutExchange("cloud.fanout");
//    }
//
//    // 队列创建
//    @Bean
//    public Queue FanoutQueue1() {
//        return QueueBuilder.durable("fanout.queue1").build();
//        // 或写为
////        return new Queue("fanout.queue1");
//    }
//
//    // 交换机绑定队列
//    @Bean
//    public Binding FantouQueueBinding(Queue queue, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange);
//    }
}
