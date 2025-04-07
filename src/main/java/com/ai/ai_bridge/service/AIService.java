package com.ai.ai_bridge.service;

import com.ai.ai_bridge.model.MessageRequest;
import com.ai.ai_bridge.model.MessageResponse;

public interface AIService {
    /**
     * Generates a response based on the provided message request
     * @param request The message request containing the input message and model preferences
     * @return MessageResponse containing the generated response and timestamp
     */
    MessageResponse generateResponse(MessageRequest request);
}

