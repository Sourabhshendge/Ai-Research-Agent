package com.sourabh.messaging.producer;

import com.sourabh.messaging.config.RabbitMqConfig;
import com.sourabh.messaging.event.DocumentUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentUploadPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(
            Long documentId
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.DOCUMENT_EXCHANGE,
                RabbitMqConfig.DOCUMENT_ROUTING_KEY,
                new DocumentUploadedEvent(
                        documentId
                )
        );
    }
}