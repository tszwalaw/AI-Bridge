package com.ai.ai_bridge.service;

import com.ai.ai_bridge.model.MessageRequest;
import com.ai.ai_bridge.model.MessageResponse;
import com.ai.ai_bridge.model.ModelName;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

import static org.springframework.ai.model.SpringAIModels.*;

@Service
@RequiredArgsConstructor
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final OpenAiChatModel openAiChatModel;
    private final OllamaChatModel ollamaChatModel;
    private final VectorStore ollamaVectorStore;
    private final ChatClient openAIChatClient;
    private final ChatClient ollamaChatClient;

    @Autowired
    public AIService(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel, VectorStore ollamaVectorStore){
        this.ollamaVectorStore = ollamaVectorStore;
        this.openAiChatModel = openAiChatModel;
        this.ollamaChatModel = ollamaChatModel;
        this.openAIChatClient = ChatClient.create(openAiChatModel);
        this.ollamaChatClient = ChatClient.create(ollamaChatModel);
        initializeVectorStore();
    }

    private void initializeVectorStore() {

    }

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
            VectorStore vectorStore = getVectorStore(request.getModelName());

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

        return switch (modelName.getValue()) {
            case OPENAI -> openAIChatClient;
            case OLLAMA -> ollamaChatClient;
            default -> throw new IllegalArgumentException("Unsupported chat model: " + modelName);
        };
    }

    private VectorStore getVectorStore(ModelName modelName) {
        logger.debug("Getting vector store for: {}", modelName);

        return switch (modelName.getValue()) {
            //case OPENAI -> openAIVectorStore;
            case OLLAMA -> ollamaVectorStore;
            default -> throw new IllegalArgumentException("Unsupported chat model: " + modelName);
        };
    }
}

