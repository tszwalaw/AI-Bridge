package com.ai.ai_bridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataConfig {
    
    @Value("${data.storage.path}")
    private String storagePath;

    public String getStoragePath() {
        return storagePath;
    }
} 