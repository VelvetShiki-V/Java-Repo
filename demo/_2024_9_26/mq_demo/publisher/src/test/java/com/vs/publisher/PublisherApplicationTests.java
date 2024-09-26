package com.vs.publisher;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PublisherApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void publishMessage() {
        // 指定队列和消息发送
        String queueName = "demo.queue.1";
        String message = "nihao123333333";
        // 发送
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
