package com.sourabh.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.chat")
@Getter
@Setter
public class ChatProperties {

    private int memoryWindow = 10;
}
