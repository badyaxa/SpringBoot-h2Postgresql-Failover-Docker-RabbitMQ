package com.pr.tm.service;

import com.pr.tm.config.RabbitMQConfig;
import com.pr.tm.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class TaskMessageService {
    private final RabbitTemplate rabbitTemplate;

    public TaskMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTaskCreatedMessage(Task task) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TOPIC_EXCHANGE,
                RabbitMQConfig.TASK_ROUTING_KEY,
                task.toString().getBytes(StandardCharsets.UTF_8));
        log.info("The task creation notification has been sent: " + task.getTitle());
    }
}
