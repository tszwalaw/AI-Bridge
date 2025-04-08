package com.ai.ai_bridge.helper.impl;

import com.ai.ai_bridge.helper.DocumentHelper;
import com.ai.ai_bridge.helper.VectorStoreHelper;
import com.ai.ai_bridge.model.ModelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import com.ai.ai_bridge.config.DataConfig;

import java.io.File;
import java.util.List;

@Component
public class VectorStoreHelperImpl implements VectorStoreHelper {

    private static final Logger logger = LoggerFactory.getLogger(VectorStoreHelperImpl.class);
    private final DocumentHelper documentHelper;
    private final VectorStore ollamaVectorStore;
    private final DataConfig dataConfig;

    public VectorStoreHelperImpl(DocumentHelper documentHelper, VectorStore ollamaVectorStore, DataConfig dataConfig) {
        this.documentHelper = documentHelper;
        this.ollamaVectorStore = ollamaVectorStore;
        this.dataConfig = dataConfig;
    }

    private void initialiseVectorStore() {
        File filesFolder = new File(dataConfig.getStoragePath());

        if (!filesFolder.exists() || !filesFolder.isDirectory()) {
            logger.error("The specified directory does not exist.");
            return;
        }

        // Loop through all files in the folder
        File[] files = filesFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null && files.length > 0) {
            for (File pdfFile : files) {
                logger.info("Processing file: {}", pdfFile.getName());
                List<Document> documents = documentHelper.readData(pdfFile);
                ollamaVectorStore.add(documents);
            }
        } else {
            logger.warn("No files found in the files folder.");
        }
    }

    @Override
    public VectorStore getVectorStore(ModelName modelName) {
        logger.debug("Getting vector store for: {}", modelName);
        initialiseVectorStore();

        return switch (modelName) {
            //case OPENAI -> openAIVectorStore;
            case OLLAMA -> ollamaVectorStore;
            default -> throw new IllegalArgumentException("Unsupported chat model: " + modelName);
        };
    }
}
