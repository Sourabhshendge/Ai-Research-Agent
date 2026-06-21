package com.sourabh.messaging.controller;

import com.sourabh.messaging.config.RabbitMqConfig;
import com.sourabh.messaging.producer.DocumentUploadPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rabbit")
public class RabbitTestController {

    private final RabbitTemplate rabbitTemplate;
    private final DocumentUploadPublisher publisher;

    @GetMapping("/send")
    public String send() {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.DOCUMENT_EXCHANGE,
                RabbitMqConfig.DOCUMENT_ROUTING_KEY,
                "hello"
        );

        return "sent";
    }

    @GetMapping("/publish/{id}")
    public String publish(
            @PathVariable Long id
    ) {

        publisher.publish(id);

        return "published";
    }
}
