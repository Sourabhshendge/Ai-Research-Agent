package com.sourabh.messaging.consumer;

import com.sourabh.document.service.impl.DocumentAsyncProcessor;
import com.sourabh.messaging.config.RabbitMqConfig;
import com.sourabh.messaging.event.DocumentUploadedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentUploadConsumer {

    private final DocumentAsyncProcessor
            documentAsyncProcessor;

    @RabbitListener(
            queues = RabbitMqConfig.DOCUMENT_QUEUE
    )
    public void consume(
            DocumentUploadedEvent event
    ) {

        log.info(
                "Received document upload event: {}",
                event.documentId()
        );

        documentAsyncProcessor.processDocument(
                event.documentId()
        );
    }
}
