package com.ai.ai_bridge.helper.impl;


import com.ai.ai_bridge.config.DataConfig;
import com.ai.ai_bridge.helper.DocumentHelper;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import org.springframework.stereotype.Component;

@Component
public class DocumentHelperImpl implements DocumentHelper {
    private static final Logger logger = LoggerFactory.getLogger(DocumentHelperImpl.class);
    private final DataConfig dataConfig;

    public DocumentHelperImpl(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
    }

    @Override
    public List<Document> readData(File file) {
        try {
            String filePath = dataConfig.getStorageFolder() + file.getName();
            TextReader textReader = new TextReader(filePath);
            textReader.getCustomMetadata().put("file_name", file.getName());
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            return splitDocuments;
        } catch (Exception e) {
            logger.error("Error processing file {}: {}", file.getName(), e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
