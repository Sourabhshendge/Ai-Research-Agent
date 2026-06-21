package com.sourabh.monitoring.health;

import com.sourabh.vector.service.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QdrantHealthIndicator implements HealthIndicator {

    private final VectorStoreService vectorStoreService;

    @Override
    public Health health() {

        if (vectorStoreService.isHealthy()) {

            return Health.up()
                    .withDetail("service", "Qdrant")
                    .build();
        }

        return Health.down()
                .withDetail("service", "Qdrant")
                .build();
    }
}