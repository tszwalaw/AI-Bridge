package com.ai.ai_bridge.helper;

import com.ai.ai_bridge.model.ModelName;
import org.springframework.ai.vectorstore.VectorStore;

public interface VectorStoreHelper {

    //**
    VectorStore getVectorStore(ModelName modelName);
}
