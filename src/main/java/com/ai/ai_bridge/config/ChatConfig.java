package com.ai.ai_bridge.config;

import lombok.Getter;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ChatConfig {

    @Value("${default.chat.model.name}")
    private String chatModelName;


    @Bean
    VectorStore ollamaVectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel){
        return SimpleVectorStore.builder(embeddingModel)
                .build();
    }

    @Bean
    VectorStore openAIVectorStore(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel){
        return SimpleVectorStore.builder(embeddingModel)
                .build();
    }

}
