package com.ai.ai_bridge.service;

import com.ai.ai_bridge.model.MessageRequest;
import com.ai.ai_bridge.model.MessageResponse;
import com.ai.ai_bridge.model.ModelName;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.ai.model.SpringAIModels.OLLAMA;
import static org.springframework.ai.model.SpringAIModels.OPENAI;

@Service
@RequiredArgsConstructor
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final OpenAiChatModel openAiChatModel;
    private final OllamaChatModel ollamaChatModel;

    public MessageResponse generateResponse(MessageRequest request) {
        try {
            return processRequest(request);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid chat model configuration: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error generating response: {}", e.getMessage());
            throw new RuntimeException("Failed to process request", e);
        }
    }

    private MessageResponse processRequest(MessageRequest request) {
        if (request == null || request.getMessage() == null || request.getModelName() == null) {
            throw new IllegalArgumentException("Invalid request");
        }

        try {
            ChatModel chatModel = getChatModel(request.getModelName());
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessageResponse(chatModel.call(request.getMessage()));
            messageResponse.setTimestamp(java.time.OffsetDateTime.now());
            return messageResponse;

        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage());
            throw new RuntimeException("Processing failed", e);
        }
    }

    private ChatModel getChatModel(ModelName modelName) {
        logger.debug("Getting chat model for: {}", modelName);

        return switch (modelName.getValue()) {
            case OPENAI -> openAiChatModel;
            case OLLAMA -> ollamaChatModel;
            default -> throw new IllegalArgumentException("Unsupported chat model: " + modelName);
        };
    }
}

