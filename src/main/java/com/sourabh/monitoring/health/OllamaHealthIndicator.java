package com.sourabh.monitoring.health;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaHealthIndicator implements HealthIndicator {

    private final ChatClient.Builder chatClientBuilder;

    @Override
    public Health health() {

        try {

            String response = chatClientBuilder
                    .build()
                    .prompt("hello")
                    .call()
                    .content();

            return Health.up()
                    .withDetail("service", "Ollama")
                    .build();

        } catch (Exception e) {

            return Health.down(e)
                    .withDetail("service", "Ollama")
                    .build();
        }
    }
}