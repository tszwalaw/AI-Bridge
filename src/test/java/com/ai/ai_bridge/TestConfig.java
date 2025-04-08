package com.ai.ai_bridge;

import com.ai.ai_bridge.config.DataConfig;
import com.ai.ai_bridge.helper.DocumentHelper;
import com.ai.ai_bridge.helper.VectorStoreHelper;
import com.ai.ai_bridge.helper.impl.VectorStoreHelperImpl;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public DataConfig dataConfig() {
        return new DataConfig() {
            @Override
            public String getStoragePath() {
                return "src/test/resources/textFiles";
            }
        };
    }

    @Bean
    @Primary
    public DocumentHelper documentHelper() {
        return new DocumentHelper() {
            @Override
            public List<Document> readData(File file) {
                return new ArrayList<>();
            }
        };
    }

    @Bean
    @Primary
    public VectorStore vectorStore() {
        return new VectorStore() {
            @Override
            public void add(List<Document> documents) {
                // Do nothing
            }

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return new ArrayList<>();
            }

            @Override
            public void delete(Filter.Expression filterExpression) {
                // Do nothing
            }

            @Override
            public void delete(List<String> idList) {
                // Do nothing
            }
        };
    }

    @Bean
    @Primary
    public VectorStoreHelper vectorStoreHelper(DocumentHelper documentHelper, 
                                             VectorStore vectorStore,
                                             DataConfig dataConfig) {
        return new VectorStoreHelperImpl(documentHelper, vectorStore, dataConfig);
    }
} 