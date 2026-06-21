package com.sourabh.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

@Configuration
public class RabbitMqConfig {

    public static final String DOCUMENT_QUEUE =
            "document.upload.queue";

    public static final String DOCUMENT_EXCHANGE =
            "document.exchange";

    public static final String DOCUMENT_ROUTING_KEY =
            "document.uploaded";

    @Bean
    public Queue documentQueue() {

        System.out.println("=== Rabbit Queue Bean Created ===");

        return QueueBuilder
                .durable(DOCUMENT_QUEUE)
                .build();
    }

    @Bean
    public TopicExchange documentExchange() {
        return new TopicExchange(
                DOCUMENT_EXCHANGE
        );
    }

    @Bean
    public Binding documentBinding(
            Queue documentQueue,
            TopicExchange documentExchange
    ) {
        return BindingBuilder
                .bind(documentQueue)
                .to(documentExchange)
                .with(DOCUMENT_ROUTING_KEY);
    }

    @Bean
    public AmqpAdmin amqpAdmin(
            ConnectionFactory connectionFactory
    ) {
        RabbitAdmin rabbitAdmin =
                new RabbitAdmin(connectionFactory);

        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }

    @Bean
    public CommandLineRunner rabbitRunner(
            ConnectionFactory connectionFactory
    ) {
        return args -> {
            System.out.println(
                    "RabbitMQ Connected: "
                            + connectionFactory
            );
        };
    }

    @Bean
    public CommandLineRunner rabbitAdminTest(
            AmqpAdmin amqpAdmin
    ) {
        return args -> {

            System.out.println(
                    "Queues = "
                            + amqpAdmin.getQueueInfo(
                            DOCUMENT_QUEUE
                    )
            );
        };
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}