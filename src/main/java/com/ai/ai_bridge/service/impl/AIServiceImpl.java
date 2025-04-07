package com.ai.ai_bridge.service.impl;

import com.ai.ai_bridge.helper.VectorStoreHelper;
import com.ai.ai_bridge.model.MessageRequest;
import com.ai.ai_bridge.model.MessageResponse;
import com.ai.ai_bridge.model.ModelName;
import com.ai.ai_bridge.service.AIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class AIServiceImpl implements AIService{
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final OpenAiChatModel openAiChatModel;
    private final OllamaChatModel ollamaChatModel;

    private final ChatClient openAIChatClient;
    private final ChatClient ollamaChatClient;
    private final VectorStoreHelper vectorStoreHelper;

    @Autowired
    public AIServiceImpl(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel, VectorStoreHelper vectorStoreHelper){
        this.openAiChatModel = openAiChatModel;
        this.ollamaChatModel = ollamaChatModel;
        this.openAIChatClient = ChatClient.create(openAiChatModel);
        this.ollamaChatClient = ChatClient.create(ollamaChatModel);
        this.vectorStoreHelper = vectorStoreHelper;

    }

    @Override
    public MessageResponse generateResponse(MessageRequest request) {
        try {
            validateRequest(request);
            return processRequest(request);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid chat model configuration: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error generating response: {}", e.getMessage());
            throw new RuntimeException("Failed to process request", e);
        }
    }

    private void validateRequest(MessageRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getMessage() == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (request.getModelName() == null) {
            throw new IllegalArgumentException("Model name cannot be null");
        }
    }


    private MessageResponse processRequest(MessageRequest request) {
        try {
            ChatClient selectedChatClient = getChatClient(request.getModelName());
            VectorStore vectorStore = vectorStoreHelper.getVectorStore(request.getModelName());

            String response = selectedChatClient.prompt()
                    .advisors(new QuestionAnswerAdvisor(vectorStore, new SearchRequest()))
                    .user(request.getMessage())
                    .call()
                    .content();

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessageResponse(response);
            messageResponse.setTimestamp(OffsetDateTime.now());
            return messageResponse;

        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage());
            throw new RuntimeException("Processing failed", e);
        }
    }

    private ChatClient getChatClient(ModelName modelName) {
        logger.debug("Getting chat model for: {}", modelName);

        return switch (modelName) {
            case OPENAI -> openAIChatClient;
            case OLLAMA -> ollamaChatClient;
            default -> throw new IllegalArgumentException("Unsupported chat model: " + modelName);
        };
    }
}
