package com.ai.ai_bridge.controller;

import com.ai.ai_bridge.api.AiApi;
import com.ai.ai_bridge.model.MessageRequest;
import com.ai.ai_bridge.model.MessageResponse;
import com.ai.ai_bridge.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIControllerDelegate implements AiApi {

    @Autowired
    private final AIService aiService;

    public AIControllerDelegate(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ResponseEntity<MessageResponse> aiMessagePost(MessageRequest messageRequest) {
        MessageResponse response = aiService.generateResponse(messageRequest);
        return ResponseEntity.ok(response);
    }
}
