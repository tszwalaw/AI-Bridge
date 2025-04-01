package com.ai.ai_bridge.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ChatConfig {

    @Value("${default.chat.model.name}")
    private String chatModelName;

}
