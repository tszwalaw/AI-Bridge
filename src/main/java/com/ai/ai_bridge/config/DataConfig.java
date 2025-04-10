package com.ai.ai_bridge.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DataConfig {
    
    @Value("${data.storage.path}")
    private String storagePath;

    @Value("${data.storage.folder}")
    private String storageFolder;

}