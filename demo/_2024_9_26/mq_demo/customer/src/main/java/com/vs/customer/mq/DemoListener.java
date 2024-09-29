package com.vs.customer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DemoListener {
    // 需要将listener配置为spring的一个bean，且监听方法需要加上listener注解
//    @RabbitListener(queues = "demo.queue.1")
//    public void listenQueue(String message) {
//        log.info("接收到来自demo.queue.1的消息: {}", message);
//    }

//    @RabbitListener(queues = "cloud.fanout.queue1")
//    public void listenFanout1(String message) {
//        log.info("1号监听到fanout queue1: {}", message);
//    }
//
//    @RabbitListener(queues = "cloud.fanout.queue2")
//    public void listenFanout2(String message) {
//        log.info("2号监听到fanout queue1: {}", message);
//    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "cloud.direct", type = ExchangeTypes.DIRECT),
            key = {"c1", "c2"}
    ))
    public void directQueue1(List<String> msg) {
        log.info("q1接收到消息: {}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "cloud.direct", type = ExchangeTypes.DIRECT),
            key = {"c1", "c2"}
    ))
    public void directQueue2(List<String> msg) {
        log.info("q2接收到消息: {}", msg);
    }
}
