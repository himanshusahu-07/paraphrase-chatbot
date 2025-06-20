package org.example.bot.controller;

import org.example.bot.dto.ChatRequest;
import org.example.bot.dto.ChatResponse;
import org.example.bot.service.GeminiService;
import org.example.bot.util.PromptBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // Optional: allow frontend requests
public class ChatController {

    private final GeminiService geminiService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest request) {
        ChatResponse response = new ChatResponse();
        try {
            String prompt = PromptBuilder.buildPrompt(request);
            String reply = geminiService.getGeminiReply(prompt, geminiApiKey);
            response.setContent(reply);
            response.setError(false);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMessage("Server Error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
