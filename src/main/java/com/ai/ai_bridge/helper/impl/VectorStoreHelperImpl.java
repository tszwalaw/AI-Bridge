package com.ai.ai_bridge.helper.impl;

import com.ai.ai_bridge.helper.DocumentHelper;
import com.ai.ai_bridge.helper.VectorStoreHelper;
import com.ai.ai_bridge.model.ModelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class VectorStoreHelperImpl implements VectorStoreHelper {

    private static final Logger logger = LoggerFactory.getLogger(DocumentHelperImpl.class);
    private final DocumentHelper documentHelper;
    private final VectorStore ollamaVectorStore;

    public VectorStoreHelperImpl(DocumentHelper documentHelper, VectorStore ollamaVectorStore) {
        this.documentHelper = documentHelper;
        this.ollamaVectorStore = ollamaVectorStore;
    }

    private void initialiseVectorStore() {
        File pdfFolder = new File("src/main/resources/pdf");

        if (!pdfFolder.exists() || !pdfFolder.isDirectory()) {
            logger.error("The specified directory does not exist.");
            return;
        }

        // Loop through all files in the folder
        File[] files = pdfFolder.listFiles((dir, name) -> name.endsWith(".pdf"));

        if (files != null && files.length > 0) {
            for (File pdfFile : files) {
                logger.info("Processing file: {}", pdfFile.getName());
                List<Document> documents = documentHelper.readPDF(pdfFile);
                ollamaVectorStore.add(documents);
            }
        } else {
            logger.warn("No PDF files found in the pdf folder.");
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
