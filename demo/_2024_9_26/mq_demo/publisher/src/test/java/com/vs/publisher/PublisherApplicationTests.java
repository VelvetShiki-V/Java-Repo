package com.vs.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class PublisherApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void publishMessage() {
        // 指定队列和消息发送
        String exchange = "cloud.direct";
        // 发送string
//        String message = "hello!!!DIRECT!!!";

        // 发送object
        List<String> list = new LinkedList<>(List.of("hello", "world", "ni", "hao"));
        // 发送(默认的json转换不好用，需要用别的工具转字节流)
//        rabbitTemplate.convertAndSend(exchange, "c1", message);
//        rabbitTemplate.convertAndSend(exchange, "c2", message);
        rabbitTemplate.convertAndSend(exchange, "c2", list);
        rabbitTemplate.convertAndSend(exchange, "c2", list);
    }
}
