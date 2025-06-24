package com.example.speingai.chat;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/groq")
public class GroqController {

    private final ChatModel chatModel;

    @Autowired
    public GroqController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> testGroq() {
        try {
            String promptText = "파이썬은 무엇인가요?";
            Prompt prompt = new Prompt(promptText);
            ChatResponse response = chatModel.call(prompt);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "response", response.getResult().getOutput().getText()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "status", "error",
                            "message", "Failed to process request: " + e.getMessage()
                    ));
        }
    }
}