package com.vs.customer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoListener {
    // 需要将listener配置为spring的一个bean，且监听方法需要加上listener注解
    @RabbitListener(queues = "demo.queue.1")
    public void listenQueue(String message) {
        log.info("接收到来自demo.queue.1的消息: {}", message);
    }

    // TODO: fanout交换机接收消息
}
