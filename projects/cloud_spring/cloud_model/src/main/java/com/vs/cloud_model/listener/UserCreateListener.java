package com.vs.cloud_model.listener;

import com.vs.cloud_common.domain.UserInfo;
import com.vs.cloud_model.domain.Model;
import com.vs.cloud_model.service.impl.ModelServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreateListener {
    private final ModelServiceImpl modelService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "model.create.queue1"),
            exchange = @Exchange(name = "cloud.topic", type = ExchangeTypes.TOPIC),
            key = {"k1"}
    ))
    public void listenUserCreate(UserInfo user) {
        log.info("model.create.queue1接收到用户mq消息: {}", user);
        // 执行数据创建
        modelService.modelCreate(new Model(user.getUid(),
                "default ones",
                user.getName(),
                "ZZZ",
                "void",
                "void",
                LocalDateTime.now(),
                LocalDateTime.now(),
                10));
        log.info("已同步执行数据创建");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "model.test.queue"),
            exchange = @Exchange(name = "cloud.topic", type = ExchangeTypes.TOPIC),
            key = {"k1"}
    ))
    public void listenTest(UserInfo user) {
        log.info("model.test.queue接收到用户信息: {}", user);
    }
}
