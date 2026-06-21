package com.sourabh.monitoring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final Environment environment;

    @GetMapping("/summary")
    public Map<String, Object> summary() {

        Runtime runtime = Runtime.getRuntime();

        return Map.of(
                "application", "AI Research Agent",
                "profile", String.join(",", environment.getActiveProfiles()),
                "javaVersion", System.getProperty("java.version"),
                "availableProcessors", runtime.availableProcessors(),
                "maxMemoryMb", runtime.maxMemory() / 1024 / 1024,
                "usedMemoryMb",
                (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024,
                "uptimeSeconds",
                ManagementFactory.getRuntimeMXBean().getUptime() / 1000
        );
    }
}